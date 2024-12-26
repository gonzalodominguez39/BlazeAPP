package com.emma.blaze.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.emma.blaze.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    private  List<String> interests;
    private final List<String> selectedInterests = new ArrayList<>();

    public InterestAdapter(List<String> interests) {
        this.interests = interests;
    }



    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_button_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String interest = interests.get(position);
        holder.interestButton.setText(interest);

        holder.itemView.setOnClickListener(v -> {
            if (selectedInterests.contains(interest)) {
                selectedInterests.remove(interest);
                holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rounded_background));

            } else {
                selectedInterests.add(interest);
                holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rounded_background_selected));
                Log.d("interest", "onBindViewHolder: "+selectedInterests);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public List<String> getSelectedInterests() {
        return selectedInterests;
    }

    static class InterestViewHolder extends RecyclerView.ViewHolder {
        TextView interestButton;

        InterestViewHolder(View itemView) {
            super(itemView);
            interestButton = itemView.findViewById(R.id.buttonInterest);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateInterests(List<String> newInterests) {
        this.interests = newInterests;
        notifyDataSetChanged();
    }
}
