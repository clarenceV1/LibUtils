package com.example.clarence.utillibrary.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by clarence on 2018/3/21.
 */

public class Log1 implements ILog {
    private boolean isDebug = false;

    protected Log1(Log1Build build) {
        this.isDebug = build.isDebug();
    }

    @Override
    public void error(String tag, String... msg) {
        if (isDebug) {
            Log.e(tag, msg[0]);
        }
    }

    @Override
    public void warn(String tag, String... msg) {
        if (isDebug) {
            Log.w(tag, msg[0]);
        }
    }

    @Override
    public void info(String tag, String... msg) {
        if (isDebug) {
            Log.i(tag, msg[0]);
        }
    }

    @Override
    public void debug(String tag, String... msg) {
        if (isDebug) {
            Log.d(tag, msg[0]);
        }
    }

    @Override
    public void verbose(String tag, String... msg) {
        if (isDebug) {
            Log.v(tag, msg[0]);
        }
    }

    @Override
    public void showLogPosition(String tag, String... msg) {
        if (!isDebug) {
            return;
        }
        if (!TextUtils.isEmpty(msg[0])) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("showLog") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
                error(tag, LogFactory.getMsg(msg[0], "\n---->at ", className, ".", methodName, "(", className, ".java:", lineNumber, ")"));
            } else {
                error(tag, msg);
            }
        }
    }
}
