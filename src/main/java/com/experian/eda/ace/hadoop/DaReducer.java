package com.experian.eda.ace.hadoop;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class DaReducer extends Reducer<Text, MapWritable, String, String> {

    @Override
    protected void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {

        final StringBuilder resultRecord = new StringBuilder();

        values.forEach(row ->
                row.entrySet().forEach(item ->
                        resultRecord.append(String.format(", %s : %s", item.getKey(), ((ObjectWritable) item.getValue()).get()))));

        resultRecord.deleteCharAt(0);
        context.write(key.toString(), resultRecord.toString());
    }
}
