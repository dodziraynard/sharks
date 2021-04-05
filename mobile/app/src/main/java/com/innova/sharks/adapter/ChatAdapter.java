package com.innova.sharks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemChatBinding;
import com.innova.sharks.models.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Chat> chats;

    public ChatAdapter(Context context) {
        mContext = context;
        chats = new ArrayList<>();
    }

    public void setData(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.binding.chatText.setText(chat.getMessage());
        holder.binding.username.setText(chat.getUsername());
        holder.binding.timetamp.setText(formatDate(chat.getTimestamp()));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.container.getLayoutParams();
        if (Objects.equals(chat.getUsername(), "dodzi")) {
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            params.startToStart = ConstraintLayout.LayoutParams.UNSET;
            holder.binding.container.setBackgroundResource(R.drawable.background_outgoing_chat_bubble);
        } else {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            holder.binding.container.setBackgroundResource(R.drawable.background_incoming_chat_bubble);
        }

        holder.binding.container.setLayoutParams(params);
    }

    private String formatDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBinding binding;

        public ViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
