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
        String code = Objects.requireNonNull(binding.editCodeNumber.getText()).toString();
        codePhoneViewModel.verifyCode(code, task -> {
            if (task.isSuccessful()) {
                userViewModel.loginWithPhone(codePhoneViewModel.getPhoneNumberLiveData().getValue());
                userViewModel.getIsLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
                    if (isLoggedIn) {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_PhoneCodeVerification_to_home, null, new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeVerification, true).build());
                    }else{
                        Log.d("phonenumber", "verificateCode: " + codePhoneViewModel.getPhoneNumberLiveData().getValue());
                        User user = userViewModel.getUserLiveData().getValue();
                        if(user == null) user = new User();
                        user.setPhoneNumber(codePhoneViewModel.getPhoneNumberLiveData().getValue());
                        userViewModel.getUserLiveData().setValue(user);
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_PhoneCodeVerification_to_signUp, null, new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeVerification, true).build());
                    }
                });
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Toast.makeText(getContext(), "Error en la verificacion", Toast.LENGTH_SHORT).show();
                    iniciarContador();

                }
            }
        });
    }

    private void iniciarContador() {

        binding.btnResendSms.setEnabled(false);
        binding.tvTimer.setVisibility(View.VISIBLE);

        CountDownTimer start = new CountDownTimer(30000, 1000) { // 30 segundos, intervalo de 1 segundo
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText("Reenviar en " + millisUntilFinished / 1000 + " segundos");
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                binding.btnResendSms.setEnabled(true);
                binding.btnResendSms.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black_opacity))
                );
                binding.btnResendSms.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white_opacity)));
                binding.tvTimer.setVisibility(View.GONE);
            }

        }.start();
    }


}

