package com.example.projectbootcamp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    public static final String SP_USER_APP = "spUserApp";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_IMAGE = "spImage";
    public static final String SP_STATUS_LOGIN = "spSudahLogin";

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_USER_APP, Context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama() {
        return sharedPreferences.getString(SP_NAMA, "");
    }

    public String getSPEmail() {
        return sharedPreferences.getString(SP_EMAIL, "");
    }

    public String getSPImage() {
        return sharedPreferences.getString(SP_IMAGE, "");
    }

    public Boolean getSPStatusLogin() {
        return sharedPreferences.getBoolean(SP_STATUS_LOGIN, false);
    }

    public void removeData(){
        spEditor.clear();
        spEditor.commit();
    }
}
