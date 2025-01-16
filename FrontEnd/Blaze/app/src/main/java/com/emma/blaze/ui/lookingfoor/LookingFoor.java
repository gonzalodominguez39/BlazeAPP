package com.emma.blaze.ui.lookingfoor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.emma.blaze.R;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentLookingFoorBinding;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LookingFoor extends Fragment {
    private FragmentLookingFoorBinding binding;
    private LookingFoorViewModel LFViewModel;
    private UserViewModel userViewModel;
    private RadioGroup.OnCheckedChangeListener group1Listener;
    private RadioGroup.OnCheckedChangeListener group2Listener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLookingFoorBinding.inflate(inflater, container, false);
        LFViewModel = new ViewModelProvider(this).get(LookingFoorViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        group1Listener = (group, checkedId) -> {
            if (checkedId != -1) {
                binding.radioGroup2.setOnCheckedChangeListener(null);
                binding.radioGroup2.clearCheck();
                binding.radioGroup2.setOnCheckedChangeListener(group2Listener);

                if (checkedId == R.id.radioButtonMale) {
                    LFViewModel.getGenderInterest().setValue("MALE");
                } else if (checkedId == R.id.radioButtonFemale) {
                    LFViewModel.getGenderInterest().setValue("FEMALE");
                }
            }
        };

        group2Listener = (group, checkedId) -> {
            if (checkedId != -1) {
                binding.radioGroup1.setOnCheckedChangeListener(null);
                binding.radioGroup1.clearCheck();
                binding.radioGroup1.setOnCheckedChangeListener(group1Listener);

                if (checkedId == R.id.radioButtonNoSpecified) {
                    LFViewModel.getGenderInterest().setValue("NOT_SPECIFIED");
                } else if (checkedId == R.id.radioButtonBoth) {
                    LFViewModel.getGenderInterest().setValue("ALL");
                }
            }
        };

        binding.radioGroup1.setOnCheckedChangeListener(group1Listener);
        binding.radioGroup2.setOnCheckedChangeListener(group2Listener);

        LFViewModel.getGenderInterest().observe(getViewLifecycleOwner(), gender -> {
            Log.d("gender", "Gender: " + gender);
            if (gender.equals("MALE")) {
                binding.radioButtonMale.setChecked(true);
            } else if (gender.equals("FEMALE")) {
                binding.radioButtonFemale.setChecked(true);
            } else if (gender.equals("NOT_SPECIFIED")) {
                binding.radioButtonNoSpecified.setChecked(true);
            } else if (gender.equals("ALL")) {
                binding.radioButtonBoth.setChecked(true);
            }
        });

        binding.buttonNextLooking.setOnClickListener(v -> {
            User user = userViewModel.getUserLiveData().getValue();
            assert user != null;
            user.setGenderInterest(LFViewModel.getGenderInterest().getValue());
            user.setRelationshipType(LFViewModel.getSelectedRelationType().getValue());
            userViewModel.getUserLiveData().setValue(user);
            navigateScreen(R.id.action_lookingFoor_to_interests);
        });

        LFViewModel.getRelationTypeLiveData().observe(getViewLifecycleOwner(), relationTypes -> {
            if (relationTypes != null) {
                setupCategoryDropdown();
            } else {
                Log.e("LookingFoor", "Relation types are null or empty");
            }
        });
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCategoryDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                Objects.requireNonNull(LFViewModel.getRelationTypeLiveData().getValue())
        );

        binding.exposedDropdown.setAdapter(adapter);
        binding.exposedDropdown.setOnTouchListener((v, event) -> {
            binding.exposedDropdown.showDropDown();
            return true;
        });

        binding.exposedDropdown.setOnItemClickListener((parent, view, position, id) -> {
            if (LFViewModel.getRelationTypeLiveData().getValue()[position].equals("Amigos")) {
                LFViewModel.getSelectedRelationType().setValue("FRIENDS");
            } else if (LFViewModel.getRelationTypeLiveData().getValue()[position].equals("Casual")) {
                LFViewModel.getSelectedRelationType().setValue("CASUAL");
            } else if (LFViewModel.getRelationTypeLiveData().getValue()[position].equals("Formal")) {
                LFViewModel.getSelectedRelationType().setValue("FORMAL");
            } else if (LFViewModel.getRelationTypeLiveData().getValue()[position].equals("Otro")) {
                LFViewModel.getSelectedRelationType().setValue("OTHER");
            }
            Log.d("LookingFoor", "Selected Relation Type: " + LFViewModel.getSelectedRelationType().getValue());


        });
    }

    private void navigateScreen(int actionId) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId, null, new NavOptions.Builder().setPopUpTo(R.id.lookingFoor, true).build());
    }
}
