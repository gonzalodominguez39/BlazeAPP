package com.emma.blaze.ui.match;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.data.model.UserMatch;
import com.emma.blaze.data.repository.MatchRepository;
import com.emma.blaze.data.repository.MessageRepository;
import com.emma.blaze.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchViewModel extends AndroidViewModel {
    private MatchRepository matchRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    private MutableLiveData<List<UserResponse>> users = new MutableLiveData<>();
    private MutableLiveData<List<UserMatch>> matchesLiveData = new MutableLiveData<>();
    private MutableLiveData<Message> lastMessage = new MutableLiveData<>();
    private  MutableLiveData <List<UserResponse>> chats = new MutableLiveData<>();


    public MatchViewModel(@NonNull Application application) {
        super(application);
        matchRepository = new MatchRepository(application.getApplicationContext());
        userRepository = new UserRepository(application.getApplicationContext());
        messageRepository = new MessageRepository(application.getApplicationContext());
    }

    @SuppressLint("NewApi")
    public void getUserMatches(String currentUserId) {
        userRepository.getAllUsers().enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserResponse> usersListResponse = response.body();

                    List<String> matchedUserIds = matchesLiveData.getValue() != null
                            ? matchesLiveData.getValue().stream()
                            .flatMap(match -> List.of(match.getUser1Id(), match.getUser2Id()).stream())
                            .distinct()
                            .toList()
                            : new ArrayList<>();

                    usersListResponse.removeIf(userResponse ->
                            !matchedUserIds.contains(String.valueOf(userResponse.getUserId())) ||
                                    Objects.equals(String.valueOf(userResponse.getUserId()), currentUserId)
                    );

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
    }

    public void getMatches(Long userId) {
        matchRepository.getAllMatchesByUserId(userId).enqueue(new Callback<List<UserMatch>>() {
            @Override
            public void onResponse(Call<List<UserMatch>> call, Response<List<UserMatch>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    matchesLiveData.postValue(response.body());
                    getUserMatches(String.valueOf(userId));
                } else {
                    Log.d("error", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserMatch>> call, Throwable t) {
                Log.d("error", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getLastMessageBetween(long user1Id,long user2Id) {
        Call<Message> call = messageRepository.findLastMessageBetweenUsers(user1Id,user2Id);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lastMessage.postValue(response.body());
                    Log.d("lastMessage", "onResponse: "+response.body());
                }else {
                    Log.d("lastMessage", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("lastMessage", "onFailure: "+t.getMessage());
            }
        });
    }

    public LiveData<List<UserResponse>> getUsers() {
        return users;
    }

    public LiveData<List<UserMatch>> getMatchesLiveData() {
        return matchesLiveData;
    }

    public LiveData<List<UserResponse>> getChats() {
        return null;
    }
}