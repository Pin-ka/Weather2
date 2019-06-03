package com.pinka.weather2;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import static com.pinka.weather2.DetailedFragment.KEY_CITY;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean []checkBoxes={true,true,true};

    private DrawerLayout drawer;
    private EnvironmentView environment;
    private TextView inputTempTextEnvironment,inputHumidTextEnvironment;
    private StringBuilder currentHumidity=new StringBuilder();
    private StringBuilder currentTemperature=new StringBuilder();

    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showFragments(savedInstanceState);
        getSensors();
    }

    private void getSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        SensorEventListener listenerTemperature=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                showTemperatureSensor(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        SensorEventListener listenerHumidity=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                showHumiditySensor(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(listenerTemperature, sensorTemperature,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void showHumiditySensor(SensorEvent event) {
        currentHumidity.append(event.values[0]).append(" %");
        inputHumidTextEnvironment.setText(currentHumidity);
        currentHumidity.setLength(0);
    }

    private void showTemperatureSensor(SensorEvent event) {
        currentTemperature.append(event.values[0]).append(" °C");
        inputTempTextEnvironment.setText(currentTemperature);
        currentTemperature.setLength(0);
    }

    private void showFragments(Bundle savedInstanceState) {
        if(savedInstanceState==null){
            replaceDetailedFragment();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                CitiesListFragment citiesListFragment=new CitiesListFragment();
                replaceFragment(R.id.fragment1,citiesListFragment);
            }
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu=toolbar.getMenu();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        environment=findViewById(R.id.environment);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_search_city).setVisible(false);
            menu.findItem(R.id.nav_environment).setVisible(false);
            environment.setVisibility(View.VISIBLE);
        }else {
            environment.setVisibility(View.INVISIBLE);
        }
        inputTempTextEnvironment=environment.findViewById(R.id.inputTempText);
        inputHumidTextEnvironment=environment.findViewById(R.id.inputHumidText);
        Intent intent = new Intent(getApplicationContext(), FindWeatherService.class);
        intent.putExtra(KEY_CITY,CitiesListFragment.currentPosition);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem checkBoxPress = menu.findItem(R.id.menu_press);
        MenuItem checkBoxCloud = menu.findItem(R.id.menu_cloud);
        MenuItem checkBoxImage = menu.findItem(R.id.menu_image);
        checkBoxPress.setChecked(checkBoxes[0]);
        checkBoxCloud.setChecked(checkBoxes[1]);
        checkBoxImage.setChecked(checkBoxes[2]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case  R.id.menu_press:{
                checkBoxes[0] = !checkBoxes[0];
                break;
            }
            case R.id.menu_cloud:{
                checkBoxes[1] = !checkBoxes[1];
                break;
            }
            case  R.id.menu_image:{
                checkBoxes[2] = !checkBoxes[2];
                break;
            }
        }

        replaceDetailedFragment();
        invalidateOptionsMenu();
        return true;
    }
    private void replaceDetailedFragment(){
        DetailedFragment detailedFragment=new DetailedFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(DetailedFragment.KEY,CitiesListFragment.currentPosition);
        detailedFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment2,detailedFragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(int idFrameLayout, Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(idFrameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_now) {
            invalidateOptionsMenu();
            environment.setVisibility(View.INVISIBLE);
            replaceDetailedFragment();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                CitiesListFragment citiesListFragment=new CitiesListFragment();
                replaceFragment(R.id.fragment1,citiesListFragment);
            }
        } else if (id == R.id.nav_forecast) {
            menu.clear();
            environment.setVisibility(View.INVISIBLE);
            ForecastFragment forecastFragment=new ForecastFragment();
            replaceFragment(R.id.fragment2,forecastFragment);
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                environment.setVisibility(View.VISIBLE);
                FrameLayout frameLayout=findViewById(R.id.fragment1);
                if(frameLayout.getChildCount()>1){
                    frameLayout.removeViewAt(1);
                }
            } else {
                environment.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_developer) {
            menu.clear();
            environment.setVisibility(View.INVISIBLE);
            FragmentDeveloper fragmentDeveloper=new FragmentDeveloper();
            replaceFragment(R.id.fragment2,fragmentDeveloper);
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                FragmentImage.idImage=R.drawable.cat;
                FragmentImage fragmentImage=new FragmentImage();
                replaceFragment(R.id.fragment1,fragmentImage);
            }
        } else if (id == R.id.nav_feedback) {
            menu.clear();
            environment.setVisibility(View.INVISIBLE);
            FeedbackFragment feedbackFragment=new FeedbackFragment();
            replaceFragment(R.id.fragment2,feedbackFragment);
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                FragmentImage.idImage=R.drawable.send_message;
                FragmentImage fragmentImage=new FragmentImage();
                replaceFragment(R.id.fragment1,fragmentImage);
            }
        } else if (id==R.id.nav_search_city){
            invalidateOptionsMenu();
            environment.setVisibility(View.INVISIBLE);
            CitiesListFragment citiesListFragment=new CitiesListFragment();
            replaceFragment(R.id.fragment2,citiesListFragment);
        }else if (id==R.id.nav_environment){
            menu.clear();
            environment.setVisibility(View.VISIBLE);
            FrameLayout frameLayout=findViewById(R.id.fragment2);
            if(frameLayout.getChildCount()>1){
                frameLayout.removeViewAt(1);
            }

        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
