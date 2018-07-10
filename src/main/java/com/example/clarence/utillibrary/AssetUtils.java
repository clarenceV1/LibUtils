package com.example.clarence.utillibrary;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Asset 帮助类
 */
public class AssetUtils {

    /**
     * 从Asset 中读出String
     *
     * @param mContext
     * @param jsonFile
     * @return
     */
    public static String getStringFromAsset(Context mContext, String jsonFile) {
        String result = "";
        try {
            //读取文件数据
            InputStream is = mContext.getResources().getAssets().open(jsonFile);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);//输出流
            result = new String(buffer, "utf-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 把Asset下的文件，保存在SD卡的Cache里面
     *
     */
   /* public static File writeSd(Context mContext, String jsonFile, String fileName) {
        try {
            InputStream inputStream1 = mContext.getAssets().open(jsonFile);
            File file = FileUtils.write2SDFromInput(CacheDisc.getCacheFile(mContext.getApplicationContext()), fileName, inputStream1);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer))>0){
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w("AssetUtil", "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }
    
}
