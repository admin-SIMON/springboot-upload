package com.springbootupload.util;

import java.io.File;

public class FileUtil {
    public boolean mkdirsDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }
}
