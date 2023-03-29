package com.example.hciprojectwinter;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


class User{
    public String shortName;
    public int currentScore;

    public User(String shortName,int currentScore){
        this.currentScore = currentScore;
        this.shortName = shortName;
    }
    public int getCurrentScore(){
        return  this.currentScore;
    }
}

public class DBase {
    private static DBase instance = null;
    public JSONObject jsonObject;
    private DBase(Context context,String fileName) {
        try {
            jsonObject = this.readDB(context, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public static DBase getInstance(Context context, String fileName) {
        if(instance == null) {
                instance = new DBase(context,fileName);
        }
        return instance;
    }


    public boolean checkNameAndPassword(String userName, String userPassword) throws JSONException {
        // Get the JSONArray from the JSONObject
        JSONArray usersArray = jsonObject.getJSONArray("users");
        System.err.println(usersArray);
        // Iterate over the elements in the JSONArray
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("shortName").toString().equals(userName) && user.getString("password").toString().equals(userPassword))
            {
                return true;
            }
        }
        return false;
    }

    private JSONObject readDB(Context context,String fileName) throws IOException, JSONException {

        try {


            InputStream inputStream = context.getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(jsonObject);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        /*FileInputStream fis = new FileInputStream(new File(Environment.getExternalStorageDirectory()+"/data.json"));
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        fis.close();
        return new JSONObject(sb.toString());
   */

    }

    public JSONObject getUserInfo(String userName) throws JSONException{
        JSONArray usersArray = jsonObject.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("shortName").toString().equals(userName)) {return user;}
        }
        return null;
    }

    public int getUserRanking(String userName) throws JSONException {

        Comparator<User> scoreComparator = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getCurrentScore(), u1.getCurrentScore());
            }
        };

        ArrayList<User> list = new ArrayList<User>();
        JSONArray usersArray = jsonObject.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            User user1 = new User(user.getString("shortName").toString(),user.getInt("currentScore"));
            list.add(user1);
        }
        Collections.sort(list, scoreComparator);
        for (int i=0;i<list.size();i++){
            if (list.get(i).shortName.equals(userName)){
                return i+1;
            }
        }
        return -1;
    }
    public int getUserNextTarget(String userName) throws JSONException {

        Comparator<User> scoreComparator = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getCurrentScore(), u1.getCurrentScore());
            }
        };

        ArrayList<User> list = new ArrayList<User>();
        JSONArray usersArray = jsonObject.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            User user1 = new User(user.getString("shortName").toString(),user.getInt("currentScore"));
            list.add(user1);
        }
        Collections.sort(list, scoreComparator);
        for (int i=0;i<list.size();i++){
            if (list.get(i).shortName.equals(userName)){
                if (i==0){return -1;}
                else {
                    return this.getUserInfo(list.get(i-1).shortName).getInt("currentScore");
                }
            }
        }
        return -1;
    }

    public ArrayList<Calendar> getSelectedDates(String shortName) throws JSONException {
        ArrayList<Calendar> list =new ArrayList<Calendar>();
        JSONArray selectedDats = getUserInfo(shortName).getJSONArray("submittedDates");
        for (int i = 0; i < selectedDats.length(); i++) {
            String date = selectedDats.getString(i);
            int day = Integer.parseInt(date.split("/")[0]);
            int month = Integer.parseInt(date.split("/")[1]);
            int year = Integer.parseInt(date.split("/")[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month, day);
            list.add(calendar);
        }
        return list;

    }


    public int getUserIndex(String userName) throws JSONException {
        JSONArray usersArray = jsonObject.getJSONArray("users");
        // Iterate over the elements in the JSONArray
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("shortName").toString().equals(userName))
            {
                return i;
            }
        }
        return -1;
    }

    public void updateRank(String userName,int newRank) throws JSONException {
        int userIndex = getUserIndex(userName);
        JSONObject person =  jsonObject.getJSONArray("users").getJSONObject(userIndex);
        person.put("currentScore", newRank);
        return;
    }

    public void updateAccountType(String userName,boolean publicAccount) throws JSONException {
        int userIndex = getUserIndex(userName);
        JSONObject person =  jsonObject.getJSONArray("users").getJSONObject(userIndex);
        person.put("PublicAccount", publicAccount);
        return;
    }

    public ArrayList<User> getAllUserByRanking() throws JSONException {
        Comparator<User> scoreComparator = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getCurrentScore(), u1.getCurrentScore());
            }
        };

        ArrayList<User> list = new ArrayList<User>();
        JSONArray usersArray = jsonObject.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            User user1 = new User(user.getString("shortName").toString(),user.getInt("currentScore"));
            list.add(user1);
        }
        Collections.sort(list, scoreComparator);
        return  list;

    }


    public boolean checkIfPublicAccount(String userName) throws JSONException {
        boolean result = getUserInfo(userName).getBoolean("PublicAccount");
        return result;
    }


}
