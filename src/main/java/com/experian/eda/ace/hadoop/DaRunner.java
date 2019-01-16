package com.experian.eda.ace.hadoop;


import com.experian.eda.ace.hadoop.csv.CSVProcessor;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.AvroFSInput;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import javax.el.PropertyNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Properties;


public class DaRunner extends Configured implements Tool {

    public int run(String[] args) throws Exception {


        final CommandLine commandLine = processCommandLine(args);

        final FileSystem fs = FileSystem.get(URI.create(commandLine.getOptionValue("testFolder")), getConf());

        final Properties testProps = loadTestPropertiesFile(fs, commandLine.getOptionValue("testFolder"));

        final Path avroInputFile = createAvroFileFromInputFile(fs, testProps);

        Job job = Job.getInstance(getConf());
        job.setJarByClass(getClass());
        job.setJobName("da-hadoop-demo");

        job.getConfiguration().setBoolean(MRJobConfig.MAPREDUCE_JOB_USER_CLASSPATH_FIRST, true);

        //copy the properties to be available to the mapper....

        testProps.forEach((key, value) -> {
            job.getConfiguration().set((String) key, (String) value);
        });


        if ("runtime".equalsIgnoreCase(testProps.getProperty("testtype"))) {
            job.setMapperClass(DaMapper.class);
        } else if ("ser".equalsIgnoreCase(testProps.getProperty("testtype"))) {
            job.setMapperClass(DaSerMapper.class);
        } else {
            throw new PropertyNotFoundException("testtype Not found or correct in properties file for test.");
        }


        job.setReducerClass(DaReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(MapWritable.class);
        job.setOutputValueClass(List.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        final Schema jobSchema = extractSchema(fs, avroInputFile);

        AvroJob.setInputKeySchema(job, jobSchema);

        job.setInputFormatClass(AvroKeyInputFormat.class);
        FileInputFormat.setInputPaths(job, avroInputFile);


        prepareOutputFolder(fs, job, testProps);


        int returnValue = job.waitForCompletion(true) ? 0 : 1;

        System.out.println(job.isSuccessful() ? "Job was successful" : "Job was not successful");

        return returnValue;

    }

    private Schema extractSchema(FileSystem fs, Path avroInputFile) throws IOException {

        final GenericDatumReader<Object> reader = new GenericDatumReader<Object>();
        final FileContext fc = FileContext.getFileContext(getConf());
        final AvroFSInput avroFSInput = new AvroFSInput(fc, avroInputFile);
        try (FileReader<Object> fileReader = DataFileReader.openReader(avroFSInput, reader)) {
            return fileReader.getSchema();
        }
    }

    private void prepareOutputFolder(FileSystem fs, Job job, Properties testProps) throws IOException {
        final String outputFolder = String.format("%s/%s", testProps.getProperty("testFolder"), "output");
        final Path outputPath = new Path(outputFolder);
        FileOutputFormat.setOutputPath(job, outputPath);

        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
    }


    private CommandLine processCommandLine(final String[] args) {

        Options options = new Options();

        Option input = new Option("t", "testFolder", true, "path to test folder");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        System.exit(-1);
        return null;
    }

    private Path createAvroFileFromInputFile(final FileSystem fs, final Properties testProperties) throws Exception {


        final String inputfile = testProperties.getProperty("inputfile");
        final String testRootFolder = testProperties.getProperty("testFolder");

        if (inputfile == null) {
            throw new PropertyNotFoundException("property input not configured in test properties");
        }

        final String testInputFile = String.format("%s/%s", testRootFolder, inputfile);
        final Path testInputFilePath = new Path(testInputFile);

        if (!fs.exists(testInputFilePath)) {
            throw new FileNotFoundException(String.format("Input file %s file does not exist in test folder %s", inputfile, testRootFolder));
        }

        return CSVProcessor.processCsvFileToAvro(fs, testInputFilePath);
    }

    private Properties loadTestPropertiesFile(FileSystem fs, String testPathRoot) throws IOException {
        String confFile = testPathRoot + "/conf.properties";
        final Path path = new Path(confFile);
        if (fs.exists(path)) {
            final Properties props = new Properties();
            try (FSDataInputStream openFileStream = fs.open(path)) {
                try {
                    props.load(openFileStream);
                } catch (IOException e) {
                    throw new FileNotFoundException(String.format("property file '%s' not found or loaded in test folder", testPathRoot));
                }
            }
            props.setProperty("testFolder", testPathRoot);
            return props;
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new DaRunner(), args));
    }

}
