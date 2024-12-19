package com.emma.blaze.ui.lookingfoor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.emma.blaze.R;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentLookingFoorBinding;

public class LookingFoor extends Fragment {
    private FragmentLookingFoorBinding binding;
    private LookingFoorViewModel LFViewModel;

    private RadioGroup.OnCheckedChangeListener group1Listener;
    private RadioGroup.OnCheckedChangeListener group2Listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLookingFoorBinding.inflate(inflater, container, false);
        LFViewModel = new ViewModelProvider(this).get(LookingFoorViewModel.class);

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
                    LFViewModel.getGenderInterest().setValue("NOT SPECIFIED");
                } else if (checkedId == R.id.radioButtonBoth) {
                    LFViewModel.getGenderInterest().setValue("BOTH");
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
            } else if (gender.equals("NOT SPECIFIED")) {
                binding.radioButtonNoSpecified.setChecked(true);
            } else if (gender.equals("BOTH")) {
                binding.radioButtonBoth.setChecked(true);
            }
        });

binding.buttonNextLooking.setOnClickListener(v -> {
    Bundle bundle = getArguments();
    if (bundle != null) {
        User user = (User) bundle.getSerializable("user");
        if (user != null) {
            Log.d("User", "User looking"+user.getName());
            user.setGenderInterest(LFViewModel.getGenderInterest().getValue());
            bundle.putSerializable("user", user);
            navigateScreen(R.id.action_lookingFoor_to_interest,bundle);
        } else {
            Log.d("User", "No user received");
        }
    }});


        return binding.getRoot();
    }
    private void navigateScreen(int actionId,Bundle bundle) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId,bundle);
    }

}
