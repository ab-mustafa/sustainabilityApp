package com.example.hciprojectwinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class personalPage extends Activity {
    TextView UserFullName,CurrentRank, CurrentScore ,NextTargetScore ;
    ProgressBar progressBar;
    DBase db;
    String userShortName;
    ArrayList<Calendar> disabledDates;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        Log.d("lifecycle","onCreate invoked");
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(50);
        UserFullName = findViewById(R.id.UserFullName);
        CurrentRank = findViewById(R.id.CurrentRank);
        CurrentScore = findViewById(R.id.CurrentScore);
        NextTargetScore = findViewById(R.id.NextTargetScore);
        progressBar = findViewById(R.id.progressBar);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userShortName = prefs.getString("userShortName", "");
        db=  DBase.getInstance(this, "data.json");
        try {
            JSONObject obj = db.getUserInfo(userShortName);
            UserFullName.setText(obj.getString("fullName").toString());
            CurrentScore.setText(Integer.toString(obj.getInt("currentScore")));
            CurrentRank.setText(Integer.toString(db.getUserRanking(userShortName)));
            int nextTargetRanking = db.getUserNextTarget(userShortName);
            if(nextTargetRanking ==-1){
                progressBar.setProgress(100);
                NextTargetScore.setText("You are\nthe top");
            }
            else {
                int Ratio = (100*obj.getInt("currentScore"))/nextTargetRanking;
                progressBar.setProgress(Ratio);
                NextTargetScore.setText(("Reach\n"+Integer.toString(nextTargetRanking)).toString());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Button shareButton = findViewById(R.id.shareDataButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TextToShare = "Just made it to the top "+CurrentRank.getText().toString()+" in CarbonCutters! So excited to be part of a community that cares about sustainability and making a positive impact on the environment. 🌍🌿 #sustainability #greentech #climateaction #leadersinchange";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, TextToShare);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        try {
            disabledDates = this.db.getSelectedDates(userShortName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                this::onNavigationItemSelected);

    }


    private boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                // Handle Home action
                /*
                intent = new Intent(this, personalPage.class);
                startActivity(intent);
                System.out.println("Home");
                finish();
                */
                return true;


            case R.id.calender:
                // Get the current date and time in milliseconds
                // Calculate the date and time 7 days ago in milliseconds

                // Get Current Date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                String text = dayOfMonth +"/"+ month +"/"+ year;
                System.out.println("Note: "+text);
                editor.putString("SelectedDate", text);
                editor.apply();

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this,
                        (view, year1, month1, dayOfMonth1) -> {
                            // Handle the date selected by the user
                            System.out.println("You select this date" + Integer.toString(year1) + "/" + Integer.toString(month1) + "/" + Integer.toString(dayOfMonth1));
                            SharedPreferences.Editor editor2 = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                            String text2 = dayOfMonth1 +"/"+ month1 +"/"+ year1;
                            System.out.println("Note: "+text2);
                            editor2.putString("SelectedDate", text2);
                            editor2.apply();
                        },
                        year, month, dayOfMonth
                );

                // Disable dates before 7 days ago
                calendar.add(Calendar.DAY_OF_MONTH, -7); // Subtract 7 days from current date
                Date result = calendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(result.getTime());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


                // Set the cancel button listener
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel button click
                        System.out.println("Cancel Button Pressed");
                        dialog.dismiss();
                    }
                });

                // Set the proceed button listener
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle proceed button click
                        System.out.println("Next Button Pressed");
                        Intent intent = new Intent(personalPage.this, trackingPage.class);
                        startActivity(intent);
                        dialog.dismiss();
                        System.out.println("Tips");
                        //finish();
                    }
                });

                Window window = datePickerDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(android.R.style.Animation_Dialog);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                datePickerDialog.show();
                System.out.println("Calender");
                return true;
            case R.id.scoreboard:
                // Handle Notifications action
                intent = new Intent(this, ScoreboardActivity.class);
                startActivity(intent);
                System.out.println("Score board");
                finish();
                return true;
            case R.id.tips:
                // Handle Notifications action
                intent = new Intent(this, tipsPage.class);
                startActivity(intent);
                System.out.println("Tips");
                finish();
                return true;
            case R.id.setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                System.out.println("Setting");
                finish();
                return true;
        }
        return false;
    }
}
