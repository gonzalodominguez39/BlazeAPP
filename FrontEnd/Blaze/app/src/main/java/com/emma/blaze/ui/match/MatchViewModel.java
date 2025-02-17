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
import com.emma.blaze.data.model.Match;
import com.emma.blaze.data.repository.MatchRepository;
import com.emma.blaze.data.repository.MessageRepository;
import com.emma.blaze.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchViewModel extends AndroidViewModel {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final MutableLiveData<List<UserResponse>> users = new MutableLiveData<>();
    private final MutableLiveData<List<UserResponse>> usersChats = new MutableLiveData<>();
    private final MutableLiveData<List<Match>> matchesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Message>> lastMessages= new MutableLiveData<>();



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
            public void onResponse(@NonNull Call<List<UserResponse>> call, @NonNull Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserResponse> usersListResponse = response.body();

                    List<String> matchedUserIds = matchesLiveData.getValue() != null
                            ? matchesLiveData.getValue().stream()
                            .flatMap(match -> Arrays.asList(match.getUser1Id(), match.getUser2Id()).stream())
                            .distinct()
                            .collect(Collectors.toList())
                            : new ArrayList<>();

                    usersListResponse.removeIf(userResponse ->
                            !matchedUserIds.contains(String.valueOf(userResponse.getUserId())) ||
                                    Objects.equals(String.valueOf(userResponse.getUserId()), currentUserId)
                    );

                    users.postValue(usersListResponse);
                    getLastMessagesByUserId(Long.parseLong(currentUserId));
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
        matchRepository.getAllMatchesByUserId(userId).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    matchesLiveData.postValue(response.body());
                    getUserMatches(String.valueOf(userId));

                } else {
                    Log.d("error", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                Log.d("error", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getLastMessagesByUserId(long userId) {
        Call<List<Message>> call = messageRepository.findLastMessageBetweenUsers(userId);
        call.enqueue(new Callback<List<Message>> () {
            @Override
            public void onResponse(@NonNull Call<List<Message>>  call, @NonNull Response<List<Message>>  response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Message>  newLastMessages = response.body();
                    lastMessages.postValue(newLastMessages);

                    Log.d("lastMessages", "Mensaje obtenido: " + response.body());
                } else {
                    Log.d("lastMessage", "Error al obtener el mensaje, código: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>>  call, @NonNull Throwable t) {

                Log.e("lastMessage", "Fallo al obtener el mensaje: " + t.getMessage(), t);
            }
        });
    }

    @SuppressLint("NewApi")
    public void filterUsersWithMessages() {
        List<UserResponse> currentUsers = users.getValue() != null ? users.getValue() : new ArrayList<>();
        List<Message> currentLastMessages = lastMessages.getValue() != null ? lastMessages.getValue() : new ArrayList<>();

        List<UserResponse> usersWithMessages = currentUsers.stream()
                .filter(user -> currentLastMessages.stream()
                        .anyMatch(message ->
                                String.valueOf(user.getUserId()).equals(message.getSenderId()) ||
                                        String.valueOf(user.getUserId()).equals(String.valueOf(message.getReceiverId()))
                        )
                )
                .collect(Collectors.toList());

        usersChats.postValue(usersWithMessages);
        Log.d("lastMessages", "filterUsersWithMessages: "+usersWithMessages);
    }

    public LiveData<List<UserResponse>> getUsers() {
        return users;
    }



    public MutableLiveData<List<UserResponse>> getUsersChats() {
        return usersChats;
    }

    public MutableLiveData<List<Message>> getLastMessages() {
        return lastMessages;
    }

}