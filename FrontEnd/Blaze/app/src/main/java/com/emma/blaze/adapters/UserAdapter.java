package com.emma.blaze.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databinding.FragmentSwipeCardsBinding;
import com.emma.blaze.helpers.UserFunctions;
import com.emma.blaze.helpers.UserManager;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserResponse> userList;
    private UserManager userManager;
    private final Context context;
    private final String baseUrl;

    public UserAdapter(List<UserResponse> userList, Context context) {
        if (UserManager.getInstance() != null) {
            this.userManager = UserManager.getInstance();
        }
        this.userList = userList != null ? userList : new ArrayList<>();
        this.context = context;
        baseUrl = context.getString(R.string.SERVER_IP);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUsers(List<UserResponse> newUserList) {
        this.userList = newUserList != null ? newUserList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public UserResponse getUserAtPosition(int swipedPosition) {
        return (userList != null && swipedPosition >= 0 && swipedPosition < userList.size()) ? userList.get(swipedPosition) : null;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final FragmentSwipeCardsBinding binding;
        private int indexImage = 0;

        public UserViewHolder(@NonNull FragmentSwipeCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        public void bind(UserResponse user) {
            if (user == null || userManager == null || userManager.getCurrentUser() == null) {
                return;
            }

            List<String> pictureUrls = user.getPictureUrls();
            binding.userName.setText(user.getName() != null ? user.getName()+ ", "+UserFunctions.calcularEdad(user.getBirthdate()) : "");
            binding.description.setText(user.getEmail() != null ? user.getEmail() : "");
            binding.textTotalImages.setText(pictureUrls != null ? String.valueOf(pictureUrls.size()) : "0");

            if (user.getLocation() != null && userManager.getCurrentUser().getLocation() != null) {
                double distance = calculateDistance(user.getLocation().getLatitude(), user.getLocation().getLongitude(),
                        userManager.getCurrentUser().getLocation().getLatitude(), userManager.getCurrentUser().getLocation().getLongitude());
                binding.textViewUbication.setText(distance < 5D ? "muy cerca" : "a más de 5 km");
            } else {
                binding.textViewUbication.setText("Ubicación desconocida");
            }

            updateImage(user, pictureUrls);

            binding.userImage.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float touchX = event.getX();
                    float viewWidth = v.getWidth();

                    if (touchX < viewWidth / 2) {
                        retrocederImagen(user, pictureUrls);
                        binding.description.setText("Relacion: "+user.getRelationshipType());
                    } else {
                        avanzarImagen(user, pictureUrls);
                        binding.description.setText(user.getBiography() != null ? user.getBiography() :
                                "Intereses: " + user.getInterests().stream()
                                        .limit(3)
                                        .collect(Collectors.joining(", ")));
                    }
                    return true;
                }
                return false;
            });
        }

        private void avanzarImagen(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty()||pictureUrls.size()==1) {
                return;
            }
            indexImage = (indexImage + 1) % pictureUrls.size();
            updateImage(user, pictureUrls);
        }

        private void retrocederImagen(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty() || pictureUrls.size() == 1) {
                return;
            }
            indexImage = (indexImage - 1 + pictureUrls.size()) % pictureUrls.size();
            updateImage(user, pictureUrls);
        }

        private void updateImage(UserResponse user, List<String> pictureUrls) {
            if (pictureUrls == null || pictureUrls.isEmpty()) {
                binding.userImage.setImageResource(R.drawable.profile_24);
                return;
            }

            if (indexImage >= pictureUrls.size()) {
                indexImage = 0;
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
        if (userList == null || position < 0 || position >= userList.size()) {
            return;
        }

        UserResponse user = userList.get(position);
        if (user == null) {
            return;
        }

        holder.bind(user);
    }

    public double calculateDistance(double userLat, double userLng, double managerLat, double managerLng) {
        final int EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(managerLat - userLat);
        double lngDistance = Math.toRadians(managerLng - userLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(managerLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }
}
