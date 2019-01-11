package com.experian.eda.ace.hadoop;

import com.opencsv.CSVParser;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVFileInputFormatter extends FileInputFormat<LongWritable, List<Text>> {


    public RecordReader<LongWritable, List<Text>> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        final String csvDelimiter = ",";

        Character separator = null;
        if (csvDelimiter != null && csvDelimiter.length() == 1) {
            separator = csvDelimiter.charAt(0);
        }

        return new CSVRecordReader(separator);
    }

    @Override
    protected boolean isSplitable(JobContext context, Path file) {

        /*CompressionCodec codec =
                new CompressionCodecFactory(HadoopCompat.getConfiguration(context))
                        .getCodec(file);

        return codec == null;*/
        return true;
    }


    public static class CSVRecordReader extends RecordReader<LongWritable, List<Text>> {
        private LineRecordReader reader;
        private List<Text> value;
        private final CSVParser parser;

        public CSVRecordReader(Character csvDelimiter) {
            this.reader = new LineRecordReader();
            if (csvDelimiter == null) {
                parser = new CSVParser();
            } else {
                parser = new CSVParser(csvDelimiter);
            }
        }

        @Override
        public void initialize(InputSplit split,
                               TaskAttemptContext context)
                throws IOException, InterruptedException {
            reader.initialize(split, context);
        }

        @Override
        public boolean nextKeyValue()
                throws IOException, InterruptedException {
            if (reader.nextKeyValue()) {
                loadCSV();
                return true;
            } else {
                value = null;
                return false;
            }
        }

        private void loadCSV() throws IOException {
            String line = reader.getCurrentValue().toString();
            String[] tokens = parser.parseLine(line);
            value = Arrays.asList(convert(tokens));
        }

        private Text[] convert(String[] s) {
            Text t[] = new Text[s.length];
            for (int i = 0; i < t.length; i++) {
                t[i] = new Text(s[i]);
            }
            return t;
        }

        @Override
        public LongWritable getCurrentKey()
                throws IOException, InterruptedException {
            return reader.getCurrentKey();
        }

        @Override
        public List<Text> getCurrentValue()
                throws IOException, InterruptedException {
            return value;
        }

        @Override
        public float getProgress()
                throws IOException, InterruptedException {
            return reader.getProgress();
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }
}


