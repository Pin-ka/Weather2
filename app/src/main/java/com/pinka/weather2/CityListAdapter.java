package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {
    private CityData[] data;
    private Context context;

    CityListAdapter(@NonNull Context context, CityData[] data){
        this.data=data;
        this.context=context;
    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public CityData getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityData item=getItem(position);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.element_list_city_layout,parent,false);
        TextView textView=convertView.findViewById(R.id.cityTextView);
        textView.setText(item.cityName);
        return convertView;
    }
}
