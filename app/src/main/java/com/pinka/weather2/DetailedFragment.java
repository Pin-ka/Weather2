package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.content.Intent;
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
        initViews(view);
        setViews();
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

        @SuppressLint("Recycle") TypedArray images = getResources().
                obtainTypedArray(R.array.weather_images);
        image.setImageResource(images.getResourceId(CitiesListFragment.currentPosition, -1));


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

        temperText.setText(MainActivity.weatherData[0]);
        pressText.setText(MainActivity.weatherData[1]);
        cloudText.setText(MainActivity.weatherData[2]);
        MainActivity.isBackgroundServiceEnd=false;

        if(!MainActivity.checkBoxes[0]) pressLayout.setVisibility(View.GONE);
        if(!MainActivity.checkBoxes[1]) cloudLayout.setVisibility(View.GONE);
        if(!MainActivity.checkBoxes[2]) image.setVisibility(View.GONE);
    }

}
