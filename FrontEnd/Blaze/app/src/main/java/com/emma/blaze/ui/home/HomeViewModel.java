package com.emma.blaze.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> heartColor = new MutableLiveData<>();

    public void setHeartColor(int color) {
        Log.d("HomeViewModel", "Color set to: " + color);
        heartColor.setValue(color);
    }

    public LiveData<Integer> getHeartColor() {
        return heartColor;
    }
}