package com.emma.blaze.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.emma.blaze.R;
import com.emma.blaze.data.dto.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private final Context context;
    private final List<UserResponse> users;
    private final String baseUrl;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UserResponse user);
    }

    public MatchAdapter(Context context, List<UserResponse> users, String baseUrl, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.baseUrl = baseUrl;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        if (users.get(position).getPictureUrls() == null ||users.get(position).getPictureUrls().isEmpty()) {
            holder.ivMatch.setImageResource(R.drawable.profile_24);
            return;
        }
        String photoUrl = users.get(position).getPictureUrls().get(0);

        if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
            photoUrl = baseUrl + "api/pictures/photo/" + photoUrl;
        }
        holder.ivMatch.setOnClickListener(v -> {
            listener.onItemClick(users.get(position));
        });

        Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.undo_svg_com)
                .error(R.drawable.cancel_svg_com)
                .into(holder.ivMatch);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<UserResponse> newUsers) {
        this.users.clear();
        if (newUsers != null) {
            this.users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMatch;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMatch = itemView.findViewById(R.id.iv_match);
        }
    }
}
