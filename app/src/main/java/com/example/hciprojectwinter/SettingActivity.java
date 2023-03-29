package com.example.hciprojectwinter;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {
    DBase db;
    String userShortName;
    Switch aSwitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aSwitch = findViewById(R.id.switchPublicAccount);
        //Read user-Inforamtion
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userShortName = prefs.getString("userShortName", "");
        db=  DBase.getInstance(this, "data.json");

        //Name of the button => switchPublicAccount
        try {
            if(db.checkIfPublicAccount(userShortName)){
                aSwitch.setChecked(true);
            }
            else {
                aSwitch.setChecked(false);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                try {
                    db.updateAccountType(userShortName,isChecked);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.setting);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                this::onNavigationItemSelected);



    }


    private boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                // Handle Home action
                intent = new Intent(this, personalPage.class);
                startActivity(intent);
                System.out.println("Home");
                finish();
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
                        Intent intent = new Intent(SettingActivity.this, trackingPage.class);
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
                //intent = new Intent(this, SettingActivity.class);
                //startActivity(intent);
                //System.out.println("Setting");
                //finish();
                return true;
        }
        return false;
    }

}
