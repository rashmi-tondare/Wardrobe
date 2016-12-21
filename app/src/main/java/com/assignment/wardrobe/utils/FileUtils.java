package com.assignment.wardrobe.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rashmi on 20/12/16.
 */

public class FileUtils {

    private static String BASE_FOLDER;
    private static FileUtils fileUtils;

    private static Context mContext;

    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String TOP_IMAGES_FOLDER = "tops";
    private static final String BOTTOM_IMAGES_FOLDER = "bottoms";

    private FileUtils(Context mContext, String folderName) {
        this.mContext = mContext;
        BASE_FOLDER = folderName;
        createFolderStructure();
    }

    public static void init(Context context, String folderName) {
        fileUtils = new FileUtils(context, folderName);
    }

    public static FileUtils getInstance(Context context) {
        if (fileUtils == null) {
            throw new RuntimeException("FileUtils has not been initialized. Call init() first.");
        }
        return fileUtils;
    }

    public static File getBaseFileStorage() {
        String baseFolderPath = Environment.getExternalStorageDirectory() + "/" + BASE_FOLDER;
        File baseFolder = new File(baseFolderPath);
        return baseFolder;
    }

    public void createFolderStructure() {
        String baseFolderPath = Environment.getExternalStorageDirectory() + "/" + BASE_FOLDER;
        File baseFolder = new File(baseFolderPath);

        if (!baseFolder.exists()) {
            baseFolder.mkdir();
        }

        Log.d(TAG, "Base Folder name = " + baseFolder.getName());
        File topsFolder = new File(baseFolder.getPath() + "/" + TOP_IMAGES_FOLDER);
        if (!topsFolder.exists()) {
            topsFolder.mkdirs();
        }

        File bottomFolder = new File(baseFolder.getPath() + "/" + BOTTOM_IMAGES_FOLDER);
        if (!bottomFolder.exists()) {
            bottomFolder.mkdirs();
        }
    }

    /**
     * @return return with a separator e.g., abc/xyz/
     */
    public static String getTopImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + "/" + BASE_FOLDER + "/" + TOP_IMAGES_FOLDER + "/";
        return path;
    }

    public static String getBottomImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + "/" + BASE_FOLDER + "/" + BOTTOM_IMAGES_FOLDER + "/";
        return path;
    }

    public static File createImageFile(int type) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(type == AppConstants.CLOTHING_TYPE_TOP ? getTopImageStorageLocation() : getBottomImageStorageLocation());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void deleteFile(String path) {

        File destFile = new File(path);
        if (destFile.exists()) {
            destFile.delete();
        }
    }

    public void deleteBaseFolder() {
        File file = getBaseFileStorage();
        new DirectoryCleaner(file).clean();
        file.delete();
    }

    public class DirectoryCleaner {
        private final File mFile;

        public DirectoryCleaner(File file) {
            mFile = file;
        }

        public void clean() {
            if (null == mFile || !mFile.exists() || !mFile.isDirectory())
                return;
            for (File file : mFile.listFiles()) {
                delete(file);
            }
        }

        private void delete(File file) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    delete(child);
                }
            }
            file.delete();

        }
    }
}