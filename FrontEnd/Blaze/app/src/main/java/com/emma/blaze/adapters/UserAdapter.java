package com.emma.blaze.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databinding.FragmentSwipeCardsBinding;
import com.squareup.picasso.Picasso;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserResponse> userList;
    private final Context context;
    private final String baseUrl;

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

    public UserResponse getUserAtPosition(int swipedPosition) {
        return userList.get(swipedPosition);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final FragmentSwipeCardsBinding binding;
        private int indexImage = 0;

        public UserViewHolder(@NonNull FragmentSwipeCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(UserResponse user) {
            List<String> pictureUrls = user.getPictureUrls();

            binding.userName.setText(user.getName());
            binding.description.setText(user.getEmail());
            binding.textTotalImages.setText(String.valueOf(user.getPictureUrls().size()));
            updateImage(user, pictureUrls);

            binding.userImage.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float touchX = event.getX();
                    float viewWidth = v.getWidth();

                    if (touchX < viewWidth / 2) {
                        retrocederImagen(user, pictureUrls);
                    } else {
                        avanzarImagen(user, pictureUrls);
                    }
                    return true;
                }
                return false;
            });
        }

        private void avanzarImagen(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty()) {
                Log.e("UserAdapter", "No hay imágenes disponibles para: " + user.getName());
                return;
            }

            indexImage = (indexImage + 1) % pictureUrls.size();
            updateImage(user, pictureUrls);
        }

        private void retrocederImagen(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty()) {
                Log.e("UserAdapter", "No hay imágenes disponibles para: " + user.getName());
                return;
            }

            indexImage = (indexImage - 1 + pictureUrls.size()) % pictureUrls.size();
            updateImage(user, pictureUrls);
        }

        private void updateImage(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty()) {
                binding.userImage.setImageResource(R.drawable.profile_24); // Imagen predeterminada
                return;
            }

            String photoUrl = pictureUrls.get(indexImage);

            if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
                photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
            }

            Picasso.get()
                    .load(photoUrl)
                    .placeholder(R.drawable.alarm_add_svgrepo_com)
                    .error(R.drawable.undo_svg_com)
                    .into(binding.userImage);

            binding.textCurrentImage.setText(String.valueOf(indexImage + 1));
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

        Log.d("UserAdapter", "Binding user: " + user.toString());

        if (user.getPictureUrls() != null && !user.getPictureUrls().isEmpty()) {
            String photoUrl = user.getPictureUrls().get(holder.indexImage);

            if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
                photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
            }

            Picasso.get()
                    .load(photoUrl)
                    .placeholder(R.drawable.alarm_add_svgrepo_com)
                    .error(R.drawable.undo_svg_com)
                    .into(holder.binding.userImage);
        } else {
            holder.binding.userImage.setImageResource(R.drawable.profile_24); // Imagen predeterminada
        }

        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}