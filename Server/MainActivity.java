package com.example.mytest.weatherapplication;

// libraries used
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

// MainActivity: class to find weather using a background task based on a query
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // EditText for each way to search
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;

    // TextView to display the weather
    TextView tw;

    // used to get which edittext was entered
    String query1 = "";
    String query2 = "";
    String query3 = "";
    String query4 = "";

    boolean checkNum = false; // used to check if cities in circles due to different parse (see docs)

    int lasttype = 0; // used to get type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // edittext and textview to get and display weather
        e1 = findViewById(R.id.query1);
        e2 = findViewById(R.id.query2);
        e3 = findViewById(R.id.query3);
        e4 = findViewById(R.id.query4);
        tw = findViewById(R.id.weatherInfo);

        // loader
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void getQuery(View view)
    {
        // get edit text
        query1 = e1.getText().toString();
        query2 = e2.getText().toString();
        query3 = e3.getText().toString();
        query4 = e4.getText().toString();

        // hid keyboard after entered
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // check if 1st edit text was entered
        if(!query1.equals("")){
            boolean check = false;
            char[] cc = query1.toCharArray(); // get all characters
            // check for digit
            for(char c: cc){
                if(Character.isDigit(c)){
                    check = true;
                    break;
                }
            }
            // if digit, then invalid city
            if(check){
                Toast.makeText(getApplicationContext(),"Invalid City",Toast.LENGTH_LONG).show();
            }else{
                int commaCount = 0;
                // get number of commas
                for(char c: cc){
                    if(c == ','){
                        ++commaCount;
                    }
                }
                // search for city only
                if(commaCount == 0){
                    if(networkInfo != null && networkInfo.isConnected()){
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query",query1);
                        queryBundle.putInt("type",1);
                        getSupportLoaderManager().restartLoader(0,queryBundle,this);
                    }
                // search for city, state
                }else if(commaCount == 1){
                    if(networkInfo != null && networkInfo.isConnected()) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query", query1);
                        queryBundle.putInt("type", 2);
                        getSupportLoaderManager().restartLoader(0, queryBundle, this);
                    }
                // search for city,state,country
                }else if(commaCount == 2){
                    if(networkInfo != null && networkInfo.isConnected()) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query", query1);
                        queryBundle.putInt("type", 3);
                        getSupportLoaderManager().restartLoader(0, queryBundle, this);
                    }
                // too many parameters
                }else{
                    Toast.makeText(getApplicationContext(),"Too many parameters",Toast.LENGTH_LONG).show();
                }
            }
        //check if 2nd edit text was entered
        }else if(!query2.equals("")){
            boolean check = false;
            char[] cc = query2.toCharArray(); // get all characters
            // check if letter
            for(char c: cc){
                if(Character.isLetter(c)){
                    check = true;
                    break;
                }
            }
            // if letter present, then invalid lat/lon
            if(check){
                Toast.makeText(getApplicationContext(),"Invalid lat/lon",Toast.LENGTH_LONG).show();
            }else{
                // get number of commas
                int commaCount = 0;
                for(char c: cc){
                    if(c == ','){
                        ++commaCount;
                    }
                }
                // if no commas, then lat or lon not present
                if(commaCount == 0){
                    Toast.makeText(getApplicationContext(),"Not enough parameters",Toast.LENGTH_LONG).show();
                // search for the lat,lon
                }else if(commaCount == 1){
                    if(networkInfo != null && networkInfo.isConnected()) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query", query2);
                        queryBundle.putInt("type", 4);
                        getSupportLoaderManager().restartLoader(0, queryBundle, this);
                    }
                // too many parameters
                }else{
                    Toast.makeText(getApplicationContext(),"Too many parameters",Toast.LENGTH_LONG).show();
                }
            }
        // check if 3rd edit text was entered
        }else if(!query3.equals("")){
            boolean check = false;
            char[] cc = query3.toCharArray(); // get characters
            // get number of commas
            int commaCount = 0;
            for(char c: cc){
                if(c == ','){
                    ++commaCount;
                }
            }
            // if no commas, then only zip
            if(commaCount == 0){
                // check if letter because a zip code has no letters
                for(char c: cc){
                    if(Character.isLetter(c)){
                        check = true;
                        break;
                    }
                }
                // invalid if letter
                if(check){
                    Toast.makeText(getApplicationContext(),"Invalid ZIP Code",Toast.LENGTH_LONG).show();
                // search for zip code only
                }else{
                    if(networkInfo != null && networkInfo.isConnected()) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query", query3);
                        queryBundle.putInt("type", 5);
                        getSupportLoaderManager().restartLoader(0, queryBundle, this);
                    }
                }
            // if 1 comma then search for zip code, country
            }else if(commaCount == 1){
                if(networkInfo != null && networkInfo.isConnected()) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString("query", query3);
                    queryBundle.putInt("type", 6);
                    getSupportLoaderManager().restartLoader(0, queryBundle, this);
                }
            // too many parameters
            }else{
                Toast.makeText(getApplicationContext(),"Too many parameters",Toast.LENGTH_LONG).show();
            }
        // check if 4th edit text entered
        }else if(!query4.equals("")){
            boolean check = false;
            char[] cc = query4.toCharArray(); // get characters
            // count commas
            int commaCount = 0;
            for(char c: cc){
                if(c == ','){
                    ++commaCount;
                }
                // check if letter
                if(Character.isLetter(c)){
                    check = true;
                    break;
                }
            }
            // invaid if a letter is present
            if(check){
                Toast.makeText(getApplicationContext(),"Invalid query",Toast.LENGTH_LONG).show();
            // if 2 commas, search for lat,lon,cnt
            }else{
                if(commaCount == 2){
                    if(networkInfo != null && networkInfo.isConnected()) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("query", query4);
                        queryBundle.putInt("type", 7);
                        getSupportLoaderManager().restartLoader(0, queryBundle, this);
                    }
                // wrong number of parameters
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong number of parameters",Toast.LENGTH_LONG).show();
                }
            }
        // if all empty
        }else{
            Toast.makeText(getApplicationContext(),"Please enter a query",Toast.LENGTH_LONG).show();
        }
    }

    // onCreateLoader: method to start background task
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        // query and type
        String queryString = "";
        int type = 0;

        if (args != null) {
            // get query,type, and store type for later use
            queryString = args.getString("query");
            type = args.getInt("type");
            lasttype = type;
        }

        // call new Weather Background Task
        return new WeatherLoader(this, queryString,type);
    }

    // onLoadFinished: method when done to parse the JSON Response
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        try{
            // if > 1 iterations, clear text
            if(checkNum){
                tw.setText("");
            }
            JSONObject jsob = new JSONObject(data); // get data

            // if cities in circles, then get those specific values
            // see the docs for explanation on the numbers
            JSONObject jsob1;
            String current;
            if(lasttype == 7){
                JSONArray jsonA = (JSONArray)jsob.get("list");

                jsob1 = jsonA.getJSONObject(2);
                String temp = jsob1.getJSONObject("main").getString("temp");
                String pressure = jsob1.getJSONObject("main").getString("pressure");
                String humidity = jsob1.getJSONObject("main").getString("humidity");
                String temp_min = jsob1.getJSONObject("main").getString("temp_min");
                String temp_max = jsob1.getJSONObject("main").getString("temp_max");

                jsob1 = jsonA.getJSONObject(4);
                String speed = jsob1.getJSONObject("wind").getString("speed");
                String deg = jsob1.getJSONObject("wind").getString("deg");

                jsob1 = jsonA.getJSONObject(8);
                String cloud = jsob1.getJSONObject("clouds").getString("all");

                String main = "MAIN\n";
                String a = "Temp: " + temp + " K\n";
                String e = "Pressure: " + pressure + " hPa\n";
                String f = "Humidity: " + humidity + " %\n";
                String c = "Temp Min: " + temp_min + " K\n";
                String d = "Temp Max: " + temp_max + " K\n";

                String wind = "\n" + "WIND\n";
                String g = "Speed: " + speed + " m/s\n";
                String h = "Deg: " + deg + "\n";

                String clouds = "\n" + "CLOUDS: " + cloud +"%";

                // add ton text view
                tw.append(main);
                tw.append(a);
                tw.append(e);
                tw.append(f);
                tw.append(c);
                tw.append(d);
                tw.append(wind);
                tw.append(g);
                tw.append(h);
                tw.append(clouds);

            }else {
                // if other 3 test cases then get that information

                String temp = jsob.getJSONObject("main").getString("temp");
                String feelslike = jsob.getJSONObject("main").getString("feels_like");
                String temp_min = jsob.getJSONObject("main").getString("temp_min");
                String temp_max = jsob.getJSONObject("main").getString("temp_max");
                String pressure = jsob.getJSONObject("main").getString("pressure");
                String humidity = jsob.getJSONObject("main").getString("humidity");

                String visibility = jsob.getString("visibility");

                String speed = jsob.getJSONObject("wind").getString("speed");
                String deg = jsob.getJSONObject("wind").getString("deg");

                String cloud = jsob.getJSONObject("clouds").getString("all");

                String main = "MAIN\n";
                String a = "Temp: " + temp + " K\n";
                String b = "Feels Like: " + feelslike + " K\n";
                String c = "Temp Min: " + temp_min + " K\n";
                String d = "Temp Max: " + temp_max + " K\n";
                String e = "Pressure: " + pressure + " hPa\n";
                String f = "Humidity: " + humidity + " %\n";

                String visibility1 = "\n" + "VISIBILITY: " + visibility + " meters\n";

                String wind = "\n" + "WIND\n";
                String g = "Speed: " + speed + " m/s\n";
                String h = "Deg: " + deg + "\n";

                String clouds = "\n" + "CLOUDS: " + cloud + " %";

                // add to text view
                tw.append(main);
                tw.append(a);
                tw.append(b);
                tw.append(c);
                tw.append(d);
                tw.append(e);
                tw.append(f);
                tw.append(visibility1);
                tw.append(wind);
                tw.append(g);
                tw.append(h);
                tw.append(clouds);
            }
                checkNum = true;
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error getting weather",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // required
    }
}