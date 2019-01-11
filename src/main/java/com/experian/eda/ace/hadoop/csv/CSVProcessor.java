package com.experian.eda.ace.hadoop.csv;

import com.experian.eda.framework.runtime.common.utils.CsvParser;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
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
import java.util.stream.Collectors;

public class CSVProcessor {
    static final Schema STRING_SCHEMA = Schema.create(Schema.Type.STRING);
    public static final String OFFENDING_CHARS = "[_\\.\\ ]";
    public static final String BLANK_CHAR = "";

    private CSVProcessor() {
    }

    public static Path processCsvFileToAvro(FileSystem fs, Path file) throws Exception {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(file)))) {

            final String[][] csv = new CsvParser().parseCsv(br);
            final List<String> headers = Arrays.asList(csv[0]);
            final Schema schema = getSchemaFromHeaders(headers);
            return csvToAvro(fs, file, csv, headers, schema);
        }
    }

    private static Path csvToAvro(FileSystem fs, Path file, String[][] csv, List<String> headers, Schema schema) throws IOException {
        final Path avroFile = new Path(String.format("%s%s",file.toString().substring(0, file.toString().lastIndexOf(".")), ".avro"));
        try (FSDataOutputStream fsDataOutputStream = fs.create(avroFile)) {
            final DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
            try (DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(writer)) {
                fileWriter.create(schema, fsDataOutputStream);
                for (int x = 1; x < csv.length; x++) {
                    final GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(schema);
                    for (int y = 0; y < csv[x].length; y++) {
                        genericRecordBuilder.set(headers.get(y).toLowerCase().replaceAll(OFFENDING_CHARS, BLANK_CHAR), String.valueOf(csv[x][y]));
                    }
                    GenericData.Record record = genericRecordBuilder.build();
                    fileWriter.append(record);
                }
            }
        }
        return avroFile;
    }

    private static Schema getSchemaFromHeaders(List<String> headers) {
        final List<Schema.Field> collectedFields = headers.stream()
                .map(name -> new Schema.Field(
                        name.toLowerCase().replaceAll(OFFENDING_CHARS, BLANK_CHAR),
                        STRING_SCHEMA,
                        null,new TextNode(""))
                )
                .collect(Collectors.toList());

        return Schema.createRecord(collectedFields);
    }
}
