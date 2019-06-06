package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinka.weather2.rest.OpenWeatherRepo;
import com.pinka.weather2.rest.entites.WeatherRequestRestModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastFragment extends Fragment {
    private ArrayList<ForecastData> data=new ArrayList<>();
    private GregorianCalendar gc=new GregorianCalendar();
    private String currentName;
    WeatherRequestRestModel model = new WeatherRequestRestModel();
    private int forecastDays=3;
    private String [] temperForDays=new String[forecastDays];
    private float [] pressForDays=new float[forecastDays];
    private int [] cloudForDays=new int[forecastDays];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.forecast_recycler_view);
        String[] cityNamesEng = getResources().getStringArray(R.array.Cities_for_OWM);
        currentName= cityNamesEng[CitiesListFragment.currentPosition];
        getData();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RVAdapter adapter = new RVAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("DefaultLocale")
    private void getData() {
        requestRetrofit();
        for (int i=0;i<forecastDays;i++){
            gc.add(Calendar.DATE,1);
            int month=gc.get(Calendar.MONTH)+1;
            int day=gc.get(Calendar.DAY_OF_MONTH);
            String dateTerminate;
            if (month<10){
                dateTerminate=".0";
            }
            else {
                dateTerminate=".";
            }
            data.add(new ForecastData(day+dateTerminate+month,
                    temperForDays[i], String.format("%.0f",pressForDays[i])+" ", cloudForDays[i]+" "));
        }

    }

    private void requestRetrofit() {
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(currentName,"metric",
                forecastDays*8,"138da5b39eee097c6026108fe79add1c")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if(response.body()!=null&&response.isSuccessful()){
                            model=response.body();
                            setForecastData();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                       Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "WebError", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void setForecastData() {
        float min_temp;
        float max_temp;
        float press;
        int cloud;
        for (int i=0;i<forecastDays;i++){
            min_temp=100;
            max_temp=-100;
            press=0;
            cloud=0;
            for (int j=1+(8*i);j<=8+(8*i);j++){
                if (model.list[j].main.temp_min<min_temp) {
                    min_temp=model.list[j].main.temp_min;
                }
                if (model.list[j].main.temp_max>max_temp) {
                    max_temp=model.list[j].main.temp_max;
                }
                press += model.list[j].main.pressure;
                cloud += model.list[j].clouds.all;
            }
            cloudForDays[i]=cloud/8;
            pressForDays[i]= (float) (press*0.750062/8);
            temperForDays[i]=min_temp+"..."+max_temp;
        }

    }
}
