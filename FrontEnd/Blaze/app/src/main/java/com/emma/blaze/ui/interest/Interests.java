package com.emma.blaze.ui.interest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.adapters.InterestAdapter;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentInterestsBinding;
import com.emma.blaze.helpers.GridSpacingItemDecoration;
import com.emma.blaze.data.model.Interest;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;


public class Interests extends Fragment {
    private RecyclerView recyclerView;
    private FragmentInterestsBinding binding;
    private InterestAdapter adapter;
    private InterestsViewModel interestsViewModel;
    private UserViewModel userViewModel;
    private List<String> interests;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInterestsBinding.inflate(inflater, container, false);
        interestsViewModel = new ViewModelProvider(this).get(InterestsViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        recyclerView = binding.recyclerViewInterests;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true));

        interestsViewModel.getInterests().observe(getViewLifecycleOwner(), interests -> {
            if (interests != null) {
                this.interests = interests.stream()
                        .map(Interest::getName)
                        .collect(Collectors.toList());
                adapter.updateInterests(this.interests);
            }
        });



        adapter = new InterestAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        binding.nextButton.setOnClickListener(v -> {
            User user = userViewModel.getUserLiveData().getValue();
            assert user != null;
            if(adapter.getSelectedInterests().size()<2){
                Toast.makeText(getContext(),"debes seleccionar almenos 3 intereses" ,Toast.LENGTH_SHORT).show();
                return;
            }
            user.setInterests(adapter.getSelectedInterests());
            userViewModel.getUserLiveData().setValue(user);
            navigateScreen(R.id.action_interests_to_uploadImage);
        });

        return binding.getRoot();
    }


    private void navigateScreen(int actionId) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId, null, new NavOptions.Builder().setPopUpTo(R.id.interests, true).build());
    }

}

