package com.example.clarence.utillibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v4.content.ContextCompat;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamUtils {

    private static StreamUtils streamTool = new StreamUtils();

//    GodBaseApplication baseApp = GodBaseApplication.baseInstance();

    public static StreamUtils getInstance() {
        if (streamTool == null) {
            streamTool = new StreamUtils();
            return streamTool;
        }
        return streamTool;
    }

    /**
     * 说明：读取文件流
     *
     * @param file
     * @return
     */
    public byte[] File2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 描述：获取Assets中的图片资源.
     *
     * @return Bitmap 图片
     */
    public InputStream getInputStreamFromAssets(String fileName,Context context) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * Byte转InputStrea
     *
     * @return
     */
    public InputStream byteToInputStream(byte[] byt) {
        InputStream input = new ByteArrayInputStream(byt);
        return input;
    }

    /**
     * inputStream转Byte
     *
     * @return
     */
    public byte[] inputStreamToByte(InputStream inStream) {
        byte[] data = null;
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取文件里的byte[]
     *
     * @param file
     * @return
     */
    @SuppressWarnings("resource")
    public byte[] fileToByte(File file) {
        byte[] byt = null;
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(byt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byt;
    }


    /**
     * 字节流转字符串
     *
     * @param inputStream
     * @return
     */
    public String streamToString(InputStream inputStream) {
        String jsonString = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = 0;
        byte[] data = new byte[1024];
        try {
            while (-1 != (length = inputStream.read(data))) {
                outputStream.write(data, 0, length);
            }
            // inputStream流里面拿到数据写到ByteArrayOutputStream里面,
            // 然后通过outputStream.toByteArray转换字节数组，再通过new String()构建一个新的字符串。
            jsonString = new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.getStackTrace();
        }
        return jsonString;
    }


    public byte[] fromHex(String hexString) {
        if (hexString.length() % 2 == 1)
            hexString = "0" + hexString;
        byte[] raw = new byte[hexString.length() / 2];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        return raw;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param bm
     * @param quality 压缩质量：100质量不变
     * @return
     */
    public InputStream BitmapToInputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    public File compressBitmapToFile(Bitmap bitmap, String filePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        LogUtils.showLog(1,"length==="+baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > 600) {  //循环判断如果压缩后图片是否大于600kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
//            LogUtils.showLog(1, "options===" + options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
//        String fileEnd = "";
//        if (!StringUtils.isEmpty(filePath)) {
//            fileEnd = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
//        }

        File file = new File(Environment.getExternalStorageDirectory().getPath(), (StringUtils.isEmpty(filePath) ? System.currentTimeMillis() + ".png" : filePath.substring(filePath.lastIndexOf("/") + 1)));
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
//                LogUtils.showLog(2, "IOException=" + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
//            LogUtils.showLog(2, "FileNotFoundException=" + e.getMessage());
            e.printStackTrace();
        }
//        LogUtils.showLog(1, "getAbsolutePath===" + file.getAbsolutePath());
        recycleBitmap(bitmap);
        return file;
    }

    public void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    /**
     * 三星手机设置
     * 三星手机拍照要旋转
     *
     * @param filePath
     * @throws IOException
     */
    public File getFile(String filePath) {

        //根据图片判断要旋转多少角度
        int bitmapDegree = getBitmapDegree(filePath);
        //根据图片路径转bitmap
        Bitmap bitMBitmap = decodeFile(filePath);
        if (bitMBitmap == null) {
            return null;
        }
        //旋转后的bitmap
        Bitmap rotateBitmapByDegree = rotateBitmapByDegree(bitMBitmap, bitmapDegree);
        File saveBitmapFile = compressBitmapToFile(rotateBitmapByDegree, filePath);
        return saveBitmapFile;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 把batmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     * @throws IOException
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        // 从指定路径下读取图片，并获取其EXIF信息
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取图片的旋转信息
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }

        return degree;
    }

    /*public Bitmap decodeFile(String filePath) {
        Bitmap b = null;

        File f = new File(filePath);
        if (f == null || !f.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(f);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, o);
            o.inSampleSize = calculateInSampleSize(o, 520, 320);
            o.inJustDecodeBounds = false;
            b = BitmapFactory.decodeStream(fis, null, o);
            fis.close();

        } catch (IOException e) {
            LogUtils.showLog(2, "IOExceptionE===" + e.getMessage());
            e.printStackTrace();

        }
        return b;
    }*/

    public Bitmap decodeFile(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 1;
        Bitmap b = BitmapFactory.decodeFile(filePath, options);
        /*File f =new File(filePath);
        if(f==null || !f.exists()){
            return null;
        }
        Bitmap b = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, o);
            int scale = calculateInSampleSize(o, 520, 320);
            fis.close();

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
            LogUtils.showLog(2, "IOExceptionE===" + e.getMessage());
            e.printStackTrace();
        }*/
        return b;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
//        LogUtils.showLog(1, "inSampleSize===" + inSampleSize);
        return inSampleSize;
    }


    /**
     * 将InputStream转换成Bitmap
     *
     * @param is
     * @return
     */
    public Bitmap InputStreamToBitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    /**
     * Drawable转换成InputStream
     *
     * @param d
     * @return
     */
    public InputStream DrawableToInputStream(Drawable d) {
        Bitmap bitmap = drawableToBitmap(d);
        return BitmapToInputStream(bitmap, 100);
    }

    /**
     * InputStream转换成Drawable
     *
     * @param is
     * @return
     */
    public Drawable InputStreamToDrawable(InputStream is,Context context) {
        Bitmap bitmap = InputStreamToBitmap(is);
        return bitmapToDrawable(bitmap,context);
    }

    /**
     * Drawable转换成byte[]
     *
     * @param d
     * @return
     */
    public byte[] drawableToBytes(Drawable d, boolean isPng) {
        Bitmap bitmap = drawableToBitmap(d);
        return bitmapToBytes(bitmap, isPng);
    }

    /**
     * byte[]转换成Drawable
     *
     * @param b
     * @return
     */
    public Drawable bytesToDrawable(byte[] b,Context context) {
        Bitmap bitmap = bytesToBitmap(b);
        return bitmapToDrawable(bitmap,context);
    }

    /**
     * Bitmap转换成byte[]
     *
     * @param bmp
     * @param isPng 是否为png图片 true:是 false:不是
     * @return
     */
    public byte[] bitmapToBytes(Bitmap bmp, boolean isPng) {
//		 Config config = bmp.getConfig();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (isPng) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }


        byte[] result = baos.toByteArray();
        try {
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle, String localPath) {

        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            boolean isPng = fontIconNameSuffix(localPath);
            if (isPng) {
                localBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
            } else {
                localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            }

            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    private boolean fontIconNameSuffix(String url) {
        if (!url.substring(url.lastIndexOf("/") + 1).toUpperCase()
                .contains("PNG")) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * byte[]转换成Bitmap
     *
     * @param b
     * @return
     */
    public Bitmap bytesToBitmap(byte[] b) {

        try {
            if (b != null && b.length != 0) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inDither = false;
                opts.inPurgeable = true;
                return BitmapFactory.decodeByteArray(b, 0, b.length, opts);
            }
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换成Drawable
     *
     * @param bitmap
     * @return
     */
    public Drawable bitmapToDrawable(Bitmap bitmap, Context context) {

        return new BitmapDrawable(context.getResources(), bitmap);
    }


    /**
     * 资源文件转drawable
     *
     * @param resource
     * @return
     */
    public Drawable resourceToDrawable(int resource, Context context) {

        return ContextCompat.getDrawable(context, resource);

    }

    /**
     * 资源文件转color
     *
     * @param resource
     * @return
     */
    public int resourceToColor(int resource, Context context) {

        return ContextCompat.getColor(context, resource);

    }

}
