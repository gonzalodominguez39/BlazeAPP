package com.emma.blaze.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.MainActivity;
import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentLoginBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;
private FirebaseAuth mAuth;
    public static Login newInstance() {
        return new Login();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       binding = FragmentLoginBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
       binding.singInPhone.setOnClickListener(v -> {
           NavController navController = Navigation.findNavController(binding.getRoot());
           navController.navigate(R.id.action_login_to_PhoneCodeSend);
       });
        return binding.getRoot();
    }


}