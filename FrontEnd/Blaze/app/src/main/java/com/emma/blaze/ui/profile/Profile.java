package com.emma.blaze.ui.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentProfileBinding;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Profile extends Fragment {

    private ProfileViewModel profileViewModel;
    private UserViewModel userViewModel;
    private FragmentProfileBinding binding;

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        String baseUrl = getString(R.string.SERVER_IP);
        profileViewModel = new ProfileViewModel(requireActivity().getApplication());
        userViewModel = new UserViewModel(requireActivity().getApplication());

        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.getPictureUrls() != null && !user.getPictureUrls().isEmpty()) {
                String photoUrl = user.getPictureUrls().get(0);
                if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://")) {
                } else {
                    photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
                }
                Picasso.get()
                        .load(photoUrl)
                        .placeholder(R.drawable.undo_svg_com)
                        .error(R.drawable.cancel_svg_com)
                        .into(binding.profileImage);

            }
            if (user != null) {
                binding.phoneNumberButton.setText(user.getPhoneNumber());
                binding.userNameAge.setText(user.getName());
                binding.interestButton.setText(user.getGenderInterest());
                binding.lookingFoorButton.setText(user.getRelationshipType());
                binding.discoverSwitch.setChecked(user.getPrivacySetting().equals("PUBLIC"));
                binding.discoverSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            buttonView.setOnClickListener(v -> {
                                showWarningDialog(
                                        "Advertencia",
                                        "Al desactivar esta opción, ya no aparecerás en las búsquedas de otras personas. ¿Deseas continuar?",
                                        "Cancelar",
                                        "Sí, desactivar",
                                        new DialogListener() {
                                            @Override
                                            public void onPositiveButtonClick() {
                                                Objects.requireNonNull(profileViewModel.getUserUpdate().getValue()).setPrivacySetting("PRIVATE");
                                                userViewModel.updateUser(Objects.requireNonNull(profileViewModel.getUserLiveData().getValue()).getUserId(), profileViewModel.getUserUpdate().getValue());
                                            }

                                            @Override
                                            public void onNegativeButtonClick() {
                                                Objects.requireNonNull(profileViewModel.getUserUpdate().getValue()).setPrivacySetting("PUBLIC");
                                                userViewModel.updateUser(user.getUserId(), profileViewModel.getUserUpdate().getValue());
                                                Log.d("profile", "updateUser " + profileViewModel.getUserUpdate().getValue().getPrivacySetting());
                                            }
                                        }
                                );
                            });
                        });

                    binding.removeAccountButton.setOnClickListener(v -> {
                        showWarningDialog(
                                "Advertencia",
                                "¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.",
                                "Cancelar",
                                "Sí, eliminar",
                                new DialogListener() {
                                    @Override
                                    public void onPositiveButtonClick() {
                                        userViewModel.removeAccount();
                                        userViewModel.removeAccount();
                                        NavController navController = Navigation.findNavController(v);

                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.navigation_profile, true)
                                                .build();

                                        navController.navigate(R.id.action_navigation_profile_to_login, null, navOptions);
                                    }

                                    @Override
                                    public void onNegativeButtonClick() {

                                    }
                                }
                        );
                    });


            }
        });


        return binding.getRoot();
    }

    private void navigateScreen(int actionId) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId);
    }

    private void showWarningDialog(String title, String message,String cancelText,String positiveText,DialogListener listener) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(cancelText, (dialog, which) -> {
                    listener.onNegativeButtonClick();
                    dialog.dismiss();
                })
                .setPositiveButton(positiveText, (dialog, which) -> {
                    dialog.dismiss();
                   listener.onPositiveButtonClick();

                })
                .show();
    }

    public interface DialogListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }
}