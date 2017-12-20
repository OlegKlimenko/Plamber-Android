package com.ua.plamber_android.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FileUtils {

    public static List<File> getPdfFileInDirectory(String path, boolean showHidden) {
        List<File> listFile = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (showHidden) {
                if (checkFile(file)) {
                    listFile.add(file);
                }
            } else {
                if (!file.isHidden()) {
                    if (checkFile(file)) {
                        listFile.add(file);
                    }
                }
            }
        }
        return listFile;
    }

    private static boolean checkFile(File file) {
        return !file.isFile() || getFileType(file).equals("pdf");
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
        return type;
    }

    public static String removeSymbol(String str, int number) {
        return str.substring(0, str.length() - number);
    }

    public static class FileCompare implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
        }
    }
}
