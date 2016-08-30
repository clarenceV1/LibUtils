
package com.example.clarence.utillibrary;

import android.util.Log;

public class LogUtils {

    public static String customTagPrefix = "";
    public static String sTAG = "Log";

    private LogUtils() {
    }

    public static void d(String tag, String content) {
        Log.d(tag, content);
    }

    public static void e(String tag, String content) {
        Log.e(tag, content);
    }


}
