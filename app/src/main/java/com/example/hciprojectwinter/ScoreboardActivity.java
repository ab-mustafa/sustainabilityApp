package com.example.hciprojectwinter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScoreboardActivity extends AppCompatActivity {

    ListView listView;

    //ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        listView = findViewById(R.id.listView);
        ArrayList<Person> arrayList = new ArrayList<>();


        DBase db = DBase.getInstance(this,"Data.json");
        try {
            ArrayList<User> list = db.getAllUserByRanking();
            int Count=0;
            for(int i=0;i<list.size();i++){
                if (db.checkIfPublicAccount(list.get(i).shortName) == true) {
                    String userFullName = db.getUserInfo(list.get(i).shortName).getString("fullName");
                    arrayList.add(new Person(userFullName, Count + 1, list.get(i).currentScore));
                    Count+=1;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        PersonAdapter personAdapter = new PersonAdapter(this,R.layout.list_row,arrayList );
        listView.setAdapter(personAdapter);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.scoreboard);
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
                        Intent intent = new Intent(ScoreboardActivity.this, trackingPage.class);
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
                //intent = new Intent(this, ScoreboardActivity.class);
                //startActivity(intent);
                //System.out.println("Score board");
                //finish();
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