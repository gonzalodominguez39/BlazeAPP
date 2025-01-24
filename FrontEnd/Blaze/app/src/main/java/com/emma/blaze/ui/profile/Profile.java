package com.emma.blaze.ui.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentProfileBinding;
import com.emma.blaze.helpers.UserManager;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Profile extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        String baseUrl = getString(R.string.SERVER_IP);
        profileViewModel=new ProfileViewModel(requireActivity().getApplication());

       profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
      if(user!=null && user.getPictureUrls()!=null && !user.getPictureUrls().isEmpty()) {
          String photoUrl = user.getPictureUrls().get(0);
          if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://")) {
          } else {
              photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
          }

          if (photoUrl != null && !photoUrl.isEmpty()) {
              Picasso.get()
                      .load(photoUrl)
                      .placeholder(R.drawable.undo_svg_com)
                      .error(R.drawable.cancel_svg_com)
                      .into(binding.profileImage);
          } else {
              binding.profileImage.setImageResource(R.drawable.profile_24);
          }
          binding.phoneNumberButton.setText(user.getPhoneNumber());
          binding.userNameAge.setText(user.getName());
          binding.interestButton.setText(user.getGenderInterest());

      } });
        binding.removeAccountButton.setOnClickListener(v -> {
                    navigateScreen(R.id.action_navigation_profile_to_login);
                }
        );
        return binding.getRoot();
    }

    private void navigateScreen(int actionId) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId);
    }


}