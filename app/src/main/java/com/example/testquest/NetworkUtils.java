package com.example.testquest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;

import androidx.fragment.app.FragmentManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static final String BASE_URL = "http://start.webpower.cf/test/";
    private static final String ROUTE_REGISTER = "register/";
    private static final String ROUTE_AUTH = "auth";
    private static final String ROUTE_DATA = "data";
    private static final String NICKNAME_PARAM = "nickname";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";

    public static final String TITLE_KEY = "title";
    public static final String MESSAGE_KEY = "message";


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    public static String register(Context context, FragmentManager fragmentManager, String nickname, String email, String password) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(NICKNAME_PARAM, nickname)
                .addFormDataPart(EMAIL_PARAM, email)
                .addFormDataPart(PASSWORD_PARAM, password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + ROUTE_REGISTER)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d(TAG, "register response: " + responseStr);
                    showAlertDialog(responseStr, "Вы успешно зерегистрировались", context, fragmentManager);
                }
            }
        });

        return null;

    }

    public static String authorize(Context context, FragmentManager fragmentManager, String nickname, String password) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(NICKNAME_PARAM, nickname)
                .addFormDataPart(PASSWORD_PARAM, password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + ROUTE_AUTH)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = parseSignInResult(response.body().string());

                    Log.d(TAG, "authorize response: " + responseStr);
                    showAlertDialog(responseStr, "Вы успешно авторизировались", context, fragmentManager);
                }
            }
        });

        return null;
    }

    public static User getUserInfo(Context context, TextView name, TextView activity, TextView age, TextView email) {
        WeakReference<TextView> nameTV = new WeakReference<>(name);
        WeakReference<TextView> activityTV = new WeakReference<>(activity);
        WeakReference<TextView> ageTV = new WeakReference<>(age);
        WeakReference<TextView> emailTV = new WeakReference<>(email);

        Request request = new Request.Builder()
                .url(BASE_URL + ROUTE_DATA)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d(TAG, "user info response: " + responseStr);
                    User user = parseUserInfo(responseStr);

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            nameTV.get().setText(user.getName());
                            activityTV.get().setText(user.getActivity());
                            ageTV.get().setText(context.getResources().getQuantityString(
                                    R.plurals.age, Integer.parseInt(user.getAge()), user.getAge()));
                            emailTV.get().setText(user.getEmail());
                        }
                    });

                }
            }
        });

        return null;
    }

    private static void showAlertDialog(String responseStr, String successResponse, Context context, FragmentManager fragmentManager) {
        AlertFragment alertDialog = new AlertFragment();
        Bundle bundle = new Bundle();
        if (responseStr.equals(successResponse)) {
            bundle.putString(TITLE_KEY, context.getString(R.string.success));
            bundle.putString(MESSAGE_KEY, responseStr);
            alertDialog.setArguments(bundle);
            alertDialog.show(fragmentManager, "success");
        } else {
            bundle.putString(TITLE_KEY, context.getString(R.string.error));
            bundle.putString(MESSAGE_KEY, responseStr);
            alertDialog.setArguments(bundle);
            alertDialog.show(fragmentManager, "error");
        }
    }


    // Decodes a URL encoded string using `UTF-8`
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, Charset.forName("UTF-8").toString());
        } catch (IllegalCharsetNameException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String parseSignInResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String errorMsg = jsonObject.getString("error");
            String successMsg = jsonObject.getString("success");
            if (!errorMsg.isEmpty()) return decodeValue(errorMsg);
            if (!successMsg.isEmpty()) return decodeValue(successMsg);
        } catch (JSONException e) {
            e.printStackTrace();
            return "Something is wrong!";
        }
        return "Can't sign in!";
    }

    public static User parseUserInfo(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, User.class);
    }
}
