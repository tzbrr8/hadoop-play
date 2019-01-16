package com.experian.eda.ace.hadoop;

import com.experian.ace.tensorflow.runtime.RuntimeTensorFlowExecutor;
import com.experian.ace.tensorflow.runtime.TensorFlowParser;
import com.experian.ace.tensorflow.runtime.prediction.ModelCharacteristic;
import com.experian.ace.tensorflow.runtime.prediction.ModelCharacteristicFactory;
import com.experian.eda.component.ace.executor.runtime.MappedGroupCharacteristic;
import com.experian.eda.framework.runtime.ace.DataType;
import com.experian.eda.framework.runtime.ace.IMappedAtomicCharacteristic;
import com.experian.eda.framework.runtime.ace.IMappedCharacteristic;
import com.experian.eda.framework.runtime.ace.IMappedGroupCharacteristic;
import com.experian.eda.framework.runtime.common.exceptions.StratmanException;

import org.apache.avro.generic.GenericData;
import org.apache.avro.mapred.AvroKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.SavedModelBundle;

import java.io.IOException;
import java.net.URI;

public class DaMapper extends Mapper<AvroKey<GenericData.Record>, NullWritable, Text, MapWritable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaMapper.class);

    @Override
    protected void map(AvroKey<GenericData.Record> key, NullWritable value, Context context) throws IOException, InterruptedException {

        //load the model from the hadoop file system
        final String modelFile = String.format("%s/%s",context.getConfiguration().get("testFolder"),context.getConfiguration().get("model"));
        final FileSystem fs = FileSystem.get(URI.create(modelFile), context.getConfiguration());
        final FSDataInputStream open = fs.open(new Path(modelFile));
        final byte[] compressedModel = new byte[open.available()];

        open.readFully(compressedModel);
        open.close();

        // initialise Tensorflow plugin libraries with the model
        final ModelCharacteristic characteristic = ModelCharacteristicFactory.getCharacteristic(getMetaGraphDef(compressedModel));
        final RuntimeTensorFlowExecutor executor = new RuntimeTensorFlowExecutor(compressedModel, characteristic);

        //initialise the input And output values for model
        final IMappedGroupCharacteristic groupCharacteristic = new MappedGroupCharacteristic();
        key.datum().getSchema().getFields().forEach(fieldName -> {
            final String fieldValue = (String) key.datum().get(fieldName.name());
            groupCharacteristic.addCharacteristic(fieldName.name(), fieldValue.isEmpty() ? null : getValue(fieldValue));
        });


        //add the output variables
        characteristic.getOutputs().forEach(output -> groupCharacteristic.addCharacteristic(output.getName(), getValue(getDefaultValue(output.getType()))));

        //do the execution of the plugin
        try {
            executor.execute(groupCharacteristic);

            //copy the outputs for the results to be processed. (using a map)
            final MapWritable result = new MapWritable();

            //add inputs to the out put file
            characteristic.getInputs().entrySet().forEach(entry -> {
                try {
                    result.put(new Text(entry.getKey()), new ObjectWritable(groupCharacteristic.getAtomicCharacteristic(entry.getKey()).getValue().toString()));
                } catch (StratmanException e) {
                    e.printStackTrace();
                }
            });

            // add output to output file
            characteristic.getOutputs().forEach(o -> {
                try {
                    result.put(new Text(o.getName()), new ObjectWritable(String.valueOf(groupCharacteristic.getAtomicCharacteristic(o.getName()).getValue())));
                } catch (StratmanException e) {
                    e.printStackTrace();
                }
            });

            //return the results object for processing.
            context.write(new Text("result"), result);

        } catch (StratmanException e) {
            LOGGER.error("Something went wrong with executing", e);
        }
    }

    private IMappedCharacteristic getValue(final String input) {
        return new IMappedAtomicCharacteristic() {
            private Object value = input;

            @Override
            public DataType getDataType() {

                if (NumberUtils.isNumber(input)) {
                    if (input.contains(".")) {
                        return DataType.DOUBLE;
                    } else {
                        return DataType.LONG;
                    }
                }
                return DataType.STRING;
            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public boolean isArray() {
                return false;
            }

            @Override
            public Object getValue() throws StratmanException {

                if (value == null || String.valueOf(value).isEmpty())
                    return "";
                switch(getDataType()){
                    case DOUBLE: return (value instanceof Float) ? ((Float) value) : Float.valueOf((String) value);
                    case LONG: return (value instanceof Long) ? ((Long) value) : Long.valueOf((String) value);
                    default:return value;
                }
            }

            @Override
            public void setValue(final Object value) throws StratmanException {
                this.value = value;
            }
        };
    }

    private byte[] getMetaGraphDef(final byte[] content) {
        try (final SavedModelBundle model = TensorFlowParser.parse(content)) {
            return model.metaGraphDef();
        }
    }

    private String getDefaultValue(final DataType type) {
        switch (type) {
            case DOUBLE:
                return "0.0";

            case LONG:
                return "0";

            case STRING:
                return StringUtils.EMPTY;

            default:
                throw new IllegalArgumentException("invalid type {" + type + "}");
        }
    }

}
