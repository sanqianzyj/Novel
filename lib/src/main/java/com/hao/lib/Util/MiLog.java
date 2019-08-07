package com.hao.lib.Util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MiLog {
    static boolean isShowLog = true;
    static boolean isSaveLog = false;


    public static void i(String tag, String msg) {
        if (isShowLog) {
            Log.i(tag, msg);
        }
        if (isSaveLog) {
            File file = new File(Environment.getExternalStorageDirectory() + "log" + DataUtils.getStringDate());
            if (file.exists()) {
                String string = FileUtils.fileToBytes(file).toString();
                string = string + "\n log.i   " + tag + "     " + msg;
                FileUtils.byteToFile(string.getBytes(), file);
            } else {
                try {
                    String string = "log.i   " + tag + "     " + msg;
                    file.createNewFile();
                    FileUtils.byteToFile(string.getBytes(), file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
