package com.emma.blaze.ui.phone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentPhoneCodeSendBinding;

import java.util.Objects;



public class PhoneCodeSend extends Fragment {

    private String phoneNumber;
    private FragmentPhoneCodeSendBinding binding;
    private CodePhoneViewModel codePhoneViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhoneCodeSendBinding.inflate(inflater, container, false);
        codePhoneViewModel = new ViewModelProvider(requireActivity()).get(CodePhoneViewModel.class);

        binding.sendButton.setOnClickListener(v -> sendCode());

        binding.phoneNumberInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.phoneNumberLayout.setHintEnabled(false);
                binding.phoneNumberLayout.setHint(null);
                binding.phoneNumberInput.setHint(null);
            } else {
                binding.phoneNumberLayout.setHintEnabled(true);
                binding.phoneNumberLayout.setHint("@string/phoneHint");
            }
        });

        return binding.getRoot();
    }

    private void sendCode() {
        phoneNumber = Objects.requireNonNull(binding.phoneNumberInput.getText()).toString();
        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            Toast.makeText(requireContext(), "Ingresa un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
        codePhoneViewModel.getPhoneNumberLiveData().setValue(phoneNumber);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.sendButton.setEnabled(false);

        new Handler().postDelayed(() -> {
            binding.progressBar.setVisibility(View.GONE);


            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.action_PhoneCodeSend_to_PhoneCodeVerification, null,
                    new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeSend, true).build());
        }, 2000);
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}