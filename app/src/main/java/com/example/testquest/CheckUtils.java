package com.example.testquest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckUtils {

    public static boolean checkNickname(String nickname){
        return nickname.length() >= 3;
    }

    public static boolean checkEmail (CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean checkAll(String nickname, String email, String password) {
        return checkNickname(nickname)
                && checkEmail(email)
                && checkPassword(password);
    }

    public static boolean checkAll(String nickname, String password) {
        return checkNickname(nickname)
                && checkPassword(password);
    }

    public static boolean checkPassword(String password) {
        return password.length() >= 6;
    }

    public static String getErrorMsg(Context context, String nickname, String email, String password) {
        if (!CheckUtils.checkNickname(nickname)) {
            if (nickname.isEmpty())
                return context.getString(R.string.error_nickname_empty);
            if (nickname.length() < 3)
                return context.getString(R.string.error_nickname_short);
        }

        if (!CheckUtils.checkEmail(email)) return context.getString(R.string.error_email_not_valid);
        if (password.length() < 6) return context.getString(R.string.error_password_short);

        return null;
    }

    public static String getErrorMsg(Context context, String nickname, String password) {
        if (!CheckUtils.checkNickname(nickname)) {
            if (nickname.isEmpty())
                return context.getString(R.string.error_nickname_empty);
            if (nickname.length() < 3)
                return context.getString(R.string.error_nickname_short);
        }

        if (password.length() < 6) return context.getString(R.string.error_password_short);

        return null;
    }
}
