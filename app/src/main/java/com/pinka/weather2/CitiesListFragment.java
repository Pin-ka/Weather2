package com.pinka.weather2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Objects;

public class CitiesListFragment extends Fragment {
    private ListView listView;
    public static int currentPosition;
    private Bundle savedInstanceState=null;

    public static final String indexCityKey = "index_City_Key";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
            final SharedPreferences indexCityPref = Objects.requireNonNull(getActivity()).
                    getSharedPreferences(indexCityKey, Context.MODE_PRIVATE);
            saveToPreference(indexCityPref);
        }
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showCoatOfArms();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
    }

    private void initList(Context context) {
        CityData[]data=new CityData[]{
                new CityData(getResources().getStringArray(R.array.Cities)[0]),
                new CityData(getResources().getStringArray(R.array.Cities)[1]),
                new CityData(getResources().getStringArray(R.array.Cities)[2]),
                new CityData(getResources().getStringArray(R.array.Cities)[3]),
                new CityData(getResources().getStringArray(R.array.Cities)[4])
        };
        CityListAdapter adapter=new CityListAdapter(context,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                final SharedPreferences indexCityPref = Objects.requireNonNull(getActivity()).
                        getSharedPreferences(indexCityKey, Context.MODE_PRIVATE);
                saveToPreference(indexCityPref);
                showCoatOfArms();
            }
        });
    }

    private void showCoatOfArms() {
        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            listView.setItemChecked(currentPosition, true);

            DetailedFragment detailedFragment=null;
            if(!MainActivity.isAnotherFragmentInFragment2){
                detailedFragment = (DetailedFragment) Objects.requireNonNull(getFragmentManager()).
                        findFragmentById(R.id.fragment2);
            }
            if (detailedFragment == null || detailedFragment.getIndex() != currentPosition) {
                detailedFragment = DetailedFragment.create(currentPosition);
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                fragmentTransaction.replace(R.id.fragment2, detailedFragment);
                fragmentTransaction.commit();
            }
        } else {
            DetailedFragment detailedFragment=new DetailedFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(DetailedFragment.KEY,currentPosition);
            detailedFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment2,detailedFragment);
            fragmentTransaction.commit();
        }
    }

    private void saveToPreference(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        int indexCity = currentPosition;
        editor.putInt(indexCityKey, indexCity);
        editor.apply();
    }
}
