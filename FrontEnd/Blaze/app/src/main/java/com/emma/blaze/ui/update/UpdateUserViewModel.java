package com.emma.blaze.ui.update;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class UpdateUserViewModel extends AndroidViewModel {
    private static final String[] RELATIONSHIP_TYPES = {"Amigos", "Casual", "Formal", "Otro"};
    private MutableLiveData <String[]> relationTypeLiveData ;
    private final MutableLiveData<String> biography;
    private final MutableLiveData<String> relationshipTypeSelected;
    private final MutableLiveData<String> phone;

    public UpdateUserViewModel(@NonNull Application application) {
        super(application);
        biography= new MutableLiveData<>();
        relationshipTypeSelected = new MutableLiveData<>();
        phone= new MutableLiveData<>();
        relationTypeLiveData= new MutableLiveData<>();
        relationTypeLiveData.setValue( RELATIONSHIP_TYPES);
    }

    public MutableLiveData<String> getBiography() {
        return biography;
    }

    public MutableLiveData<String> getRelationshipTypeSelected() {
        return relationshipTypeSelected;
    }


    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String[]> getRelationTypeLiveData() {
        return relationTypeLiveData;
    }


}