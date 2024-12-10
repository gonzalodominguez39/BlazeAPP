package com.emma.blaze.ui.home;
import android.app.Application;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.emma.blaze.model.User;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<Integer> heartColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> cancelColor = new MutableLiveData<>();
    private final MutableLiveData<Integer> rewindColor = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        heartColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        cancelColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        rewindColor.setValue(ContextCompat.getColor(application, R.color.white_opacity));
        loadUsers();
    }

    public LiveData<List<User>> getUsers() {
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
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Emma", "john.c.breckinridge@altostrat.com", "emma"));
        userList.add(new User(2L, "John", "william.henry.harrison@example-pet-store.com", "john"));
        userList.add(new User(1L, "Rousse", "john.c.breckinridge@altostrat.com", "rouse"));
        userList.add(new User(2L, "Mica", "william.henry.harrison@example-pet-store.com", "mivca"));
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
        }else if (direction == Direction.Bottom) {
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

public void rewindCard(FragmentHomeBinding binding, CardStackLayoutManager manager){
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
