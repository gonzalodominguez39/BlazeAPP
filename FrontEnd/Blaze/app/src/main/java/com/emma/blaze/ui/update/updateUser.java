package com.emma.blaze.ui.update;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentUpdateUserBinding;
import com.emma.blaze.helpers.UserManager;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;

import java.util.Objects;

public class updateUser extends Fragment {

    private UpdateUserViewModel updateUserViewModel;
    private UserViewModel userViewModel;
    private UserManager userManager;
    private FragmentUpdateUserBinding binding;


    public static updateUser newInstance() {
        return new updateUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false);
        updateUserViewModel = new ViewModelProvider(requireActivity()).get(UpdateUserViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        if(UserManager.getInstance()!=null){
            userManager= UserManager.getInstance();
        }

        binding.updateUserButton.setOnClickListener(v -> {
            updateUserViewModel.getBiography().setValue(binding.etDescription.getText().toString());
            updateUserViewModel.getPhone().setValue(Objects.requireNonNull(binding.phoneNumberInput.getText()).toString());
            User updateUser = new User();
            updateUser.setBiography(updateUserViewModel.getBiography().getValue());
            updateUser.setPhoneNumber(updateUserViewModel.getPhone().getValue());
            updateUser.setRelationshipType(updateUserViewModel.getRelationshipTypeSelected().getValue());
            updateUser.setStatus(true);
            userViewModel.updateUser(userManager.getCurrentUser().getUserId(),updateUser);
            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigateUp();
        });
        updateUserViewModel.getRelationTypeLiveData().observe(getViewLifecycleOwner(), relationTypes -> {
            if (relationTypes != null) {
                setupCategoryDropdown();
            } else {
                Log.e("LookingFoor", "Relation types are null or empty");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateUserViewModel.getBiography().setValue(binding.etDescription.getText().toString());
        updateUserViewModel.getPhone().setValue(Objects.requireNonNull(binding.phoneNumberInput.getText()).toString());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCategoryDropdown() {
        // Asegurarse de que los datos no sean nulos
        String[] relationTypes = updateUserViewModel.getRelationTypeLiveData().getValue();
        if (relationTypes != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    relationTypes
            );

            // Configurar el adaptador en el ExposedDropdown
            binding.exposedDropdownUpdate.setAdapter(adapter);

            // Mostrar el dropdown al tocar el campo
            binding.exposedDropdownUpdate.setOnTouchListener((v, event) -> {
                binding.exposedDropdownUpdate.showDropDown();
                return false; // Permite el evento a continuar con la acción predeterminada
            });

            // Manejo de selección
            binding.exposedDropdownUpdate.setOnItemClickListener((parent, view, position, id) -> {
                String selectedRelationType = relationTypes[position];
                updateUserViewModel.getRelationshipTypeSelected().setValue(selectedRelationType);
                Log.d("LookingFoor", "Selected Relation Type: " + selectedRelationType);
            });
        } else {
            Log.e("LookingFor", "Relation types are null or empty");
        }
    }

    @Override
    public void onDestroy() {
        binding= null;
        super.onDestroy();
    }
}