package com.experian.eda.ace.hadoop;

import com.experian.eda.component.decisionagent.strategyloader.IStrategyLoader;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;

public class HadoopStratergyLoader implements IStrategyLoader {


    private static FileSystem hadoopFileSystem;
    private static String serPath;

    public static void setHadoopFileSystem(FileSystem hadoopFileSystem) {
        HadoopStratergyLoader.hadoopFileSystem = hadoopFileSystem;
    }

    @Override
    public InputStream getStream(String serFilename) throws IOException {
        return hadoopFileSystem.open(new Path(String.format("%s/%s", this.serPath, serFilename)));
    }


    public static void setSerPath(String serPath) {
        HadoopStratergyLoader.serPath = serPath;
    }
}
