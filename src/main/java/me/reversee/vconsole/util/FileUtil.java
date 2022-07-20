package me.reversee.vconsole.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static boolean writeToFile(String str, String path, boolean overwrite) {
        try {
            File file = new File(path);
            FileWriter writer;
            if (file.createNewFile()) {
                BufferedWriter outStream= new BufferedWriter(new FileWriter(file, !overwrite));
                outStream.write(str + System.lineSeparator());
                outStream.flush();
                outStream.close();
                Logger.log("File written: " + file.getPath());
            }
        } catch (IOException e) {
            Logger.log("An error occurred!");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
