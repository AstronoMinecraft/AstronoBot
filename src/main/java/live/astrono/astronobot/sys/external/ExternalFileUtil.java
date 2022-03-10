package main.java.live.astrono.astronobot.sys.external;

import java.io.File;
import java.io.IOException;

public class ExternalFileUtil {
    
    public static File generateFile(String name) throws IOException {
        File file = new File(ExternalFiles.OTHER_CACHE_DIR, name);
        if (file.exists()) {
            file.delete();
        }
        
        file.createNewFile();
        return file;
    }
    
    public static File getFile(String name) {
        return new File(ExternalFiles.OTHER_CACHE_DIR, name);
    }
}