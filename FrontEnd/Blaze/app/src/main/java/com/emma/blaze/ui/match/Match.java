package com.emma.blaze.ui.match;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.adapters.ChatListAdapter;
import com.emma.blaze.adapters.MatchAdapter;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databinding.FragmentMatchBinding;
import com.emma.blaze.helpers.UserManager;
import java.util.ArrayList;


public class Match extends Fragment {

    private MatchViewModel mViewModel;
    private FragmentMatchBinding binding;
    private MatchAdapter matchAdapter;
    private ChatListAdapter chatListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMatchBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(MatchViewModel.class);


        setupRecyclerViewMatch();
        setupRecyclerViewChat();
        fetchData();
        mViewModel.getUsers().observe(getViewLifecycleOwner(), usersList -> {
            if(usersList.isEmpty()|| usersList ==null){
                binding.tvNoMatches.setVisibility(View.VISIBLE);
            }else{
                binding.tvNoMatches.setVisibility(View.GONE);
            }
            mViewModel.getLastMessages().observe(getViewLifecycleOwner(), messagesList -> {
                mViewModel.filterUsersWithMessages();
            });
        });



        return binding.getRoot();
    }

    private void setupRecyclerViewMatch() {
        matchAdapter = new MatchAdapter(
                getContext(),
                new ArrayList<>(),
                requireContext().getString(R.string.SERVER_IP),
                this::navigateToMessages
        );

        binding.rvMatches.setAdapter(matchAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.rvMatches.setLayoutManager(layoutManager);

        mViewModel.getUsers().observe(getViewLifecycleOwner(), matchAdapter::updateData);
    }

    public void setupRecyclerViewChat() {
        chatListAdapter = new ChatListAdapter(
                new ArrayList<>(), new ArrayList<>(), requireContext().getString(R.string.SERVER_IP)
                , this::navigateToMessages
        );
        binding.rvChats.setAdapter(chatListAdapter);
        binding.rvChats.setLayoutManager(new LinearLayoutManager(requireContext()));
        mViewModel.getUsersChats().observe(getViewLifecycleOwner(), usersChats -> {
            if(usersChats.isEmpty()|| usersChats ==null){
                binding.tvNoChats.setVisibility(View.VISIBLE);
            }else{
                binding.tvNoChats.setVisibility(View.GONE);
            }
            mViewModel.getLastMessages().observe(getViewLifecycleOwner(), lastMessages -> {
                chatListAdapter.updateData(usersChats, lastMessages);
            });
        });
    }

    private void fetchData() {
        UserManager.getInstance().getCurrentUserLiveData().observe(getViewLifecycleOwner(), user -> {
           if (user!=null) {
               Long currentUserId = user.getUserId();
               mViewModel.getMatches(currentUserId);
           }
        });
    }

    private void navigateToMessages(UserResponse user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_match_to_message, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
