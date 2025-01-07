package com.emma.blaze.ui.lookingfoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LookingFoorViewModel extends AndroidViewModel {
    private static final String[] RELATIONSHIP_TYPES = {"Amigos", "Casual", "Formal", "Otro"};
    private MutableLiveData <String[]> relationTypeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> genderInterest = new MutableLiveData<>();
    private MutableLiveData<String> selectedRelationType = new MutableLiveData<>();

    public LookingFoorViewModel(@NonNull Application application) {
        super(application);
        relationTypeLiveData.setValue(RELATIONSHIP_TYPES);
    }

    public MutableLiveData<String[]> getRelationTypeLiveData() {
        return relationTypeLiveData;
    }

    public MutableLiveData<String> getGenderInterest() {
        return genderInterest;
    }

    public MutableLiveData<String> getSelectedRelationType() {
        return selectedRelationType;
    }
}
