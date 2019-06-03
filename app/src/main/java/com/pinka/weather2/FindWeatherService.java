package com.pinka.weather2;

import android.app.IntentService;
import android.content.Intent;

public class FindWeatherService extends IntentService {

    final static String KEY_BROADCAST_TEMPER ="KEY_BROADCAST_TEMPER";
    final static String KEY_BROADCAST_PRESS ="KEY_BROADCAST_PRESS";
    final static String KEY_BROADCAST_CLOUD ="KEY_BROADCAST_CLOUD";
    public FindWeatherService() {
        super("background_service_a2l5");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int cityName=intent.getIntExtra(DetailedFragment.KEY_CITY,0);
        String[] tempers=getResources().getStringArray(R.array.Tempers);
        String currentTemp=tempers[cityName];
        String[] presses=getResources().getStringArray(R.array.press);
        String currentPress=presses[cityName];
        String[] clouds=getResources().getStringArray(R.array.cloud);
        String currentCloud=clouds[cityName];

        Intent broadcastIntent = new Intent(DetailedFragment.BROADCAST_ACTION);
        broadcastIntent.putExtra(KEY_BROADCAST_TEMPER,currentTemp);
        broadcastIntent.putExtra(KEY_BROADCAST_PRESS,currentPress);
        broadcastIntent.putExtra(KEY_BROADCAST_CLOUD,currentCloud);
        sendBroadcast(broadcastIntent);
    }
}
