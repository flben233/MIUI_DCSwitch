package org.shirakawatyu.dc.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtil {
    public static void putBoolean(Context context, String name, String key, boolean val) {
        SharedPreferences dcPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor edit = dcPreferences.edit();
        edit.putBoolean(key, val);
        edit.apply();
    }
}
