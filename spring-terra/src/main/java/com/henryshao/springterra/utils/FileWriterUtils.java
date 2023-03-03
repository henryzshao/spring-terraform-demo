package com.henryshao.springterra.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterUtils {

    public static boolean writeToFile(String fileName, String output) {
        try {
            File configFile = new File("./config", fileName);
            File fileDir = new File(configFile.getParent());
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(output + "\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}