package main.java.live.astrono.astronobot.sys.external;

import java.io.File;

public interface ExternalFiles {
    File OTHER_CACHE_DIR = new ExternalFileBuilder()
            .isDirectory(true)
            .setName("other_cache")
            .buildFile();
}