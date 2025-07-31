package com.example.snaptask_v01.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSessionManager {

    private static final String PREF_NAME = "SnapTaskSession";
    private static final String KEY_UID = "uid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_ROLE = "activeRole";

    private static AppSessionManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private AppSessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new AppSessionManager(context);
        }
    }

    public static AppSessionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppSessionManager is not initialized. Call init(context) in Application or MainActivity.");
        }
        return instance;
    }

    // Setters
    public void setUid(String uid) {
        editor.putString(KEY_UID, uid).apply();
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email).apply();
    }

    public void setFullName(String fullName) {
        editor.putString(KEY_FULL_NAME, fullName).apply();
    }
    public void setMobileNumber(String mobileNumber) {
        editor.putString("MOBILE", mobileNumber).apply();
    }
    public void setActiveRole(String role) {
        editor.putString(KEY_ROLE, role).apply();
    }

    // Getters
    public String getUid() {
        return prefs.getString(KEY_UID, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, null);
    }

    public String getActiveRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    // Clear session on logout
    public void clearSession() {
        editor.clear().apply();
    }
}
