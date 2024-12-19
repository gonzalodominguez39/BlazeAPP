package com.emma.blaze.ui.lookingfoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LookingFoorViewModel extends AndroidViewModel {
private MutableLiveData<String> genderInterest = new MutableLiveData<>();
    public LookingFoorViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<String> getGenderInterest() {
        return genderInterest;
    }

}
