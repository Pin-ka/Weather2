package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

public class FindWeatherService extends IntentService {

    final static String KEY_BROADCAST_TEMPER ="KEY_BROADCAST_TEMPER";
    final static String KEY_BROADCAST_PRESS ="KEY_BROADCAST_PRESS";
    final static String KEY_BROADCAST_CLOUD ="KEY_BROADCAST_CLOUD";

    private final Handler handler = new Handler();

    String currentTemp,currentPress,currentCloud;

    public FindWeatherService() {
        super("background_service_a2l5");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int cityName=intent.getIntExtra(DetailedFragment.KEY_CITY,0);

        String[] cityNamesEng = getResources().getStringArray(R.array.Cities_for_OWM);
        String currentName=cityNamesEng[cityName];
        final JSONObject jsonObject = WeatherDataLoader.getJSONData(currentName);
        if(jsonObject == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                }
            });
        } else {
                    renderWeather(jsonObject);
        }

        Intent broadcastIntent = new Intent(DetailedFragment.BROADCAST_ACTION);
        broadcastIntent.putExtra(KEY_BROADCAST_TEMPER,currentTemp);
        broadcastIntent.putExtra(KEY_BROADCAST_PRESS,currentPress);
        broadcastIntent.putExtra(KEY_BROADCAST_CLOUD,currentCloud);
        sendBroadcast(broadcastIntent);
    }

    @SuppressLint("DefaultLocale")
    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject clouds = jsonObject.getJSONObject("clouds");
            currentTemp = String.format("%.0f", main.getDouble("temp"));
            double pressFromHPaToMms= Integer.parseInt(main.getString("pressure"))*0.750062;
            currentPress=String.format("%.0f", pressFromHPaToMms);
            currentCloud= String.valueOf(clouds.getInt("all"));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
