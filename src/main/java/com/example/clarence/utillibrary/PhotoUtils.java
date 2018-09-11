package com.example.clarence.utillibrary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Description:相机，相册相关工具类
 * author:lsg
 * Created on 2017-8-10
 * email:806667499@qq.com
 */
public class PhotoUtils {

    private static final String TAG = PhotoUtils.class.getSimpleName();

    private PhotoUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 打开手机摄像头拍照
     *
     * @param activity
     * @param filePath
     * @param requestCode
     * @return
     */
    public static boolean takePhoto(Activity activity, final String filePath, final int requestCode) {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                file.delete();
            }

            Uri outputFileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                outputFileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
            } else {
                outputFileUri = Uri.fromFile(file);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //将照片路径存放到指定的文件路径下
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (final ActivityNotFoundException e) {
            return false;
        }
        return true;
    }


    public static void choosePhoto(Activity activity, int requestCode) {

        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentToPickPic, requestCode);
    }


    /**
     * 图片裁剪
     *
     * @param activity
     * @param filePath
     */
    public static String startCorpImage(Activity activity, String filePath, int requestCode) {


        final String photoPath = FileUtils.getSaveFilePath(PrivateConstant.FileInfo.TYPE_PHOTO, activity) + "cutAvatar.png";

        //设置裁剪之后的图片路径文件
        File cutFile = new File(photoPath); //随便命名一个
        if (cutFile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
            cutFile.delete();
        }
        try {
            cutFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (uri != null)
            intent.setDataAndType(uri, "image/*");
        else
            return null;

        Uri outputUri = Uri.fromFile(cutFile);
        if (outputUri != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        else
            return null;
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
        return cutFile.getAbsolutePath();
    }

    /**
     * 保存图片
     *
     * @param filePath
     * @param bitmap
     * @param isDelete
     */
    public static void saveBitmap(String filePath, Bitmap bitmap, boolean isDelete) {

        File file = new File(filePath);
        // 若存在即删除-默认只保留一张
        if (isDelete) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 计算压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

    /**
     * 压缩Bitmap
     *
     * @param filePath
     * @param reqWidth  压缩宽度（默认1M=1024）
     * @param reqHeight 压缩高度（默认1M=1024）
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

}


