package com.experian.eda.ace.hadoop;

import com.experian.ace.tensorflow.runtime.TensorFlowParser;
import com.experian.eda.component.decisionagent.JSEMBeanInterface;
import com.experian.eda.decisionagent.interfaces.nt.NTJSEMBeanInterface;
import com.experian.eda.framework.runtime.ace.DataType;
import com.experian.eda.framework.runtime.ace.IMappedAtomicCharacteristic;
import com.experian.eda.framework.runtime.ace.IMappedCharacteristic;
import com.experian.eda.framework.runtime.common.exceptions.StratmanException;
import org.apache.avro.generic.GenericData;
import org.apache.avro.mapred.AvroKey;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.SavedModelBundle;
import testpojo.Btree1k;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;

public class DaSerMapper extends Mapper<AvroKey<GenericData.Record>, NullWritable, Text, MapWritable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaSerMapper.class);
    private JSEMBeanInterface decisionAgent;

    public DaSerMapper() {
        decisionAgent = NTJSEMBeanInterface.instance();
    }

    @Override
    protected void map(AvroKey<GenericData.Record> key, NullWritable value, Context context) throws IOException, InterruptedException {

        //load the model from the hadoop file system


        Btree1k data = new Btree1k();
        data.getOCONTROL().setValue("ALIAS", "btree1k");
        data.getOCONTROL().setValue("SIGNATURE", "btree1k");
        // Move the input Avro record data into the Decision Agent bean interface.
        key.datum().getSchema().getFields().forEach( field->{


            final String fieldNameAdjusted = field.name().replaceAll("[\\. _-]", "").toLowerCase();
            try {
                BeanUtils.setProperty(data.getLAYOUT() , fieldNameAdjusted , key.datum().get(field.name()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        });


        HadoopStratergyLoader.setHadoopFileSystem(FileSystem.get(URI.create(context.getConfiguration().get("testFolder")), context.getConfiguration()));

        HadoopStratergyLoader.setSerPath(context.getConfiguration().get("testFolder"));

        int returnCode = decisionAgent.execute(data, 31);
        //copy the outputs for the results to be processed. (using a map)
        final MapWritable result = new MapWritable();
        if(returnCode == 0){


            try {
                BeanUtils.describe(data.getLAYOUT()).forEach((fieldName,fieldvalue) ->{
                    result.put(new Text((String)fieldName), new ObjectWritable(fieldvalue));
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        //return the results object for processing.
        context.write(new Text("result"), result);
    }

}
