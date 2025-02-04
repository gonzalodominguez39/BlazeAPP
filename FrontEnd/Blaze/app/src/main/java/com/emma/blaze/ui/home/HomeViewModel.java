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
import com.emma.blaze.data.model.User;
import com.emma.blaze.data.model.UserMatch;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final SwipeRepository swipeRepository;
    private final MatchRepository matchRepository;
    private final MutableLiveData<Boolean> isLoading;
    private List<UserResponse> usersNotFilter;

    private  UserManager userManager;
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
        usersNotFilter = new ArrayList<>();
        matchRepository = new MatchRepository(application.getApplicationContext());
        isLoading = new MutableLiveData<>();
        if(UserManager.getInstance()!=null) {
            userManager = UserManager.getInstance();
        }
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

    public void loadUsers() {
        List<UserResponse> userList = new ArrayList<>();
        userRepository.getAllUsers().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<UserResponse>> call, @NonNull Response<List<UserResponse>> response) {
                if (response.isSuccessful()) {
                    List<UserResponse> usersListResponse = response.body();
                    if (usersListResponse != null) {
                        usersNotFilter = usersListResponse;
                        Log.d("filter", "userManager" + userManager.getCurrentUser().getUserId());
                        filterUsersWithMatches(userManager.getCurrentUser().getUserId());

                    }
                } else {
                    Log.e("Users", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserResponse>> call, @NonNull Throwable t) {
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


    public void filterUsers(UserResponse user) {
        if (user == null) {
            Log.e("HomeViewModel", "El usuario actual no está configurado.");
            return;
        }

        for (UserResponse users : usersNotFilter) {
            Log.d("filter", "filterUsers: " + users.getUserId());
        }

        List<UserResponse> filteredUsers = new ArrayList<>();
        for (UserResponse userResponse : usersNotFilter) {
            if (!Objects.equals(userResponse.getUserId(), user.getUserId())
                    && !userResponse.getPrivacySetting().equalsIgnoreCase("PRIVATE")) {

                if (user.getGenderInterest().equals("FEMALE") && userResponse.getGender().equals("FEMALE")) {
                    filteredUsers.add(userResponse);
                } else if (user.getGenderInterest().equals("MALE") && userResponse.getGender().equals("MALE")) {
                    filteredUsers.add(userResponse);
                } else if (user.getGenderInterest().equals("ALL")||user.getGenderInterest().equals("NOT_SPECIFIED")) {
                    filteredUsers.add(userResponse);
                }
            }

        }

        users.postValue(filteredUsers);

    }

    public void filterUsersWithMatches(Long userId) {

        matchRepository.getAllMatchesByUserId(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<UserMatch>> call, @NonNull Response<List<UserMatch>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<UserMatch> matches = response.body();
                    Log.d("matches", "" + response.body());

                    filterUsersBasedOnNoMatches(matches, userId);
                    isLoading.postValue(true);
                } else {
                    isLoading.postValue(false);
                    Log.d("HomeViewModel", "No se pudieron obtener los matches");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserMatch>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "Error al obtener los matches: " + t.getMessage());
            }
        });
    }

    private void filterUsersBasedOnNoMatches(List<UserMatch> matches, Long currentUserId) {
        if (matches == null || matches.isEmpty()) {
            filterUsers(userManager.getCurrentUser());
            return;
        }
        if (currentUserId ==null){
            return;
        }

        List<Long> matchedUserIds = new ArrayList<>();
        for (UserMatch match : matches) {
            try {
                matchedUserIds.add(Long.parseLong(match.getUser1Id()));
                matchedUserIds.add(Long.parseLong(match.getUser2Id()));
            } catch (NumberFormatException e) {
                Log.e("HomeViewModel", "Error al convertir ID de usuario a Long", e);
            }
        }

        List<UserResponse> filteredUsers = new ArrayList<>();
        for (UserResponse userResponse : usersNotFilter) {
            if (!matchedUserIds.contains(userResponse.getUserId()) && !userResponse.getUserId().equals(currentUserId)) {
                filteredUsers.add(userResponse);
            }
        }

        usersNotFilter = filteredUsers;
        Log.d("Home", "filterUsersBasedOnNoMatches: " + usersNotFilter);
    }

    public void saveSwipe(long swipedUserId, Direction direction) {
        Swipe swipeRequest = UserFunctions.CrateSwipe(userManager.getCurrentUser().getUserId(), swipedUserId, direction.name());
        Call<Boolean> call = swipeRepository.saveSwipe(swipeRequest);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean swipeResponse = response.body();
                    Log.d("match", "match " + swipeResponse);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("saveSwipe", "onFailure: ", t);
            }
        });
    }


    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void resetColorCard(Context context) {
        heartColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(context, R.color.white_opacity));
    }
}
