package com.example.clarence.utillibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by WenHui on 2015/6/26.
 * 获取设备相关信息
 */
public class DeviceUtils {


    private static WebView webview;
    private static String strUA = "";

    public static String getAndroidId(Context context) {
        try {
            String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            return ANDROID_ID;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取MAC地址
     *
     * @param context
     */

    public static String getMac(Context context) {
        return getPhoneMacAddress(context);
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = "";
        try {
            id = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取手机型号
     *
     * @param context
     * @return
     */
    public static String getPhoneModel(Context context) {
        return android.os.Build.MODEL;
    }

    public static String getProvidersIMSI(Context mContext) {
        TelephonyManager mTelephonyManager = ((TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE));
        String IMSI = "";
        try {
            IMSI = mTelephonyManager.getSubscriberId();
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return IMSI;
    }


    /**
     * 获取手机mac地址
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getPhoneMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String address = info.getMacAddress();
            if (!TextUtils.isEmpty(address) && !address.contains("000000000000")) {
                return address.toLowerCase();
            } else {
                String deviceId = getImei(context);
                if (!TextUtils.isEmpty(deviceId) && !deviceId.contains("000000000000")) {
                    return deviceId.toLowerCase();
                }
                return getUUID(context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getUUID(context);
    }


    /**
     * 获取设备ID
     *
     * @param contxet
     * @return
     */
    public static String getDeviceId(Context contxet) {
        try {
            String ret = getMac(contxet);
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";


    }

    public static String getUUID(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String identity = preference.getString("identity_android", null);
        if (identity == null) {
            //UUID uuid = new  java.util.UUID(64,64);
            //uuid.randomUUID()
            identity = UUID.randomUUID().toString();
            //identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("identity_android", identity).commit();
        }
        return identity;

    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 800;

    }

    public static float getDeviceDensity(Context context) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return scale;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;

    }

    public static int getDeviceDensityValue(Context context) {
        //   Display display = context.getWindowManager().getDefaultDisplay();
        //   return display.getHeight();
        try {
            int scale = context.getResources().getDisplayMetrics().densityDpi;
            return scale;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;

    }

    /**
     * dip到px的转换
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getMarginTopWithoutWave(Context context) {
        return dip2px(context, 20);
    }


    /**
     * px到dip的转换
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示键盘
     *
     * @param activity
     * @param view
     */
    public static void showkeyboard(Activity activity, View view) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();//
        }
    }


    //是否显示了键盘
    public static boolean isShowkeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.isActive();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }


    public static String getMNC(Context context) {
        try {
            String imsi = DeviceUtils.getProvidersIMSI(context);
            if (!TextUtils.isEmpty(imsi) && imsi.length() > 3) {
                return imsi.substring(0, 3);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getMCC(Context context) {
        try {
            String imsi = DeviceUtils.getProvidersIMSI(context);
            if (!TextUtils.isEmpty(imsi) && imsi.length() > 3) {
                return imsi.substring(0, 3);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static int getPhoneType(Context context) {
        try {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int type = telephony.getPhoneType();
            return type;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;

    }

    /**
     * 获取手机唯一码 mac_deviceid_meetyou
     *
     * @param context
     * @return
     */
    public static String getPhoneOnlyKey(Context context) {
        try {
            String macAddress = "";
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            macAddress = info.getMacAddress();
            if (macAddress == null)
                macAddress = "";

            String deviceId = getImei(context);
            if (deviceId == null)
                deviceId = "";
            String result = macAddress + "_" + deviceId + "_meetyou";
            if (result.length() > 32) {
                result = result.substring(0, 31);
                // return result.toLowerCase();
            } else {
                // return result.toLowerCase();
            }
            return result.toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getTimeZoneName() {
        try {
            TimeZone tz = TimeZone.getDefault();
            // String s = "TimeZone   "+tz.getDisplayName(false,
            // TimeZone.SHORT)+" Timezon id :: " +tz.getID();
            // LogUtils.dln(s);

            return tz.getDisplayName(false, TimeZone.SHORT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static boolean isInstall(Context context, String packagename) {
        boolean isInstall = false;
        try {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(
                        packagename, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                e.printStackTrace();
            }
            if (packageInfo == null) {
                isInstall = false;
            } else {
                isInstall = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstall;
    }

    public static int getMarginTopWithWave(Context context) {
        return DeviceUtils.dip2px(context, 4);
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        try {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            return statusBarHeight;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 20;
    }
}
