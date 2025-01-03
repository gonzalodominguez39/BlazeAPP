package com.emma.blaze.ui.signup;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.emma.blaze.R;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentSignUpBinding;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.Objects;
import java.util.TimeZone;


public class SignUp extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel signUpViewModel;
    private String email;
    private boolean isBhirtdayPickerVisible;
    private UserViewModel userViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_aires"));

        signUpViewModel.getmAuth().observe(getViewLifecycleOwner(), auth -> {
            if (auth.getCurrentUser() != null && signUpViewModel.getSetUser().getValue() == Boolean.FALSE) {
                signUpViewModel.setUser();
                signUpViewModel.getSetUser().setValue(true);
            }
        });


        signUpViewModel.getName().observe(getViewLifecycleOwner(), name -> binding.Name.setText(name));

        signUpViewModel.getLastName().observe(getViewLifecycleOwner(), lastName -> binding.lastName.setText(lastName));
        signUpViewModel.getEmail().observe(getViewLifecycleOwner(), email -> binding.email.setText(email));
        signUpViewModel.getBirthDate().observe(getViewLifecycleOwner(), birthDate -> binding.birthday.setText(birthDate));

        signUpViewModel.getIsEmailValid().observe(getViewLifecycleOwner(), isValid -> {
            binding.email.setError(isValid ? null : getString(R.string.invalid_email));

            if (isValid) {
                binding.layoutEmail.setEndIconDrawable(R.drawable.check_24);
                binding.layoutEmail.setEndIconTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.black_opacity)
                );

            } else {
                binding.layoutEmail.setEndIconTintList(
                        ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
                );
            }
        });
        binding.birthday.setOnClickListener(v -> {
            binding.birthday.setFocusable(false);
            if (!isBhirtdayPickerVisible) {
                isBhirtdayPickerVisible = true;

                MaterialDatePicker<Long> bhirtdayPicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_birthday)
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                bhirtdayPicker.show(getParentFragmentManager(), "END_DATE_PICKER");

                bhirtdayPicker.addOnPositiveButtonClickListener(selection -> {
                    String date = SignUpViewModel.longToDateString(selection);
                    signUpViewModel.getBirthDate().setValue(date);
                    isBhirtdayPickerVisible = false;
                });

                bhirtdayPicker.addOnDismissListener(dialog -> isBhirtdayPickerVisible = false);
            }
        });
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                Log.d("Radio", "No option selected");
            } else if (checkedId == R.id.radioMale) {
                signUpViewModel.getGender().setValue("MALE");
            } else if (checkedId == R.id.radioFemale) {
                signUpViewModel.getGender().setValue("FEMALE");
            } else if (checkedId == R.id.notSpecified) {
                signUpViewModel.getGender().setValue("NOT SPECIFIED");}
        });

        signUpViewModel.getGender().observe(getViewLifecycleOwner(), gender -> {
            if (gender.equals("MALE")) {
                binding.radioMale.setChecked(true);
            } else if (gender.equals("FEMALE")) {
                binding.radioFemale.setChecked(true);
            } else if (gender.equals("NOT SPECIFIED")) {
                binding.notSpecified.setChecked(true);
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            signUpViewModel.getName().setValue(Objects.requireNonNull(binding.Name.getText()).toString());
            signUpViewModel.getLastName().setValue(Objects.requireNonNull(binding.lastName.getText()).toString());
            signUpViewModel.getPassword().setValue(Objects.requireNonNull(binding.editPassword.getText()).toString());
            signUpViewModel.getConfirmPassword().setValue(Objects.requireNonNull(binding.confirmPassword.getText()).toString());
            signUpViewModel.getEmail().setValue(email);
            if (signUpViewModel.getIsEmailValid().getValue() == Boolean.TRUE && signUpViewModel.matchPasswords() && signUpViewModel.validateForm()) {
                User user = signUpViewModel.createUser();
                userViewModel.getUserLiveData().setValue(user);
                navigateScreen(R.id.action_signUp_to_lookingFoor);
            } else if (signUpViewModel.getIsEmailValid().getValue() == Boolean.FALSE) {
                Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            } else if (!signUpViewModel.matchPasswords()) {
                Toast.makeText(getContext(), R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            }

        });

        binding.email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUpViewModel.validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = s.toString();
            }
        });


        return binding.getRoot();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        signUpViewModel.getName().setValue(Objects.requireNonNull(binding.Name.getText()).toString());
        signUpViewModel.getLastName().setValue(Objects.requireNonNull(binding.lastName.getText()).toString());
        signUpViewModel.getEmail().setValue(Objects.requireNonNull(binding.email.getText()).toString());
       signUpViewModel.getPassword().setValue( Objects.requireNonNull(binding.editPassword.getText()).toString());
        signUpViewModel.getConfirmPassword().setValue(Objects.requireNonNull(binding.confirmPassword.getText()).toString());

    }

    private void navigateScreen(int actionId ){
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId);
    }
}



