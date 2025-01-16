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
            binding.textCurrentImage.setText(String.valueOf(indexImage + 1));
            binding.textTotalImages.setText(String.valueOf(user.getPictureUrls().size()));
            binding.userImage.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        float touchX = event.getX();
                        float viewWidth = v.getWidth();
                        if (touchX < viewWidth / 2) {
                            retrocederImagen();
                        } else {
                            avanzarImagen();
                        }

                        return true;
                    }
                    return false;
                }

                private void avanzarImagen() {
                    Log.d("YourFragment", "Avanzando imagen: " + user.getName());


                    if (pictureUrls == null || pictureUrls.isEmpty()) {
                        Log.e("YourFragment", "No hay URLs de imágenes disponibles para el usuario: " + user.getName());
                        return;
                    }

                    indexImage = (indexImage + 1) % pictureUrls.size();

                    String newPhotoUrl = pictureUrls.get(indexImage);


                    if (!newPhotoUrl.startsWith("http://") && !newPhotoUrl.startsWith("https://")) {
                        newPhotoUrl = baseUrl + "api/pictures/photo/" + newPhotoUrl;
                    }

                    pictureUrls.set(indexImage, newPhotoUrl);
                    user.setPictureUrls(pictureUrls);


                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        binding.userImage.setImageDrawable(null);
                        binding.textCurrentImage.setText(String.valueOf(indexImage + 1));
                        Picasso.get().load(newPhotoUrl).into(binding.userImage); //
                    } else {
                        Log.e("YourFragment", "Posición de adaptador inválida para el usuario: " + user.getName());
                    }
                }


                private void retrocederImagen() {
                    Log.d("YourFragment", "Retrocediendo imagen: " + user.getName());

                    if (pictureUrls == null || pictureUrls.isEmpty()) {
                        Log.e("YourFragment", "No hay URLs de imágenes disponibles para el usuario: " + user.getName());
                        return;
                    }

                    indexImage = (indexImage - 1 + pictureUrls.size()) % pictureUrls.size();

                    String newPhotoUrl = pictureUrls.get(indexImage);

                    if (!newPhotoUrl.startsWith("http://") && !newPhotoUrl.startsWith("https://")) {
                        newPhotoUrl = baseUrl + "api/pictures/photo/" + newPhotoUrl;
                    }

                    pictureUrls.set(indexImage, newPhotoUrl);
                    user.setPictureUrls(pictureUrls);

                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        binding.userImage.setImageDrawable(null);
                        binding.textCurrentImage.setText(String.valueOf(indexImage + 1));
                        Picasso.get().load(newPhotoUrl).into(binding.userImage);
                    } else {
                        Log.e("YourFragment", "Posición de adaptador inválida para el usuario: " + user.getName());
                    }
                }
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
        Log.d("user", "onBindViewHolder: "+user.toString());

        String photoUrl = user.getPictureUrls().get(holder.indexImage);
        if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://")) {
        } else {
            photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
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