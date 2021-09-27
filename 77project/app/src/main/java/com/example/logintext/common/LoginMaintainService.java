package com.example.logintext.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginMaintainService {

    static final String PREF_USER_EMAIL = "email";
    static final String PREF_PASSWD = "passwd";
    static final String PREF_TYPE = "type";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장 - 이메일
    public static void setEmail(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_EMAIL, userName);
        editor.commit();
    }

    // 계정 정보 저장 - 비밀번호
    public static void setPasswd(Context context, String passwd) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PASSWD, passwd);
        editor.commit();
    }

    // 계정 정보 저장 - 타입
    public static void setType(Context context, String type) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_TYPE, type);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_EMAIL, "");
    }

    public static String getPasswd(Context context) {
        return getSharedPreferences(context).getString(PREF_PASSWD, "");
    }

    public static String getType(Context context) {
        return getSharedPreferences(context).getString(PREF_TYPE, "");
    }

    // 로그아웃
    public static void clearUserName(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
