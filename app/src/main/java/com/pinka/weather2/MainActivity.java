package com.pinka.weather2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean []checkBoxes={true,true,true};
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showFragments(savedInstanceState);
    }

    private void showFragments(Bundle savedInstanceState) {
        if(savedInstanceState==null){
            replaceDetailedFragment();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                replaceCitiesListFragment(R.id.fragment1);
            }
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_search_city).setVisible(false);
        }
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

    private void replaceCitiesListFragment(int idFragment){
        CitiesListFragment citiesListFragment=new CitiesListFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(idFragment,citiesListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_now) {
            replaceDetailedFragment();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                replaceCitiesListFragment(R.id.fragment1);
            }
        } else if (id == R.id.nav_forecast) {
            ForecastFragment forecastFragment=new ForecastFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment2,forecastFragment);
            fragmentTransaction.commit();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                FragmentImage.idImage=R.drawable.forecast_image;
                FragmentImage fragmentImage=new FragmentImage();
                FragmentTransaction fragmentTransactionFor1=getSupportFragmentManager().beginTransaction();
                fragmentTransactionFor1.replace(R.id.fragment1,fragmentImage);
                fragmentTransactionFor1.commit();
            }

        } else if (id == R.id.nav_developer) {
            FragmentDeveloper fragmentDeveloper=new FragmentDeveloper();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment2,fragmentDeveloper);
            fragmentTransaction.commit();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                FragmentImage.idImage=R.drawable.cat;
                FragmentImage fragmentImage=new FragmentImage();
                FragmentTransaction fragmentTransactionFor1=getSupportFragmentManager().beginTransaction();
                fragmentTransactionFor1.replace(R.id.fragment1,fragmentImage);
                fragmentTransactionFor1.commit();
            }

        } else if (id == R.id.nav_feedback) {
            FeedbackFragment feedbackFragment=new FeedbackFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment2,feedbackFragment);
            fragmentTransaction.commit();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                FragmentImage.idImage=R.drawable.send_message;
                FragmentImage fragmentImage=new FragmentImage();
                FragmentTransaction fragmentTransactionFor1=getSupportFragmentManager().beginTransaction();
                fragmentTransactionFor1.replace(R.id.fragment1,fragmentImage);
                fragmentTransactionFor1.commit();
            }

        } else if (id==R.id.nav_search_city){
                replaceCitiesListFragment(R.id.fragment2);
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
