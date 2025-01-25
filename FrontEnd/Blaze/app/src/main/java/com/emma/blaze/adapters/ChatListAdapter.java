package com.emma.blaze.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private final List<UserResponse> users;
    private final String baseUrl;
    private List<Message> lastMessages;
    private final OnItemClickListener listener;

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<UserResponse> newUsers, List<Message> lastMessages) {
        this.users.clear();
        if (newUsers != null) {
            this.users.addAll(newUsers);
        }
        if (lastMessages != null) {
            this.lastMessages = lastMessages;
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onChatClick(UserResponse user);
    }

    public ChatListAdapter(List<UserResponse> users, List<Message> lastMessages, String baseUrl, OnItemClickListener listener) {
        this.users = users;
        this.baseUrl = baseUrl;
        this.listener = listener;
        this.lastMessages = lastMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        UserResponse user = users.get(position);
        holder.bind(user, lastMessages, baseUrl, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatar;
        private final TextView name;
        private final TextView lastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.chat_avatar);
            name = itemView.findViewById(R.id.chat_name);
            lastMessage = itemView.findViewById(R.id.chat_last_message);
        }

        public void bind(UserResponse user, List<Message> lastMessages, String baseUrl, OnItemClickListener listener) {


            name.setText(user.getName());
            String lastMessageText = "No messages";
            if (lastMessages != null && !lastMessages.isEmpty()) {
                for (Message message : lastMessages) {
                    if (message.getSenderId().equals(String.valueOf(user.getUserId())) || message.getReceiverId().equals(String.valueOf(user.getUserId()))) {
                        lastMessageText = message.getMessage();
                        break;
                    }
                }
            }
            lastMessage.setText(lastMessageText);

            String photoUrl = null;
            if (user.getPictureUrls() != null && !user.getPictureUrls().isEmpty()) {
                photoUrl = baseUrl + "api/pictures/photo/" + user.getPictureUrls().get(0);

            }else if(user.getPictureUrls() == null || user.getPictureUrls().isEmpty()) {
             avatar.setImageResource(R.drawable.profile_24);
            }

            if (photoUrl != null) {
                Picasso.get()
                        .load(photoUrl)
                        .placeholder(R.drawable.undo_svg_com)
                        .error(R.drawable.cancel_svg_com)
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.cancel_svg_com);             }

            itemView.setOnClickListener(v -> listener.onChatClick(user));
        }
    }
}
