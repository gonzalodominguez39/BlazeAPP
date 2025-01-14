package com.emma.blaze.ui.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.R;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.response.UserResponse;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.emma.blaze.helpers.UserManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private UserManager userManager;
    private final MutableLiveData<List<UserResponse>> users = new MutableLiveData<>();
    private final MutableLiveData<Integer> heartColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> cancelColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> rewindColor = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository= new UserRepository(application.getApplicationContext());
        heartColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        userManager= UserManager.getInstance();
        loadUsers();
    }

    public LiveData<List<UserResponse>> getUsers() {
        return users;
    }

    public LiveData<Integer> getHeartColor() {
        return heartColor;
    }

    public LiveData<Integer> getCancelColor() {
        return cancelColor;
    }

    public LiveData<Integer> getRewindColor() {
        return rewindColor;
    }

    private void loadUsers() {
        List<UserResponse> userList = new ArrayList<>();
        userRepository.getAllUsers().enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful()) {
                    List<UserResponse> usersListResponse = response.body();
                    for (UserResponse userResponse : usersListResponse) {
                        if(Objects.equals(userResponse.getUserId(), userManager.getCurrentUser().getUserId()) || Objects.equals(userResponse.getPrivacySetting(), "PRIVATE")){
                            usersListResponse.remove(userResponse);
                        }
                    }
                    users.postValue(usersListResponse);
                } else {
                    Log.e("Users", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.e("UserActivity", "Fallo la solicitud: " + t.getMessage());
            }
        });

        users.setValue(userList);
    }

    public void swipeColorCard(Direction direction, Context context) {
        if (direction == Direction.Right) {
            heartColor.setValue(ContextCompat.getColor(context, R.color.purple_primary));
            cancelColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
            rewindColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        } else if (direction == Direction.Left) {
            cancelColor.setValue(ContextCompat.getColor(context, R.color.red_opacity));
            heartColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
            rewindColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        } else if (direction == Direction.Bottom) {
            rewindColor.setValue(ContextCompat.getColor(context, R.color.black_opacity));
            heartColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
            cancelColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        }
    }

    public void performSwipe(Direction direction, FragmentHomeBinding binding, CardStackLayoutManager manager) {

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);


        binding.cardStackView.swipe();
    }

    public void rewindCard(FragmentHomeBinding binding, CardStackLayoutManager manager) {
        RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .build();

        manager.setRewindAnimationSetting(setting);


        binding.cardStackView.rewind();
    }


    public void resetColorCard(Context context) {
        heartColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
    }
}
