package com.innova.sharks.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.innova.sharks.utils.Constants.CURRENT_CHAT_ADAPTER_POSITION;
import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.sharks.utils.Constants.USERNAME;
import static com.innova.sharks.utils.Constants.USER_ID;
import static com.innova.sharks.utils.Constants.USER_TOKEN;

public class Functions {
    private static final String TAG = "Functions";

    public static void removeUserToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        prefs.edit().remove(USER_TOKEN).apply();
        prefs.edit().remove(USER_ID).apply();
    }

    public static String getUserToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getString(USER_TOKEN, "");
    }

    public static long getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getLong(USER_ID, -1L);
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME, "");
    }
}
