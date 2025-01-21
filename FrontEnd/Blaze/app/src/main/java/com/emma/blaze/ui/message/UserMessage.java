package com.emma.blaze.ui.message;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.adapters.ChatAdapter;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.databinding.FragmentMessageBinding;
import com.emma.blaze.helpers.UserManager;

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
        chatAdapter = new ChatAdapter(new ArrayList<>(), "2");
        List<Message> testMessages = new ArrayList<>();
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        testMessages.add(new Message("1", "2","hola")); // Asume que "1" es el ID del remitente
        testMessages.add(new Message("2", "1","cpomo estas"));
        testMessages.add(new Message("1", "2","tt"));
        chatAdapter.setMessages(testMessages);

     binding.messagesRecyclerView.setAdapter(chatAdapter);


        userMessageViewModel.connectToPrivateChat("2", "1", requireContext());

    /*    binding.sendButton.setOnClickListener(v -> {
            String message = Objects.requireNonNull(binding.inputMessage.getText()).toString();
            if (!message.isEmpty()) {
                userMessageViewModel.sendMessage("2", "1", message);
                binding.inputMessage.setText("");
            }
        });
*/

        userMessageViewModel.getChatClient().getMessagesLiveData().observe(getViewLifecycleOwner(), messages -> {
            if (messages != null) {
                Log.d("UserMessage", "Mensajes actualizados: " + messages.size());
                requireActivity().runOnUiThread(() -> { // ¡IMPRESCINDIBLE! Ejecutar en el hilo principal
                    chatAdapter.setMessages(messages); // O chatAdapter.notifyDataSetChanged() si usas addMessage
                });
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
