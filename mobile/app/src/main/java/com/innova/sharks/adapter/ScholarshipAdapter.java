package com.innova.sharks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemScholarshipBinding;
import com.innova.sharks.models.Scholarship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Scholarship> scholarships;

    private OnItemClickListener listener;

    public ScholarshipAdapter(Context context) {
        mContext = context;
        scholarships = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Scholarship> scholarships) {
        this.scholarships = scholarships;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScholarshipBinding binding = ItemScholarshipBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScholarshipAdapter.ViewHolder holder, int position) {
        Scholarship scholarship = scholarships.get(position);
        holder.binding.title.setText(scholarship.getTitle());
        holder.binding.description.setText(scholarship.getDescription());
        holder.binding.location.setText(scholarship.getLocation());

        holder.binding.opens.setText(formatDate(scholarship.getOpens()));
        holder.binding.closes.setText(formatDate(scholarship.getCloses()));

        Glide.with(mContext)
                .load(scholarship.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_favorite_border_24)
                .into(holder.binding.imageIcon);
    }

    private String formatDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return scholarships.size();
    }

    public interface OnItemClickListener {
        void setOnScholarshipCardClickListener(Scholarship scholarship);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemScholarshipBinding binding;

        public ViewHolder(@NonNull ItemScholarshipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.card.setOnClickListener(view -> {
                if (listener != null)
                    listener.setOnScholarshipCardClickListener(scholarships.get(getLayoutPosition()));
            });
        }
    }
}
