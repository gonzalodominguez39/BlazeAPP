package com.emma.blaze.ui.interest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.adapters.InterestAdapter;
import com.emma.blaze.databinding.FragmentButtonInterestBinding;
import com.emma.blaze.databinding.FragmentInterestBinding;
import com.emma.blaze.helpers.GridSpacingItemDecoration;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;


public class Interest extends Fragment {
    private RecyclerView recyclerView;
    private FragmentInterestBinding binding;
    private InterestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= FragmentInterestBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerViewInterests;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true));


        List<String> interests = Arrays.asList("Sports", "Music", "Travel", "Food", "Tech", "Books", "Art", "Fitness","Sports", "Music", "Travel", "Food", "Tech", "Books", "Art", "Fitness");
        adapter = new InterestAdapter(interests);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    public List<String> getSelectedInterests() {
        return adapter.getSelectedInterests();
    }
}
