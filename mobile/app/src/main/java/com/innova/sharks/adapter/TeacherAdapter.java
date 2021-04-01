package com.innova.sharks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemTeacherBinding;
import com.innova.sharks.models.Teacher;

import java.util.ArrayList;
import java.util.List;


/**
 * this adapter displays coupon items in recycler view
 * it extends PagedListAdapter which gets data from PagedList
 * and displays in recycler view as data is available in PagedList
 */
public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    private static final String TAG = "TeacherAdapter";

    private final Context mContext;
    private OnItemClickListener listener;
    private List<Teacher> mTeachers = new ArrayList<>();


    public TeacherAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Teacher> teachers) {
        mTeachers = teachers;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTeacherBinding binding = ItemTeacherBinding.inflate(layoutInflater, parent, false);
        return new TeacherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = mTeachers.get(position);
        if (teacher != null)
            holder.bindTo(teacher);

        holder.mBinding.card.setOnClickListener(view -> {
            if (listener != null)
                listener.setOnTeacherCardClickListener(mTeachers.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return mTeachers.size();
    }

    public interface OnItemClickListener {
        void setOnTeacherCardClickListener(Teacher teacher);
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder {
        ItemTeacherBinding mBinding;

        public TeacherViewHolder(ItemTeacherBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindTo(Teacher teacher) {
            // Update views
            mBinding.name.setText(teacher.getName());
            mBinding.about.setText(teacher.getAbout());
            mBinding.header.setText(teacher.getHeader());
            mBinding.comments.setText(mContext.getString(R.string.comments, teacher.getReviews()));
            mBinding.rating.setText(mContext.getString(R.string.rating, teacher.getRating()));

            Glide.with(mContext)
                    .load(teacher.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(mBinding.profileImage);
        }
    }
}
