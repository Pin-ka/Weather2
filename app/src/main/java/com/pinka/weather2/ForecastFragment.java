package com.pinka.weather2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class ForecastFragment extends Fragment {
    private ArrayList<ForecastData> data=new ArrayList<>();
    private GregorianCalendar gc=new GregorianCalendar();
    private Random random=new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.forecast_recycler_view);
        getData();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RVAdapter adapter = new RVAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        for (int i=0;i<3;i++){
            gc.add(Calendar.DATE,1);
            int month=gc.get(Calendar.MONTH)+1;
            int day=gc.get(Calendar.DAY_OF_MONTH);
            int temper=random.nextInt(20);
            int press=742+random.nextInt(10);
            int cloud=random.nextInt(100);
            String dateTerminate;
            if (month<10){
                dateTerminate=".0";
            }
            else {
                dateTerminate=".";
            }
            data.add(new ForecastData(day+dateTerminate+month,
                    temper+"..."+(temper+random.nextInt(5)), press+" ", cloud+" "));
        }

    }
}
