package com.emma.blaze.ui.match;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.adapters.MatchAdapter;
import com.emma.blaze.databinding.FragmentMatchBinding;
import com.emma.blaze.helpers.UserManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Match extends Fragment {

    private MatchViewModel mViewModel;
    private FragmentMatchBinding binding;
    private UserManager userManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMatchBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        userManager = UserManager.getInstance();
        mViewModel.getMatches(userManager.getCurrentUser().getUserId());
        mViewModel.getUserMatches(String.valueOf(userManager.getCurrentUser().getUserId()));

        MatchAdapter adapter = new MatchAdapter(getContext(), new ArrayList<>(),requireContext().getString(R.string.SERVER_IP), user -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user); // Asegúrate de que `UserResponse` implemente Parcelable
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_match_to_message, bundle);
        });
        binding.rvMatches.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMatches.setLayoutManager(layoutManager);
        mViewModel.getUsers().observe(getViewLifecycleOwner(), adapter::updateData);

        return binding.getRoot();
    }

}