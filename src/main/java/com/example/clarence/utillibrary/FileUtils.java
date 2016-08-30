package com.example.clarence.utillibrary;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by clarence on 3/27/16.
 */
public class FileUtils {
    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        File f = new File(path);
        return f.delete();
    }
    /**
     * 文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
    /**
     * 创建目录
     *
     * @param dirName 如/sdcard/xiangyu/
     * @return
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 创建文件
     *
     * @param fileName 如/sdcard/xiangyu/temp.apk
     * @return
     */
    public static File creatSDFile(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            return file;
        } catch (Exception e) {
            LogUtils.e("FileUtils.creatSDFile()", e.getLocalizedMessage());
        }
        return null;

    }

    /**
     * 写文件
     *
     * @param dirName
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeStringToFile(String dirName, String fileName, String content) {
        try {
            creatSDDir(dirName);
            File file = creatSDFile(dirName + fileName);
            FileOutputStream output = new FileOutputStream(file);
            output.write(content.getBytes());
            output.flush();
            output.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
