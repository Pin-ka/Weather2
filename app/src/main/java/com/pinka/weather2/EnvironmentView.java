package com.pinka.weather2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class EnvironmentView extends LinearLayout {

    public EnvironmentView(Context context) {
        super(context);
        initViews(context);
    }

    public EnvironmentView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initViews(context);
    }

    public EnvironmentView(Context context, AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.compound_environment_view,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
