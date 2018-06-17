package com.example.clarence.utillibrary;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by clarence on 3/27/16.
 */
public class StringUtils {
    /**
     * 手机号加密
     * @param mobile
     * @return
     */
    public static String encryptMobile(String mobile) {
        if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
            return mobile;
        }
        return buildString(mobile.substring(0, 3), "****", mobile.substring(7, mobile.length()));
    }

    /**
     *  姓名加密
     * @param name
     * @return
     */
    public static String encryptName(String name) {
        if (TextUtils.isEmpty(name) || name.length() <2) {
            return name;
        }
        return buildString(name.substring(0, 1), "*", name.substring(2, name.length()));
    }
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String buildString(Object... str) {
        int size = str.length;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            if (str[i] != null) {
                builder.append(String.valueOf(str[i]));
            }
        }

        return builder.toString();
    }

    public static boolean isNull(String str) {
        return str == null || str.length() == 0 || str.equals("null");
    }

    public static String join(String[] array, String sep) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;
        int sepSize = 0;
        if (sep != null && !sep.equals("")) {
            sepSize = sep.length();
        }

        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].length()) + sepSize) * arraySize);
        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(sep);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String jsonJoin(String[] array) {
        int arraySize = array.length;
        int bufSize = arraySize * (array[0].length() + 3);
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(',');
            }

            buf.append('"');
            buf.append(array[i]);
            buf.append('"');
        }
        return buf.toString();
    }

    public static byte[] utf8Bytes(String data) {
        try {
            return data.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static String strip(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0, length = s.length(); i < length; i++) {
            char c = s.charAt(i);
            if (c > '\u001f' && c < '\u007f') {
                b.append(c);
            }
        }
        return b.toString();
    }
}
