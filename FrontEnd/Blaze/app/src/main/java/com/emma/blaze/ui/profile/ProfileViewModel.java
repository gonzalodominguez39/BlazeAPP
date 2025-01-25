package com.emma.blaze.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.User;
import com.emma.blaze.helpers.UserManager;


public class ProfileViewModel extends AndroidViewModel {
    private MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userUpdate = new MutableLiveData<>();


    private UserManager userManager;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userManager= UserManager.getInstance();
        if(userManager.getCurrentUser()!=null){
        userLiveData.setValue(userManager.getCurrentUser());}
        userUpdate.setValue(new User());
    }


    public MutableLiveData<User> getUserUpdate() {
        return userUpdate;
    }

    public MutableLiveData<UserResponse> getUserLiveData() {
        return userLiveData;
    }


}