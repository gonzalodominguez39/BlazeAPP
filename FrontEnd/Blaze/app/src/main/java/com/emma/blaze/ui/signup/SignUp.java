package com.emma.blaze.ui.signup;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUp extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel signUpViewModel;

    public static SignUp newInstance() {
        return new SignUp();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpViewModel.getmAuth().observe(getViewLifecycleOwner(), auth -> {
            if (auth.getCurrentUser() != null) {
                signUpViewModel.setUser();
            }
        });

        signUpViewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.Name.setText(name);
        });

        signUpViewModel.getLastName().observe(getViewLifecycleOwner(), lastName -> {
            binding.surname.setText(lastName);
        });
        signUpViewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            binding.email.setText(email);
        });


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

        binding.email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUpViewModel.validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }


}