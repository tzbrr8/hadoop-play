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
import org.apache.commons.lang3.StringUtils;
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
    protected void map(<AvroKey<GenericData.Record> key, NullWritable value, Context context) throws IOException, InterruptedException {

        //load the model from the hadoop file system
        final FileSystem fs = FileSystem.get(URI.create("hdfs://localhost:9000/home/testdata/tensorflow/btree1k/btrees_1k_class.zip"), context.getConfiguration());
        final FSDataInputStream open = fs.open(new Path("hdfs://localhost:9000/home/testdata/tensorflow/btree1k/btrees_1k_class.zip"));
        final byte[] compressedModel = new byte[open.available()];
        open.readFully(compressedModel);
        open.close();

        // initialise Tensorflow plugin libraries with the model
        final ModelCharacteristic characteristic = ModelCharacteristicFactory.getCharacteristic(getMetaGraphDef(compressedModel));
        final RuntimeTensorFlowExecutor executor = new RuntimeTensorFlowExecutor(compressedModel, characteristic);

        //initialise the input values for model
        final IMappedGroupCharacteristic groupCharacteristic = new MappedGroupCharacteristic();
        groupCharacteristic.addCharacteristic("address_time", getValue(10.0));
        groupCharacteristic.addCharacteristic("age", getValue(30.0));
        groupCharacteristic.addCharacteristic("bank_time", getValue(10.0));
        groupCharacteristic.addCharacteristic("income", getValue(30000.0));

        //add the output variables
        characteristic.getOutputs().stream().forEach(output -> groupCharacteristic.addCharacteristic(output.getName(), getValue(getDefaultValue(output.getType()))));

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
                    result.put(new Text(o.getName()), new ObjectWritable(groupCharacteristic.getAtomicCharacteristic(o.getName()).getValue().toString()));
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

    private IMappedCharacteristic getValue(final Object input) {
        return new IMappedAtomicCharacteristic() {
            private Object value = input;

            @Override
            public DataType getDataType() {
                if ((input instanceof Double) || (input instanceof Float)) {
                    return DataType.DOUBLE;

                } else if ((input instanceof Integer) || (input instanceof Long)) {
                    return DataType.LONG;
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
                return value;
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

    private Object getDefaultValue(final DataType type) {
        switch (type) {
            case DOUBLE:
                return 0d;

            case LONG:
                return 0l;

            case STRING:
                return StringUtils.EMPTY;

            default:
                throw new IllegalArgumentException("invalid type {" + type + "}");
        }
    }

}
