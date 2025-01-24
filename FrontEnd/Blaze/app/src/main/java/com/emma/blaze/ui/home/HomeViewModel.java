package com.emma.blaze.ui.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.R;
import com.emma.blaze.data.model.Swipe;
import com.emma.blaze.data.repository.MatchRepository;
import com.emma.blaze.data.repository.SwipeRepository;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.emma.blaze.helpers.UserFunctions;
import com.emma.blaze.helpers.UserManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private SwipeRepository swipeRepository;

    private UserManager userManager;
    private final MutableLiveData<List<UserResponse>> users = new MutableLiveData<>();
    private final MutableLiveData<Integer> heartColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> cancelColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> rewindColor = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        swipeRepository = new SwipeRepository(application.getApplicationContext());
        heartColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        userManager = UserManager.getInstance();
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
                    if (usersListResponse != null) {
                        filterUsers(usersListResponse);
                    }
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


    public void filterUsers(List<UserResponse> listUsers) {
        List<UserResponse> filteredUsers = new ArrayList<>();
        for (UserResponse userResponse : listUsers) {
            if (!Objects.equals(userResponse.getUserId(), userManager.getCurrentUser().getUserId())
                    && !Objects.equals(userResponse.getPrivacySetting(), "PRIVATE")) {

                if (userManager.getCurrentUser().getGenderInterest().equals("FEMALE")
                        && userResponse.getGender().equals("FEMALE")) {
                    filteredUsers.add(userResponse);
                } else if (userManager.getCurrentUser().getGenderInterest().equals("MALE")
                        && userResponse.getGender().equals("MALE")) {
                    filteredUsers.add(userResponse);
                }
            }
        }
        users.postValue(filteredUsers);
    }

    public void saveSwipe(long swipedUserId, Direction direction) {
        Swipe swipeRequest = UserFunctions.CrateSwipe(userManager.getCurrentUser().getUserId(), swipedUserId, direction.name());
        Call<Boolean> call = swipeRepository.saveSwipe(swipeRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean swipeResponse = response.body();
                    Log.d("match", "match " + swipeResponse);

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("saveSwipe", "onFailure: ", t);
            }
        });
    }


    public void resetColorCard(Context context) {
        heartColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
    }
}
