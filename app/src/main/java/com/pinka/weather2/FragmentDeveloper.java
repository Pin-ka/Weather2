package com.pinka.weather2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDeveloper extends Fragment {
    TextView nameText,aboutText;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer,container,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameText=view.findViewById(R.id.textName);
        aboutText=view.findViewById(R.id.textAbout);
        nameText.setText("Разработано: Екатерина Кадырова");
        aboutText.setText("\n" +
                "Не знаю, что рассказывать о себе,\n" +
                "поэтому пусть лучше будут стихи...\n" +
                "\n" +
                "Тот, кто ты есть (авт.: Юлия Евстратова)\n" +
                "\n"+
                "Тот, кто ты есть на самом деле,\n" +
                "Давным-давно тебя ведет\n" +
                "И сквозь пургу, и сквозь метели,\n" +
                "И в снегопад, и в гололед,\n" +
                "\n"+
                "Ведет сквозь все,  до пониманья,\n" +
                "Что Он и Ты, есть ТОТ, кто есть.\n" +
                "И лишь одно твое вниманье\n" +
                "Значенье придает и вес \n" +
                "\n"+
                "Всему, что в жизни происходит\n" +
                "В тебе, с тобой, вокруг  тебя.\n" +
                "И не судьба тобою водит,\n" +
                "Судьба исходит из тебя.\n" +
                "\n"+
                "2005. март.");
    }

}
