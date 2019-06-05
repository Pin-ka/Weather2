package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class DetailedFragment extends Fragment {
    private TextView temperText, temperName, pressText, cloudText, cityName;
    private ImageView image;
    private LinearLayout pressLayout,cloudLayout;
    private int index=CitiesListFragment.currentPosition;

    public static final String KEY="currentCity";
    public static final String KEY_CITY="KEY_CITY";
    final static String BROADCAST_ACTION = "com.pinka.weather2";

    private ServiceFinishedReceiver receiver = new ServiceFinishedReceiver();

    public static DetailedFragment create(int index) {
        DetailedFragment f = new DetailedFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getIndex() {
        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            index=Objects.requireNonNull(getArguments()).getInt("index", 0);
        }
        return index;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        index=getArguments().getInt("currentCity",0);

        return inflater.inflate(R.layout.fragment_detailed,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences indexCityPref = Objects.requireNonNull(getActivity()).
                getSharedPreferences(CitiesListFragment.indexCityKey, Context.MODE_PRIVATE);
        readFromPreference(indexCityPref);
        initViews(view);
        setViews();
    }

    private void readFromPreference(SharedPreferences preferences) {
        CitiesListFragment.currentPosition = preferences.getInt(CitiesListFragment.indexCityKey, 0);
    }

    private void initViews(View view) {
        temperText=view.findViewById(R.id.temperTextView);
        cityName=view.findViewById(R.id.cityName);
        image=view.findViewById(R.id.weatherImage);
        pressText=view.findViewById(R.id.pressTextView);
        cloudText=view.findViewById(R.id.cloudTextView);
        pressLayout=view.findViewById(R.id.pressLayout);
        cloudLayout=view.findViewById(R.id.cloudLayout);
        temperName=view.findViewById(R.id.temperName);
    }

    private void setViews() {
        String[] cityNames = getResources().getStringArray(R.array.Cities);
        String currentName=cityNames[CitiesListFragment.currentPosition];
        cityName.setText(currentName);

        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            temperText.setTextSize(30);
            temperName.setTextSize(30);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)image.getLayoutParams();
            params.height= 200;
            image.setLayoutParams(params);
        }

        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), FindWeatherService.class);
        intent.putExtra(KEY_CITY,CitiesListFragment.currentPosition);
        getActivity().startService(intent);

        if(!MainActivity.checkBoxes[0]) pressLayout.setVisibility(View.GONE);
        if(!MainActivity.checkBoxes[1]) cloudLayout.setVisibility(View.GONE);
        if(!MainActivity.checkBoxes[2]) image.setVisibility(View.GONE);
    }

    private class ServiceFinishedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final Intent receivedIntent=intent;
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String currentTemp=receivedIntent.getStringExtra(FindWeatherService.KEY_BROADCAST_TEMPER);
                    temperText.setText(currentTemp);
                    String currentPress=receivedIntent.getStringExtra(FindWeatherService.KEY_BROADCAST_PRESS);
                    pressText.setText(currentPress);
                    String currentCloud=receivedIntent.getStringExtra(FindWeatherService.KEY_BROADCAST_CLOUD);
                    cloudText.setText(currentCloud);

                    int imageIndex;
                    int cloudness = Integer.parseInt(currentCloud);
                    if(cloudness==0){
                        imageIndex=0;
                    }else if (cloudness<=30&&cloudness>0){
                        imageIndex=1;
                    }else if(cloudness<=50&&cloudness>30){
                        imageIndex=2;
                    }else if(cloudness<100&&cloudness>50){
                        imageIndex=3;
                    }else imageIndex=4;

                    @SuppressLint("Recycle") TypedArray images = getResources().
                            obtainTypedArray(R.array.weather_images);
                    image.setImageResource(images.getResourceId(imageIndex, -1));
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION));
    }

    @Override
    public void onStop() {
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        super.onStop();
    }
}
