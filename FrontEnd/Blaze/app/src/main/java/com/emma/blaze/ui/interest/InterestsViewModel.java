package com.emma.blaze.ui.interest;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.emma.blaze.data.repository.InterestRepository;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.emma.blaze.data.model.Interest;


public class InterestsViewModel extends AndroidViewModel {


    public InterestsViewModel(@NonNull Application application) {
        super(application);
        this.interestRepository = new InterestRepository();
    }

    private final InterestRepository interestRepository;
    private final MutableLiveData<List<Interest>> interests = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();


    public LiveData<List<Interest>> getInterests() {
        loadInterests();
        return interests;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void loadInterests() {
       Call<List<Interest>> call = interestRepository.getAllInterests();
        call.enqueue(new Callback<List<Interest>>() {
            @Override
            public void onResponse(Call<List<Interest>> call, Response<List<Interest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    interests.setValue(response.body());
                } else {
                    errorMessage.setValue("Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Interest>> call, Throwable t) {
                errorMessage.setValue("Failed to load interests: " + t.getMessage());
            }
        });
    }
}