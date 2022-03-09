package com.example.mytest.weatherapplication;

// libraries used
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

// NetworkUtils: class to open api and get information
public class NetworkUtils
{
    private static final String id = "&APPID=018f237d06c32c1b1802d0af4cd14f98"; // unique id

    // getWeatherInfo: method to open api and get information
    public static String getWeatherInfo(String query, int type)
    {
        // used for connection, read, path, and get weather
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String weather = null;
        String path = null;

        // open api and get information based on types
        // types are described below
        // Type 1: city
        // Type 2: city,state
        // Type 3: city,state,country
        // Type 4: lat,lon
        // Type 5: zip
        // Type 6: zip,country
        // Type 7: lat,lon,cnt
        try{
            if(type == 1){
                path = "https://api.openweathermap.org/data/2.5/weather?q=" +query +id;
            }else if(type == 2){
                String[] full = query.split(",");
                String city = full[0];
                String state = full[1];
                path = "https://api.openweathermap.org/data/2.5/weather?q=" +city +"," +state +id;
            }else if(type == 3){
                String[] full = query.split(",");
                String city = full[0];
                String state = full[1];
                String country = full[2];
                path = "https://api.openweathermap.org/data/2.5/weather?q=" +city +"," +state +"," +country +id;
            }else if(type == 4){
                String[] full = query.split(",");
                String lat = full[0];
                String lon = full[1];
                path = "https://api.openweathermap.org/data/2.5/weather?lat=" +lat +"&lon=" +lon +id;
            }else if(type == 5){
                path = "https://api.openweathermap.org/data/2.5/weather?zip=" +query +id;
            }else if(type == 6){
                String[] full = query.split(",");
                String zip = full[0];
                String country = full[1];
                path = "https://api.openweathermap.org/data/2.5/weather?zip=" +zip +"," +country +id;
            }else{
                String[] full = query.split(",");
                String lat = full[0];
                String lon = full[1];
                String cnt = full[2];
                path = "https://api.openweathermap.org/data/2.5/find?lat=" +lat +"&lon=" +lon +"&cnt=" +cnt +id;
            }
            URL requestURL = new URL(path); // get url

            // open connection
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read
            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            // get json response
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                builder.append("\n");
            }

            // if error
            if (builder.length() == 0) {
                return null;
            }
            weather = builder.toString(); // get weather

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return weather; // return the weather
    }
}
