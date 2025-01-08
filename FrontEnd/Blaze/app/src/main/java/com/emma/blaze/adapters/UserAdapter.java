package com.emma.blaze.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.response.UserResponse;
import com.emma.blaze.databinding.FragmentSwipeCardsBinding;
import com.squareup.picasso.Picasso;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserResponse> userList;
    private Context context;
    private String baseUrl;

    public UserAdapter(List<UserResponse> userList, Context context) {
        this.userList = userList;
        this.context = context;
        baseUrl = context.getString(R.string.SERVER_IP);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUsers(List<UserResponse> newUserList) {
        this.userList = newUserList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final FragmentSwipeCardsBinding binding;
        private int indexImage = 0;

        public UserViewHolder(@NonNull FragmentSwipeCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserResponse user) {
            binding.userName.setText(user.getName());
            binding.description.setText(user.getEmail());

            binding.userImage.setOnClickListener(v -> {
                Log.d("YourFragment", "Image clicked: " + user.getName());

                if (user.getPictureUrls().size() > indexImage) {
                    indexImage++;
                } else {
                    indexImage = 0;
                }

                String newPhotoUrl = user.getPictureUrls().get(indexImage);
                if (newPhotoUrl.startsWith("http://") || newPhotoUrl.startsWith("https://")) {
                    Log.d("YourFragment", "Using existing full URL: " + newPhotoUrl);
                } else {
                    newPhotoUrl = baseUrl + "api/pictures/photo/" + newPhotoUrl;
                    Log.d("YourFragment", "Constructed new URL: " + newPhotoUrl);
                }
                List<String> pictureUrls = user.getPictureUrls();
                if (pictureUrls != null && !pictureUrls.isEmpty()) {
                    pictureUrls.set(0, newPhotoUrl);
                    user.setPictureUrls(pictureUrls);
                }
                notifyItemChanged(getAdapterPosition());
            });
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
        UserResponse user = userList.get(position);


        String photoUrl = user.getPictureUrls().get(holder.indexImage);
        if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://")) {
            Log.d("user", "onBindViewHolder: " + photoUrl);
        } else {
            photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
            Log.d("user", "onBindViewHolder: " + photoUrl);
        }

        Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.alarm_add_svgrepo_com)
                .error(R.drawable.undo_svg_com)
                .into(holder.binding.userImage);

        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}