package com.emma.blaze;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.emma.blaze.databinding.ActivityMainBinding;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            int[] visibleDestinations = {
                  R.id.home
            };

            boolean isVisible = Arrays.stream(visibleDestinations).anyMatch(id -> id == destination.getId());

            binding.bottomNavigation.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        });
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);


    }


}
