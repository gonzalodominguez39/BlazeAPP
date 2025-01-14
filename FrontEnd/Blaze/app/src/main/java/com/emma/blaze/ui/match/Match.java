package com.emma.blaze.ui.match;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentMatchBinding;

public class Match extends Fragment {

    private MatchViewModel mViewModel;
    private FragmentMatchBinding binding;

    public static Match newInstance() {
        return new Match();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentMatchBinding.inflate(inflater,container,false);
            return binding.getRoot();
    }

}