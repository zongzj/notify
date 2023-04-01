package com.zong.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.tencent.mmkv.MMKV;

public class MMKVUtil {
    static MMKV mmkv;

    public static void init(Context activity) {
        MMKV.initialize(activity);
        mmkv = MMKV.defaultMMKV();
    }

    public static void putBool(String key, boolean v) {
        mmkv.putBoolean(key, v);

    }


    public static boolean getBool(String key) {
        return mmkv.decodeBool(key, false);
    }

    public static void putInt(String key, int v) {
        mmkv.putInt(key, v);

    }

    public static void putLong(String key, long v) {
        mmkv.putLong(key, v);
    }

    public static void putString(String key, String v) {
        mmkv.putString(key, v);
    }

    public static String getString(String key) {
        return mmkv.decodeString(key, "");
    }

    public static long getLong(String key) {
        return mmkv.decodeLong(key, 0);
    }

    public static Float getFloat(String key) {
        return mmkv.decodeFloat(key, 1f);
    }

    public static void putFloat(String key, Float v) {
        mmkv.putFloat(key, v);
    }

    public static int getInt(String key) {
        return mmkv.decodeInt(key, -1);
    }



}
