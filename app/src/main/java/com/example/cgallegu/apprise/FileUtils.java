package com.example.cgallegu.apprise;

/**
 * Created by matth on 29/10/2017.
 */
import android.content.Context;

import java.io.File;

public class FileUtils {

    public static File getDataDir(Context context) {

        String path = context.getFilesDir() + "/SampleZip";

        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static File getDataDir(Context context, String folder) {

        //String path = context.getFilesDir().getAbsolutePath() + "/" + folder;

        String path = context.getFilesDir() + "/" + folder;
        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}