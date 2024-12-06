package com.emma.blaze.ui.home;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentSwipeCardsBinding;

public class FragmentSwipeCards extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentSwipeCardsBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ){
       binding= FragmentSwipeCardsBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
       homeViewModel.getHeartColor().observe(getViewLifecycleOwner(), colorResId-> {
           Log.d("HomeViewModel", "Setting heart color: " + colorResId);
           int color = ContextCompat.getColor(requireContext(), colorResId);
           binding.imageheart.setColorFilter(color);
       });

        Log.d("HomeViewModel", "valor de dolor id: "+homeViewModel.getHeartColor());
        return binding.getRoot();
    }


}
