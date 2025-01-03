package com.emma.blaze.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentSwipeCardsBinding;



import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;
    private  Context context;

    public UserAdapter(List<User> userList,Context  context) {
        this.userList = userList;
        this.context = context;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final FragmentSwipeCardsBinding binding;

        public UserViewHolder(@NonNull FragmentSwipeCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {

            binding.userName.setText(user.getName());
            binding.description.setText(user.getEmail());


        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       FragmentSwipeCardsBinding binding = FragmentSwipeCardsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
      //  int drawableId = getDrawableIdByName(user.getProfilePicture());
        //   holder.binding.userImage.setImageResource(drawableId);
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @SuppressLint("DiscouragedApi")
    private int getDrawableIdByName(String imageName) {
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (resourceId == 0) {
            Log.d("CardStackAdapter", "Resource not found for image name: " + imageName);
        }
        return resourceId;
    }



}
