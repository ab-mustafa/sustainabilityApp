package com.example.hciprojectwinter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    DBase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Log.d("lifecycle","onCreate invoked");
        db= DBase.getInstance(this,"data.json");
    }


    public void loginToDashboard(View view) throws Exception {

        TextView shortName = findViewById(R.id.user_shortname);
        TextView password = findViewById(R.id.user_password);
        String shortName_Text = shortName.getText().toString();
        String password_Text = password.getText().toString();
        boolean result = this.db.checkNameAndPassword(shortName_Text, password_Text);
        if (result){
            // Move to main page
            //@Elba need to add two lined here to move to the  next page
            System.err.println("PASS");
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.putString("userShortName", shortName_Text);
            editor.putBoolean("FirstLogin",true);
            editor.apply();
            Intent intent = new Intent(this, personalPage.class);
            startActivity(intent);
            finish();
        }
        else{
            //pop up window
            System.err.println("FAIL");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Incorrect information");
            builder.setMessage("You have entered an invalid username or password \n ");
            //builder.setIcon(R.drawable.ic_alert);
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "OK" button click event
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            shortName.setText("");
            password.setText("");

        }


    }



}
