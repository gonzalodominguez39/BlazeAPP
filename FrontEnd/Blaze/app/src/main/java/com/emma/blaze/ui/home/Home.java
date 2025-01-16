package com.emma.blaze.ui.home;


import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.emma.blaze.R;
import com.emma.blaze.adapters.UserAdapter;
import com.emma.blaze.data.response.UserResponse;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.Objects;

public class Home extends Fragment {

    private HomeViewModel hViewModel;
    private FragmentHomeBinding binding;
    private CardStackLayoutManager manager;
    private UserAdapter adapter;

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        hViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);



        if (adapter == null) {
            adapter = new UserAdapter(hViewModel.getUsers().getValue(), requireContext());
        }

        hViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                adapter.updateUsers(users);
                binding.cardStackView.setAdapter(adapter);
            }
        });

        manager = new CardStackLayoutManager(requireContext(), new CardStackListener() {
            Boolean isRewinding = false;

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                View frontCardView = Objects.requireNonNull(binding.cardStackView.getLayoutManager())
                        .findViewByPosition(manager.getTopPosition());
                if (frontCardView != null) {
                    hViewModel.swipeColorCard(direction, requireContext());
                    Log.d("card", "onCardDragging: ");
                    hViewModel.getHeartColor().observe(getViewLifecycleOwner(), color -> {
                        ImageView imageHeart = frontCardView.findViewById(R.id.imageheart);
                        if (imageHeart != null) {
                            imageHeart.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        }
                    });

                    hViewModel.getCancelColor().observe(getViewLifecycleOwner(), color -> {
                        ImageView imageCancel = frontCardView.findViewById(R.id.imageCancel);
                        if (imageCancel != null) {
                            imageCancel.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        }
                    });
                }
            }

            @Override
            public void onCardAppeared(View view, int position) {
                hViewModel.resetColorCard(requireContext());
                ImageView imageHeart = view.findViewById(R.id.imageheart);
                ImageView imageCancel = view.findViewById(R.id.imageCancel);
                ImageView imageRewind = view.findViewById(R.id.imageRevert);
                if (position == 0 && imageRewind != null) {
                    imageRewind.setVisibility(View.INVISIBLE);
                } else if (imageRewind != null) {
                    imageRewind.setVisibility(View.VISIBLE);
                }

                if (imageHeart != null) {
                    imageHeart.setOnClickListener(v -> {
                        if (isRewinding) return;
                        Log.d("card", "onCardAppered: ");
                        hViewModel.swipeColorCard(Direction.Right, requireContext());
                        hViewModel.getHeartColor().observe(getViewLifecycleOwner(), color -> {
                            imageHeart.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                            hViewModel.performSwipe(Direction.Right, binding, manager);
                        });
                    });
                }

                if (imageCancel != null) {
                    imageCancel.setOnClickListener(v -> {
                        if (isRewinding) return;
                        hViewModel.swipeColorCard(Direction.Left, requireContext());
                        hViewModel.getCancelColor().observe(getViewLifecycleOwner(), color -> {
                            imageCancel.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                            hViewModel.performSwipe(Direction.Left, binding, manager);
                        });
                    });
                }

                if (imageRewind != null) {
                    imageRewind.setOnClickListener(v -> {
                        isRewinding = true;
                        hViewModel.swipeColorCard(Direction.Bottom, requireContext());
                        hViewModel.getRewindColor().observe(getViewLifecycleOwner(), color -> {
                            imageRewind.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                           binding.cardStackView.rewind();
                        });
                    });
                }
            }

            public void onCardSwiped(Direction direction) {
                if (isRewinding) return;
                try {
                    int swipedPosition = manager.getTopPosition() - 1;

                    if (swipedPosition < 0 || swipedPosition >= adapter.getItemCount()) {
                        return;
                    }

                    UserResponse swipedUser = adapter.getUserAtPosition(swipedPosition);
                    if (swipedUser != null && swipedUser.getUserId() != null) {
                        hViewModel.saveSwipe(swipedUser.getUserId(), direction);
                    }
                } catch (Exception e) {
                    Log.e("onCardSwiped", "Error procesando el swipe", e);
                }
                clearCardClickListeners();
            }

            @Override
            public void onCardRewound() {
                Log.d("card", "onCardRewound: " + manager.getTopPosition());
                isRewinding = false;
                hViewModel.resetColorCard(requireContext());
                clearCardClickListeners();
            }

            private void clearCardClickListeners() {
                View frontCardView = Objects.requireNonNull(binding.cardStackView.getLayoutManager())
                        .findViewByPosition(manager.getTopPosition());
                if (frontCardView != null) {
                    ImageView imageHeart = frontCardView.findViewById(R.id.imageheart);
                    ImageView imageCancel = frontCardView.findViewById(R.id.imageCancel);

                    if (imageHeart != null) imageHeart.setOnClickListener(null);
                    if (imageCancel != null) imageCancel.setOnClickListener(null);
                }
            }

            @Override
            public void onCardCanceled() {
                hViewModel.resetColorCard(requireContext());
            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        binding.cardStackView.setLayoutManager(manager);

        return binding.getRoot();
    }

}