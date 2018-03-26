package com.example.clarence.utillibrary.log;

import android.content.Context;

/**
 * Created by clarence on 2018/3/26.
 */

public class Log1Build extends LogBaseBuild {
    private boolean isDebug;

    public Log1Build(Context context) {
        super(context);
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Log1Build setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    @Override
    public ILog build() {
        return new Log1(this);
    }
}
