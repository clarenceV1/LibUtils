package com.example.clarence.utillibrary.encrypt;

import android.content.Context;

/**
 * Created by clarence on 3/27/16.
 */
public class EncryptManager {

    private EncryptManager() {

    }

    public static class Holder {
        static EncryptManager instance = new EncryptManager();
    }

    public static EncryptManager getInstance() {

        return Holder.instance;
    }

    public void init(Context context) {

    }
}
