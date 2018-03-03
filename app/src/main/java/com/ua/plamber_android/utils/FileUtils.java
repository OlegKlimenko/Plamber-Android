package com.ua.plamber_android.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FileUtils {

    public static List<File> getFileInDirectory(String path, boolean showHidden, String... types) {
        List<File> listFile = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
                if (showHidden) {
                    if (checkFile(file, types)) {
                        listFile.add(file);
                    }
                } else {
                    if (!file.isHidden()) {
                        if (checkFile(file, types)) {
                            listFile.add(file);
                        }
                    }
                }
        }
        return listFile;
    }

    public static boolean isCorrectType(File file, String... types) {
        return file.isFile() && Arrays.asList(types).contains(getFileType(file));
    }

    private static boolean checkFile(File file, String... types) {
        return !file.isFile() || Arrays.asList(types).contains(getFileType(file));
    }

    public static String getFileSize(File file) {
        double fileSize = file.length() / (double)(1024 * 1024);
        return String.format(Locale.US, "%.2f", fileSize);
    }



    public static String getFileType(File file) {
        String type = "";
        int i = file.getName().lastIndexOf(".");
        if (i > 0)
            type = file.getName().substring(i + 1);
        return type.toLowerCase();
    }

    public static String removeType(String fileName) {
        String name = "";
        int i = fileName.lastIndexOf(".");
        if (i > 0)
            name = fileName.substring(0, i);
        return name;
    }
}
