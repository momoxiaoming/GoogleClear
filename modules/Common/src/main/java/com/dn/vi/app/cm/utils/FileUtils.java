package com.dn.vi.app.cm.utils;

import com.dn.vi.app.cm.log.VLog;

import java.io.File;
import java.io.IOException;

/**
 * created by wensefu on 2020/9/9
 */
public class FileUtils {

    public static void deleteFile(String filepath) {
        if (filepath == null) {
            return;
        }
        File file = new File(filepath);
        if (file.isFile()) {
            try {
                file.delete();
            } catch (Exception e) {
                VLog.d("FileUtils deleteFile failed,file=" + filepath);
            }
        } else {
            try {
                Runtime.getRuntime().exec("rm -rf " + filepath);
            } catch (IOException e) {
                VLog.d("FileUtils deleteFile-dir failed,file=" + filepath);
            }
        }
    }
}
