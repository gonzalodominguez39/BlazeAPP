package com.emma.blaze.ui.home;
import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.time.Duration;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void swipeColorCard(Direction direction, FragmentHomeBinding binding, Context context, CardStackLayoutManager manager) {
        View frontCardView = Objects.requireNonNull(binding.cardStackView.getLayoutManager()).findViewByPosition(manager.getTopPosition());

        if (frontCardView != null) {
            ImageView imageHeart = frontCardView.findViewById(R.id.imageheart);
            ImageView imageCancel = frontCardView.findViewById(R.id.imageCancel);
            if (imageHeart != null && imageCancel != null) {
                if (direction == Direction.Right) {
                    imageHeart.setColorFilter(ContextCompat.getColor(context, R.color.purple_primary), PorterDuff.Mode.SRC_IN);
                } else if (direction == Direction.Left) {
                    imageCancel.setColorFilter(ContextCompat.getColor(context, R.color.red_opacity), PorterDuff.Mode.SRC_IN);
                } else {
                    imageHeart.setColorFilter(ContextCompat.getColor(context, R.color.white_opacity), PorterDuff.Mode.SRC_IN);
                    imageCancel.setColorFilter(ContextCompat.getColor(context, R.color.white_opacity), PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }

    public void performSwipe(Direction direction, FragmentHomeBinding binding, CardStackLayoutManager manager) {
        View frontCardView = Objects.requireNonNull(binding.cardStackView.getLayoutManager())
                .findViewByPosition(manager.getTopPosition());

        if (frontCardView != null) {
            ImageView imageHeart = frontCardView.findViewById(R.id.imageheart);
            ImageView imageCancel = frontCardView.findViewById(R.id.imageCancel);

            if (imageHeart != null && imageCancel != null) {
              if(direction==Direction.Right){
                  imageHeart.setColorFilter(ContextCompat.getColor(getApplication(), R.color.purple_primary), PorterDuff.Mode.SRC_IN);
              }else {
                  imageCancel.setColorFilter(ContextCompat.getColor(getApplication(), R.color.red_opacity), PorterDuff.Mode.SRC_IN);
              }
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(direction)
                        .setDuration(300)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();

                manager.setSwipeAnimationSetting(setting);

                binding.cardStackView.swipe();
            }
        }
    }

    public void dropCard(FragmentHomeBinding binding, Context context, CardStackLayoutManager manager) {
        View frontCardView = Objects.requireNonNull(binding.cardStackView.getLayoutManager()).findViewByPosition(manager.getTopPosition());
        if (frontCardView != null) {
            ImageView imageHeart = frontCardView.findViewById(R.id.imageheart);
            ImageView imageCancel = frontCardView.findViewById(R.id.imageCancel);
            if (imageHeart != null && imageCancel != null) {
                imageHeart.setColorFilter(ContextCompat.getColor(context, R.color.white_opacity), PorterDuff.Mode.SRC_IN);
                imageCancel.setColorFilter(ContextCompat.getColor(context, R.color.white_opacity), PorterDuff.Mode.SRC_IN);
            }
        }
    }
}
