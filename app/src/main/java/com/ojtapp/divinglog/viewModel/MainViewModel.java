package com.ojtapp.divinglog.viewModel;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.ViewModel;

import com.ojtapp.divinglog.databinding.LogDateBinding;

public class MainViewModel extends ViewModel implements LogDateBinding.EventHandlers {
    private String diveNumber;
    private String place;
    private String point;
    private String depthMax;
    private String depthAve;
    private String airStart;
    private String airEnd;
    private String weather;
    private String temp;
    private String tempWater;
    private String visibility;
    private String memberNavigate;
    private String member;
    private String memo;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String timeDive;
    private String pictureUri;

    public MainViewModel() {
    }


    @Override
    public void onMakeClick(View view) {

    }

    @Override
    public void onDeleteClick(View view) {

    }

    @Override
    public void onEditClick(View view) {

    }
}
