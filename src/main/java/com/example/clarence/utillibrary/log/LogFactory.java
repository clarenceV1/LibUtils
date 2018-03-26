package com.example.clarence.utillibrary.log;

import android.text.TextUtils;


public class LogFactory {


    private ILog iLog;

    private static class SingletonHolder {
        private static final LogFactory instance = new LogFactory();
    }

    private LogFactory() {

    }


    public static final LogFactory getInsatance() {
        return SingletonHolder.instance;
    }

    /**
     * 初始化一次就够了
     */
    public void init(LogBaseBuild build) {
        if (iLog != null) {
            return;
        }
        this.iLog = build.build();
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

    public ILog getiLog() {
        if (iLog == null) {
            throw new NullPointerException("hei ！iLog == null,u need com.cai.framework.manager.LogDock.initLog()");
        }
        return iLog;
    }
}
