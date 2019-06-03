package com.pinka.weather2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FindWeatherService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int cityName=intent.getIntExtra(DetailedFragment.KEY_CITY,0);
        String[] tempers=getResources().getStringArray(R.array.Tempers);
        MainActivity.weatherData[0]= tempers[cityName];
        String[] presses=getResources().getStringArray(R.array.press);
        MainActivity.weatherData[1]= presses[cityName];
        String[] clouds=getResources().getStringArray(R.array.cloud);
        MainActivity.weatherData[2]= clouds[cityName];

        MainActivity.isBackgroundServiceEnd=true;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
