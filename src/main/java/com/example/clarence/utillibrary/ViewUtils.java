package com.example.clarence.utillibrary;

import android.view.View;

public class ViewUtils {
    /**
     * 获取布局中的View
     *
     * @param viewId view的Id
     * @param <T>    View的类型
     * @return view
     */
    public static <T extends View> T getViewById(View rootView, int viewId) {
        return (T) rootView.findViewById(viewId);
    }
}
