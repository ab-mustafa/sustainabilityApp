package com.example.hciprojectwinter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class trackingPage extends Activity {
    ArrayList<CheckBox> checkBoxes;
    LinearLayout linearLayout;
    int currentScore;
    String userShortName;
    int day;
    int month;
    int year;

    private void addCheckBoxesToList(){
        String[] metrics = {"Took a short shower",
                "Used public transportation",
                "Used drying rack",
                "Turned off lights",
                "Set energy saving function in your devices",
                "Used a reusable bag",
                "Used a paper straw"
                };
        for (int i=0;i<metrics.length;i++){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(metrics[i]);
            checkBox.setChecked(false);
            checkBoxes.add(checkBox);
            linearLayout.addView(checkBox);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When you enter this page , I need the username , data to submit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackdaily);
        Log.d("lifecycle","onCreate invoked");
        checkBoxes = new ArrayList<CheckBox>();
        linearLayout = findViewById(R.id.linearLayout);
        this.addCheckBoxesToList();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userShortName = prefs.getString("userShortName", "");
        day = Integer.parseInt(prefs.getString("SelectedDate", "").toString().split("/")[0]);
        month = Integer.parseInt(prefs.getString("SelectedDate", "").toString().split("/")[1]);
        year = Integer.parseInt(prefs.getString("SelectedDate", "").toString().split("/")[2]);




    }
    public void moveToScorePage(View view) {
        Intent intent = new Intent(this, personalPage.class);
        startActivity(intent);
        finish();
    }

    public void submitAction(View view) throws JSONException {
        //Note:for protoType we will not support editing the submission for previous page
        //get date [I need the date @abdelrahman]
        String Date = "20/02/2023";
        //store what user select
        ArrayList<String> userSelection = new ArrayList<String>();
        for(int i =0; i<checkBoxes.size();i++){
            if (checkBoxes.get(i).isChecked()){
                userSelection.add(checkBoxes.get(i).getText().toString());
            }
        }


        DBase dbRef = DBase.getInstance(this,"Data.json");

        int TodayScore = 10 * userSelection.size();
        int userScore = TodayScore + dbRef.getUserInfo(userShortName).getInt("currentScore");
        dbRef.updateRank(userShortName, userScore);
        //Go to score page
        Intent intent = new Intent(this, personalPage.class);
        startActivity(intent);
        finish();

    }

    }
