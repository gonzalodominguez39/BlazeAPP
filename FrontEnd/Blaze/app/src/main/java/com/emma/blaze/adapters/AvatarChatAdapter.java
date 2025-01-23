package com.emma.blaze.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AvatarChatAdapter extends RecyclerView.Adapter<AvatarChatAdapter.AvatarChatViewHolder> {

    private List<UserResponse> userList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UserResponse user);
    }

    public AvatarChatAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvatarChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new AvatarChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarChatViewHolder holder, int position) {
        UserResponse user = userList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserList(List<UserResponse> newUserList) {
        userList.clear();
        if (newUserList != null) {
            userList.addAll(newUserList);
        }
        notifyDataSetChanged();
    }

    public void addUser(UserResponse user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    public static class AvatarChatViewHolder extends RecyclerView.ViewHolder {
        private final ImageView chatAvatar;
        private final TextView chatName;
        private final TextView chatLastMessage;

        public AvatarChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatAvatar = itemView.findViewById(R.id.chat_avatar);
            chatName = itemView.findViewById(R.id.chat_name);
            chatLastMessage = itemView.findViewById(R.id.chat_last_message);
        }

        public void bind(UserResponse user, OnItemClickListener listener) {
            // Bind avatar image
            String avatarUrl = user.getPictureUrls() != null && !user.getPictureUrls().isEmpty()
                    ? user.getPictureUrls().get(0)
                    : null;

            Picasso.get()
                    .load(avatarUrl)
                    .placeholder(R.drawable.undo_svg_com)
                    .error(R.drawable.cancel_svg_com)
                    .into(chatAvatar);


            chatName.setText(user.getName());


            String lastMessage = "ultimo mensaje";
            chatLastMessage.setText(lastMessage);

            // Set click listener
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }
    }
}
