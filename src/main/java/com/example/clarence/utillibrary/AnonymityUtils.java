package com.example.clarence.utillibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by clarence on 3/27/16.
 */
public class AnonymityUtils {
    /**
     * @param context
     * @param name    Manifest 名字
     * @param key     key
     * @return
     */
    public static String getManifestValue(Context context, String name, String key) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get(name);
            return a.getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
