package com.example.clarence.utillibrary.log;

import android.text.TextUtils;


public class LogFactory implements ILog {

    private boolean isDebug = false;
    private ILog iLog;

    private static class SingletonHolder {
        private static final LogFactory instance = new LogFactory();
    }

    private LogFactory() {

    }

    /**
     * 初始化一次就够了
     */
    public void init(int type, boolean isDebug) {
        this.isDebug = isDebug;
        this.iLog = getLog(type);
    }

    private ILog getLog(int type) {
        ILog iLog = null;
        switch (type) {
            case 1:
                iLog = new Log1();
                break;
            default:

                break;
        }
        return iLog;
    }

    public static final LogFactory getInsatance() {
        return SingletonHolder.instance;
    }

    public static String getTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return "LogUtils";
        }
        return tag;
    }

    public static String getMsg(String... msgs) {
        if (msgs != null && msgs.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String msg : msgs) {
                stringBuilder.append(msg);
            }
            return stringBuilder.toString();
        }
        return "msg is null";
    }

    @Override
    public void error(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.error(getTag(tag), getMsg(msg));
        }
    }

    @Override
    public void warn(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.warn(getTag(tag), getMsg(msg));
        }
    }

    @Override
    public void info(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.info(getTag(tag), getMsg(msg));
        }
    }

    @Override
    public void debug(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.debug(getTag(tag), getMsg(msg));
        }
    }

    @Override
    public void verbose(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.verbose(getTag(tag), getMsg(msg));
        }
    }

    @Override
    public void showLogPosition(String tag, String... msg) {
        if (isDebug && iLog != null) {
            iLog.showLogPosition(getTag(tag), getMsg(msg));
        }
    }
}
