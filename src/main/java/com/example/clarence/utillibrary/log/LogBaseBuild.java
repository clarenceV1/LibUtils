package com.example.clarence.utillibrary.log;

import android.content.Context;

/**
 * Created by clarence on 2018/3/26.
 */

public abstract class LogBaseBuild {

    private Context context;

    public LogBaseBuild(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public abstract ILog build();
}
