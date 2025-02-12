package com.emma.blaze.adapters;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.blaze.R;
import com.emma.blaze.data.model.Message;


import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private  List<Message> messages;
    private final String currentUserId;

    public ChatAdapter(List<Message> messages, String currentUserId) {

        this.messages = messages;
        this.currentUserId = currentUserId;
        for (Message message:messages){
            Log.d("message", "ChatAdapter: "+message);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        Log.d("message", "getItemViewType: "+message.getMessage());
        if (message.getSenderId() == null || currentUserId == null) {
            Log.e("ChatAdapter", "Sender ID or Current User ID is null!");
        }
        return message.getSenderId().equals(currentUserId)  ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("messge", "viewType: "+viewType);
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_send, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_recivied, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position); // Asegúrate de que "position" sea válido
        Log.d("ChatAdapter", "onBindViewHolder: Posición " + position + ", Mensaje: " + message.getMessage());
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("ChatAdapter", "getItemCount: " + messages.size());
        return messages.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<Message> newMessages) {
        this.messages.clear();
        this.messages.addAll(newMessages);
        Log.d("ChatAdapter", "setMessages: Mensajes actualizados, total: " + this.messages.size());
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMessages(List<Message> newMessages) {
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            int position = messages.size();
            messages.add(message);
            notifyItemInserted(position);
            Log.d("ChatAdapter", "Message added: " + message.getMessage());
        }
    }
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }

        public void bind(Message message) {
            textMessage.setText(message.getMessage());

        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessageReceived);
        }

        public void bind(Message message) {
            textMessage.setText(message.getMessage());
        }
    }

    public List<Message> getMessages(){
        return this.messages;
    }
}
