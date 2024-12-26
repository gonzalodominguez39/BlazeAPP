package com.emma.blaze.ui.interest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.emma.blaze.R;
import com.emma.blaze.adapters.InterestAdapter;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentInterestsBinding;
import com.emma.blaze.helpers.GridSpacingItemDecoration;
import com.emma.blaze.data.model.Interest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;


public class Interests extends Fragment {
    private RecyclerView recyclerView;
    private FragmentInterestsBinding binding;
    private InterestAdapter adapter;
    private InterestsViewModel interestsViewModel;
   private List<String> interests;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInterestsBinding.inflate(inflater, container, false);
        interestsViewModel = new ViewModelProvider(this).get(InterestsViewModel.class);

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
    Bundle bundle = getArguments();
    if (bundle != null) {
        User user = (User) bundle.getSerializable("user");
        if (user != null) {
            Log.d("User", "User looking"+user.getName());
            user.setInterests(interests);
            bundle.putSerializable("user", user);;
        } else {
            Log.d("User", "No user received");
        }}
});

        return binding.getRoot();
    }
    public List<String> getSelectedInterests() {
        return adapter.getSelectedInterests();
    }
}
