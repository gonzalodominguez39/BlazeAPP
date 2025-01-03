package com.emma.blaze.ui.phone;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentCodeVerificationBinding;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class CodeVerification extends Fragment {
private FragmentCodeVerificationBinding binding;
private CodePhoneViewModel codePhoneViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding= FragmentCodeVerificationBinding.inflate(inflater, container, false);
      codePhoneViewModel = new ViewModelProvider(requireActivity()).get(CodePhoneViewModel.class);
        binding.verificationButton.setOnClickListener(v -> verificateCode());

        binding.phoneCodeLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.phoneCodeLayout.setHintEnabled(false);
                binding.phoneCodeLayout.setHint(null);
                binding.editCodeNumber.setHint(null);
            } else {
                binding.editCodeNumber.setHint("@string/codeHint");
            }
        });
        binding.btnResendSms.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        return binding.getRoot();
    }

    private void verificateCode() {
        String code = Objects.requireNonNull(binding.editCodeNumber.getText()).toString();
        codePhoneViewModel.verifyCode(code, task -> {
            if (task.isSuccessful()) {
                AuthResult authResult = task.getResult();
                FirebaseUser user = authResult.getUser();
               Navigation.findNavController(binding.getRoot()).navigate(R.id.action_PhoneCodeVerification_to_lookingFoor);
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

