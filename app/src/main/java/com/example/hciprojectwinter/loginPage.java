package com.example.hciprojectwinter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Log.d("lifecycle","onCreate invoked");
    }
    public void loginToDashboard(View view) throws Exception {

        TextView shortName = findViewById(R.id.user_shortname);
        TextView password = findViewById(R.id.user_password);
        String shortName_Text = shortName.getText().toString();
        String password_Text = password.getText().toString();
        String ipAddress = "192.168.0.16";
        String port = "5000";
        //Send request
        // Set the URL for the request
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"username\":\""+  shortName_Text+"\",\"password\":\""+password_Text+"\"}";
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("http://"+ipAddress+":"+port+"/login")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    System.out.println("OK");
                }
                else {
                    System.out.println("Try Again");
                }
            }
        });

    }



}
