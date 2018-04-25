package com.example.clarence.utillibrary;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtils {
    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的Toast
    private static Context mContext;//全局唯一

    /*private控制不应该被实例化*/
    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    public static void initToast(Context context) {
        mContext = context;
    }

    private static Context getContext() {
        if (mContext == null) {
            throw new UnsupportedOperationException("ToastUtils 没有调用initToast初始化");
        }
        return mContext;
    }

    private static Toast getToast(CharSequence message, int duration) {
        if (!isShow) {
            return null;
        }
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), message, duration);
        } else {
            mToast.setGravity(0, 0, 0);
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        return mToast;
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 自定义
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        Toast mToast = getToast(message, duration);
        if (mToast != null) {
            mToast.show();
        }
    }

    /**
     * 自定义
     *
     * @param resId
     * @param duration
     */
    public static void show(int resId, int duration) {
        show(getContext().getResources().getString(resId), duration);
    }

    /**
     * 自定义Toast的位置
     *
     * @param message
     * @param duration 单位:毫秒
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showGravity(CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        Toast mToast = getToast(message, duration);
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     *
     * @param message
     * @param iconResId 图片的资源id,如:R.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
        Toast mToast = getToast(message, duration);
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(int resId) {
        showShort(getContext().getResources().getString(resId));
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        show(message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(int resId) {
        show(getContext().getResources().getString(resId), Toast.LENGTH_LONG);
    }
}
