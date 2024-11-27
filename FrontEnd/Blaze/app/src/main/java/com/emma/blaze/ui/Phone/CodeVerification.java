package com.emma.blaze.ui.Phone;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.emma.blaze.databinding.FragmentCodeVerificationBinding;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class CodeVerification extends Fragment {
private FragmentCodeVerificationBinding binding;
private CodePhoneViewModel codePhoneViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        return binding.getRoot();
    }

    private void verificateCode() {
        String code = Objects.requireNonNull(binding.editCodeNumber.getText()).toString();
        codePhoneViewModel.verifyCode(code, task -> {
            if (task.isSuccessful()) {
                AuthResult authResult = task.getResult();
                FirebaseUser user = authResult.getUser();
                Log.d("Auth", "Usuario verificado: " + user.getPhoneNumber());
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Log.e("Auth", "Error durante la verificación: " + e.getMessage());
                }
            }
        });
    }
}