package com.emma.blaze.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.emma.blaze.R;
import com.emma.blaze.adapters.UserAdapter;
import com.emma.blaze.databinding.FragmentHomeBinding;
import com.emma.blaze.model.User;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;

import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;
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
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Emma", "john.c.breckinridge@altostrat.com","emma"));
        users.add(new User(2L, "John", "william.henry.harrison@example-pet-store.com","john"));
        users.add(new User(1L, "Rousse", "john.c.breckinridge@altostrat.com","rouse"));
        users.add(new User(2L, "Mica", "william.henry.harrison@example-pet-store.com","mivca"));
        adapter = new UserAdapter(users, requireContext());
        hViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


        manager = new CardStackLayoutManager(requireContext(), new CardStackListener() {


                @Override
                public void onCardAppeared(View view, int position) {
                    ImageView imageHeart = view.findViewById(R.id.imageheart);
                    ImageView imageCancel = view.findViewById(R.id.imageCancel);

                    if (imageHeart != null && imageCancel != null) {
                        imageHeart.setOnClickListener(v -> {

                            hViewModel.performSwipe(Direction.Right, binding, manager);
                        });
                        imageCancel.setOnClickListener(v -> {
                            hViewModel.performSwipe(Direction.Left, binding, manager);
                        });
                    }
                }

                @Override
            public void onCardDragging(Direction direction, float ratio) {
                hViewModel.swipeColorCard(direction, binding, requireContext(), manager);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                // Maneja eventos de swipe si es necesario
            }

            @Override
            public void onCardRewound() { }

            @Override
            public void onCardCanceled() {
                hViewModel.dropCard(binding, requireContext(), manager);
            }



            @Override
            public void onCardDisappeared(View view, int position) { }
        });



        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);

        binding.cardStackView.setLayoutManager(manager);




        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);

       binding.cardStackView.setLayoutManager(manager);
        binding.cardStackView.setAdapter(adapter);
    return binding.getRoot();
    }


}