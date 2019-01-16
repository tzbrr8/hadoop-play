package com.experian.eda.ace.hadoop.csv;

import com.experian.eda.framework.runtime.common.utils.CsvParser;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumWriter;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.node.TextNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CSVProcessor {
    static final Schema STRING_SCHEMA = Schema.create(Schema.Type.STRING);

    private CSVProcessor() {
    }

    public static Path processCsvFileToAvro(FileSystem fs, Path file) throws Exception {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(file)))) {

            final String[][] csv = new CsvParser().parseCsv(br);
            final List<String> headers = Arrays.asList(csv[0]);
            final Schema schema = getSchemaFromHeaders("testpojo", headers);
            return csvToAvro(fs, file, csv, schema);
        }
    }

    private static Path csvToAvro(FileSystem fs, Path file, String[][] csv, Schema schema) throws IOException {
        final Path avroFile = new Path(String.format("%s%s", file.toString().substring(0, file.toString().lastIndexOf(".")), ".avro"));
        try (FSDataOutputStream fsDataOutputStream = fs.create(avroFile)) {
            final DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
            try (DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(writer)) {
                fileWriter.create(schema, fsDataOutputStream);

                for (int x = 1; x < csv.length; x++) {
                    final GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(schema);
                    for (int y = 0; y < csv[x].length; y++) {
                        genericRecordBuilder.set(schema.getFields().get(y), String.valueOf(csv[x][y]));
                    }
                    fileWriter.append(genericRecordBuilder.build());
                }
            }
        }
        return avroFile;
    }

    private static Schema getSchemaFromHeaders(String schemaName, List<String> headers) {
        final Set<String> namespaces = new TreeSet<>();

        final List<Schema.Field> collectedFields = headers.stream()
                .map(name -> {
                    String fieldName = name;
                    final String[] split = name.split("\\.");
                    if(split.length==2) {
                        namespaces.add(split[0]);
                        fieldName = split[1];
                    }

                    return new Schema.Field(
                            fieldName,
                            STRING_SCHEMA,
                            null, new TextNode(""));
                })
                .collect(Collectors.toList());


        final Schema result = Schema.createRecord(schemaName, (String) null, namespaces.stream().findFirst().get(), false);
        result.setFields(collectedFields);
        return result;
    }
}
