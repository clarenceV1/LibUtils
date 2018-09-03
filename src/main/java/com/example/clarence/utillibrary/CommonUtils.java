package com.example.clarence.utillibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class CommonUtils {



    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class<?> c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newDrawable;
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    /**
     * Create time:2014年12月9日
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 说明：是否是整数
     * Create time:2014年12月3日
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 弧度转角度
     *
     * @param radian
     * @return
     */
    public static double radianToAngle(double radian) {
        double angle = 180 * radian / Math.PI;
        return angle;
    }

    /**
     * 角度转弧度
     *
     * @param angle
     * @return
     */
    public static double angleToRadian(double angle) {
        double radian = Math.PI * angle / 180;
        return radian;
    }


    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * @return Properties对象
     * @des 得到config.properties配置文件中的所有配置
     */
    public static Properties getConfig() {
        Properties props = new Properties();
        InputStream in = CommonUtils.class.getResourceAsStream("/config/config.eg");
        try {
            props.load(in);
        } catch (IOException e) {
//            LogUtils.i("工具包异常:获取配置文件异常");
            e.printStackTrace();
        }
        return props;
    }

    /**
     * 转换编码格式
     *
     * @param s 需要转换的字符串
     * @return 转换编码后的字符串
     */
    public static String decode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 功能描述：<根据手机的方向计算相机预览画面应该选择的角度>
     *
     * @author chengaobin create 2014/11/4 V.1.0
     */
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    /**
     * 功能描述：<转码>
     *
     * @author chengaobin create 2014/11/21 V.1.0
     */
    public static String getEncodeString(String encodeString) {
        // String encodeString = text;
        try {
            encodeString = URLEncoder.encode(encodeString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return encodeString;
    }

    /**
     * 控件点击效果
     *
     * @param normalId  点击前的效果
     * @param pressedId 点击后的效果
     * @return
     */
    public static StateListDrawable selectorState(Context context,int normalId, int pressedId) {


        Bitmap normalBitmap = BitmapFactory.decodeResource(context.getResources(), normalId, null);
        Bitmap pressedBitmap = BitmapFactory.decodeResource(context.getResources(), pressedId, null);
        Drawable dwNormal = new BitmapDrawable(context.getResources(), normalBitmap);
        Drawable dwPressed = new BitmapDrawable(context.getResources(), pressedBitmap);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, dwPressed);

        stateListDrawable.addState(new int[]{}, dwNormal);
        return stateListDrawable;
    }


    /**
     * 控件点击效果(颜色)
     *
     * @param normalId  点击前的效果
     * @param pressedId 点击后的效果
     * @return
     */
    public static StateListDrawable selectorStateColor(Context context,int normalId, int pressedId) {


        Drawable dwNormal = StreamUtils.getInstance().resourceToDrawable(normalId,context);
        Drawable dwPressed = StreamUtils.getInstance().resourceToDrawable(pressedId,context);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, dwPressed);

        stateListDrawable.addState(new int[]{}, dwNormal);
        return stateListDrawable;
    }

    /**
     * 设置 shape的背景
     *
     * @param dwNormal
     * @param dwPressed
     * @return
     */
    public static StateListDrawable selectorState(Drawable dwNormal, Drawable dwPressed) {


        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, dwPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, dwPressed);
        stateListDrawable.addState(new int[]{}, dwNormal);
        return stateListDrawable;
    }

    /**
     * @param normal
     * @param pressed
     * @return
     * @Description 设置不同状态时其文字颜色
     * @date 2015/12/15
     */
    public static ColorStateList selectorColorState(int normal, int pressed) {

        int[] colors = new int[]{pressed, pressed, pressed, pressed, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{android.R.attr.state_pressed};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 获取assets下所有文件
     *
     * @return 该目录下所有子文件
     */
   /* public static String[] getAssetsChildName(String FolderName) {
        String[] files = FileUtils.getAssetsFiles(FolderName);
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            files[i] = FolderName + File.separator + file.getName();
        }
        return files;
    }



    public static boolean isBackstage() {

        ActivityManager activityManager = (ActivityManager) BaseApplication.baseInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(BaseApplication.baseInstance().getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
//                    LogUtils.i("后台" + appProcess.processName);//当前应用程序不在最前
                    return false;
                } else {
//                    LogUtils.i("前台" + appProcess.processName);//当前应用程序显示最前
                    return true;
                }
            }
        }
        return false;
    }*/


    public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    private static final Bitmap StoreThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);

            thumb.compress(Bitmap.CompressFormat.PNG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * 将URI转为实际路径
     *
     * @param activity
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Activity activity, Uri contentUri) {


        String picturePath = ""; // 关于Android4.4的图片路径获取，如果回来的Uri的格式有两种

        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(activity, contentUri)) {
            String wholeID = DocumentsContract.getDocumentId(contentUri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    picturePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }

        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(contentUri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    picturePath = cursor.getString(column_index);
                }
                cursor.close();
            }
        }

        return picturePath;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Activity context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 代码画矩形背景
     *
     * @param roundRadiusPx 圆角大小
     * @param fillColor     填充颜色
     * @param strokeWidthPx 边框大小
     * @param strokeColor   边框颜色
     * @return
     */
    public static GradientDrawable getGradientDrawable(int strokeColor, int fillColor, int strokeWidthPx, int roundRadiusPx) {

        int strokeWidth = strokeWidthPx > 0 ? strokeWidthPx : 0; //  边框宽度
        int roundRadius = roundRadiusPx > 0 ? roundRadiusPx : 0; //  圆角半径
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    /**
     * 代码画矩形背景
     *
     * @param roundRadiusPx 圆角大小
     * @return
     */
    public static GradientDrawable getGradientDrawable(int roundRadiusPx, int[] colors) {

        int roundRadius = roundRadiusPx > 0 ? roundRadiusPx : 0; //  圆角半径
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);//创建drawable
        gd.setGradientCenter(0, 1F);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
//        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }


    /**
     * 代码画圆形背景
     *
     * @param strokeWidthPx 边框大小
     * @param strokeColor   边框颜色
     * @return
     */
    public static GradientDrawable getGradientDrawable(int strokeWidthPx, int strokeColor) {

        int strokeWidth = strokeWidthPx; //  边框宽度
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(strokeColor);
        gd.setCornerRadius(0);
        gd.setSize(strokeWidth,strokeWidth);
        gd.setStroke(0, strokeColor);
        return gd;
    }

    /**
     * 指定四个角圆角大小
     *
     * @param radii
     * @param fillColor
     * @param strokeWidthPx
     * @param strokeColor
     * @return
     */
    public static GradientDrawable getGradientDrawable(float[] radii, int fillColor, int strokeColor, int strokeWidthPx) {

        int strokeWidth = strokeWidthPx; //  边框宽度
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadii(radii);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    /**
     * 设置背景
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable){
        if(Build.VERSION.SDK_INT < 16){
            view.setBackgroundDrawable(drawable);
        }else{
            view.setBackground(drawable);
        }
    }


    private static long lastClickTime = 0;

    /**
     * @Description 判断是否连续点击
     * @date 2015/12/8
     */
    public synchronized static boolean isDoubleClick() {
        boolean isClick;
        if (lastClickTime + 1000 > System.currentTimeMillis()) {
            isClick = false;
        } else {
            isClick = true;
        }
        lastClickTime = System.currentTimeMillis();
        return isClick;
    }

    /**
     * @param activity
     * @Description 主动回到Home，后台运行
     * @date 2015/12/10
     */
    public static void goHome(Activity activity) {

        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        activity.startActivity(mHomeIntent);
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void recursionFileDelete(File file) {

        if (file.exists()) {

            if (file.isFile()) {
                file.delete();
                return;
            }

            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    recursionFileDelete(childFiles[i]);
                }
                file.delete();
            }
        }
    }


    public static ViewGroup getRootView(Activity activity) {
        return (ViewGroup) (((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    /**
     * 判断是否为6.0系统
     *
     * @return
     */
    public static boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

    }

    public static void operateKeyboard(Activity activity) {
        //关闭时打来，打开时关闭
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 获取最顶层程序包名
     *
     * @return
     */
    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    public static String getTaskPackName() {
        BaseApplication baseApp = BaseApplication.baseInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isNoOption(baseApp)) {
                if (isNoSwitch(baseApp)) {
                    UsageStatsManager usm = (UsageStatsManager) baseApp.getSystemService(Context.USAGE_STATS_SERVICE);
                    long time = System.currentTimeMillis();
                    List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                    if (appList != null && appList.size() > 0) {
                        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                        }
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {
                            return mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        }
                    }
                } else {
                    ToastUtils.showToastDialog("请打开此项避免消息接收不正常");
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    baseApp.startActivity(intent);
                }
            } else {
                return getPackName(baseApp);
            }
        } else {
            ActivityManager mActivityManager = (ActivityManager) baseApp.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo taskInfo = (tasks != null && !tasks.isEmpty() ? tasks.get(0) : null);
            return taskInfo.topActivity != null ? taskInfo.topActivity.getPackageName() : "";
//            ActivityManager am = (ActivityManager) baseApp.getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
//            return tasks.get(0).processName;
        }
        return "";
    }*/

    /**
     * 获取程序包名(本程序包名5.0版本上下都可获取)
     *
     * @return
     */
    public static String getPackName(Context baseApp) {
        RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String currentApp = "";
        try {
            field = RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityManager am = (ActivityManager) baseApp.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (RunningAppProcessInfo app : appList) {
            if (app.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Integer state = null;
                try {
                    state = field.getInt(app);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state != null && state == START_TASK_TO_FRONT) {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null) {
            currentApp = currentInfo.processName;
        }
        return currentApp;
    }

    /**
     * 判断当前设备中有没有“有权查看使用权限的应用”这个选项
     *
     * @param baseApp
     * @return
     */
    /*private static boolean isNoOption(Context baseApp) {

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = baseApp.pkgManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }*/

    /**
     * 判断调用该设备中“有权查看使用权限的应用”这个选项的APP有没有打开
     *
     * @param baseApp
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isNoSwitch(Context baseApp) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) baseApp.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 生成一个四位数，包括字母
     *
     * @return
     */
    public static String getRandomFourNum() {

        String[] beforeShuffle = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};

        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int beforeShuffleLength = beforeShuffle.length - 1;
        for (int i = 0; i < 4; i++) {
            sb.append(beforeShuffle[rand.nextInt(beforeShuffleLength)]);
        }
        String result = sb.toString();
        return result;
    }

    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {

        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null)
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static String handleJson(JSONObject jo) {
        String result = "";
        Iterator<String> keys = jo.keys();
        while (keys.hasNext()) {
            String key = keys.next();
//                Log.e("MainActivity", key);
            if ("submit_url".equals(key)) {
                continue;
            }
            result += "&" + key + "=" + jo.optString(key);
        }
        result=result.replaceFirst("&","");
        return result;
    }





}
