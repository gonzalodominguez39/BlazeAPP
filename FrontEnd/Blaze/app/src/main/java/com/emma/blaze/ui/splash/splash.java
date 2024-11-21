package com.emma.blaze.ui.splash;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentSplashBinding;


public class splash extends Fragment {

 private FragmentSplashBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSplashBinding.inflate(inflater, container, false);
        new Handler().postDelayed(() -> {
            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.action_splash_to_login, null, new  NavOptions.Builder().setPopUpTo(R.id.splash, true).build());
        }, 2000);
        return binding.getRoot();
    }
}