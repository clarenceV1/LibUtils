package com.example.clarence.utillibrary;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;


    private static void toShow(Context context, CharSequence content) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private static void preShow(final Context context, final CharSequence content) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toShow(context, content);
                }
            });
        } else {
            toShow(context, content);
        }
    }

    public static void showToast(Context context, String content) {
        if (content == null || TextUtils.isEmpty(content)) {
            return;
        }
        preShow(context, content);
    }

    public static void showToast(Context context, int content) {
        if (context == null || content <= 0) {
            return;
        }
        preShow(context, context.getText(content));
    }

}
