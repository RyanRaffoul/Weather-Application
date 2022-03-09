package com.example.mytest.weatherapplication;

// libraries used
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

// WeatherLoader: async loader to load and get the weather
public class WeatherLoader extends AsyncTaskLoader<String>
{
    // query and type
    String query;
    int type;

    // set query and type
    WeatherLoader(Context context, String query, int type)
    {
        super(context);
        this.query = query;
        this.type = type;
    }

    // force load
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    // call network utilis to get weather information and return this to main
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getWeatherInfo(query,type);
    }
}
