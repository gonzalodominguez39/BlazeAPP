package com.emma.blaze.ui.message;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emma.blaze.R;
import com.emma.blaze.adapters.ChatAdapter;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.databinding.FragmentMessageBinding;
import com.emma.blaze.helpers.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserMessage extends Fragment {

    private String WEBSOCKET_SERVER_URL;
    private UserMessageViewModel userMessageViewModel;
    private FragmentMessageBinding binding;
    private ChatAdapter chatAdapter;
    private UserManager userManager;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        userMessageViewModel = new ViewModelProvider(this).get(UserMessageViewModel.class);
        userManager = UserManager.getInstance();

        chatAdapter = new ChatAdapter(new ArrayList<>(), String.valueOf(userManager.getCurrentUser().getUserId()));
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.messagesRecyclerView.setAdapter(chatAdapter);
        if (getArguments() != null) {
            userMessageViewModel.getUser2Connect().setValue((UserResponse) getArguments().getSerializable("user"));
        }
            userMessageViewModel.getUser2Connect().observe(getViewLifecycleOwner(), user2Connect -> {
                String baseUrl = requireContext().getString(R.string.SERVER_IP);
                binding.username.setText(user2Connect.getName());
                String photoUrl = user2Connect.getPictureUrls().get(0);

                if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
                    photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
                }
                Picasso.get()
                        .load(photoUrl)
                        .placeholder(R.drawable.undo_svg_com)
                        .error(R.drawable.cancel_svg_com)
                        .into(binding.profileImage);
                userMessageViewModel.connectToPrivateChat(String.valueOf(userManager.getCurrentUser().getUserId()), String.valueOf(user2Connect.getUserId()), requireContext());
                binding.sendButton.setOnClickListener(v -> {
                    String message = Objects.requireNonNull(binding.inputMessage.getText()).toString();
                    if (!message.isEmpty()) {
                        userMessageViewModel.sendMessage(String.valueOf(userManager.getCurrentUser().getUserId()), String.valueOf(user2Connect.getUserId()), message);
                        chatAdapter.addMessage(new Message(String.valueOf(userManager.getCurrentUser().getUserId()), String.valueOf(user2Connect.getUserId()), message));
                        binding.inputMessage.setText("");
                    }
                });

            });




        binding.backButton.setOnClickListener(v -> {
            userMessageViewModel.disconnect();
            NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack();
        });

        userMessageViewModel.getChatClient().getMessagesLiveData().observe(getViewLifecycleOwner(), messages -> {
            if (messages != null) {
                Log.d("UserMessage", "Mensajes actualizados: " + messages.size());
                chatAdapter.setMessages(messages);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        userMessageViewModel.disconnect();
        super.onDestroy();
    }
}
