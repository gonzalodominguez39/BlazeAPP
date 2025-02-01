package com.emma.blaze.ui.phone;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentCodeVerificationBinding;

import com.emma.blaze.ui.sharedViewModel.UserViewModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class CodeVerification extends Fragment {
    private FragmentCodeVerificationBinding binding;
    private UserViewModel userViewModel;
    private CodePhoneViewModel codePhoneViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeVerificationBinding.inflate(inflater, container, false);
        codePhoneViewModel = new ViewModelProvider(requireActivity()).get(CodePhoneViewModel.class);
        binding.verificationButton.setOnClickListener(v -> verificateCode());
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.phoneCodeLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.phoneCodeLayout.setHintEnabled(false);
                binding.phoneCodeLayout.setHint(null);
                binding.editCodeNumber.setHint(null);
            } else {
                binding.editCodeNumber.setHint("@string/codeHint");
            }
        });

        return binding.getRoot();
    }

    private void verificateCode() {
        codePhoneViewModel.getPhoneNumberLiveData().observe(getViewLifecycleOwner(), phoneNumber -> {

            userViewModel.loginWithPhone(phoneNumber, null, new UserViewModel.LoginCallback() {
                @Override
                public void onLoginSuccess(boolean userExists, UserResponse userResponse) {
                    if (userExists) {
                        // Ahora tienes userResponse disponible aquí si lo necesitas
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_PhoneCodeVerification_to_home, null, new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeVerification, true).build());
                    } else {
                        User user = userViewModel.getUserLiveData().getValue();
                        if (user == null) user = new User();
                        user.setPhoneNumber(phoneNumber); //phoneNumber ya está disponible aquí
                        userViewModel.getUserLiveData().setValue(user);
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_PhoneCodeVerification_to_signUp, null, new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeVerification, true).build());
                    }
                }

                @Override
                public void onLoginError(String errorMessage) {
                    // Manejar el error, por ejemplo, mostrar un Toast
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}

