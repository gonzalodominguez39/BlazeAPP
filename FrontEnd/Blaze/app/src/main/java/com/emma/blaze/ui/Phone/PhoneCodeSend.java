package com.emma.blaze.ui.Phone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentPhoneCodeSendBinding;

import java.util.Objects;


public class PhoneCodeSend extends Fragment {


    private FragmentPhoneCodeSendBinding binding;
private ViewModelCodeSend viewModelCodeSend;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhoneCodeSendBinding.inflate(inflater, container, false);
        viewModelCodeSend = new ViewModelProvider(requireActivity()).get(ViewModelCodeSend.class);


        binding.sendButton.setOnClickListener(v -> sendCode());

       viewModelCodeSend.isCodeSent().observe(getViewLifecycleOwner(), isCodeSent -> {
           if (isCodeSent) {
               NavController navController = Navigation.findNavController(binding.getRoot());
               navController.navigate(R.id.action_PhoneCodeSend_to_PhoneCodeVerification, null, new NavOptions.Builder().setPopUpTo(R.id.PhoneCodeSend, true).build());
           }
       });

       viewModelCodeSend.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
           Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
           binding.progressBar.setVisibility(View.GONE);
           binding.sendButton.setEnabled(true);
       });

        binding.phoneNumberInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
               binding.phoneNumberLayout.setHintEnabled(false);
            } else {
                binding.phoneNumberLayout.setHintEnabled(true);
                binding.phoneNumberLayout.setHint("@string/phoneHint");
            }
        });
        return binding.getRoot();
    }

private void sendCode() {
    String phoneNumber = Objects.requireNonNull(binding.phoneNumberInput.getText()).toString();
    viewModelCodeSend.setPhoneNumber(phoneNumber);
    if (phoneNumber.isEmpty()) {
        Toast.makeText(requireContext(), "Ingresa un número válido", Toast.LENGTH_SHORT).show();
        return;
    }
    binding.progressBar.setVisibility(View.VISIBLE);
    binding.sendButton.setEnabled(false);
    viewModelCodeSend.startPhoneVerification(requireActivity(),phoneNumber);
}

}