package com.example.clarence.utillibrary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtils {

    public static void copyToClipBoard(Context context, String key, String value) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(key, value);
        clipboardManager.setPrimaryClip(clipData);
    }
}
