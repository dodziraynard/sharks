package com.innova.sharks.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemCourseBinding;
import com.innova.sharks.models.Course;


/**
 * this adapter displays coupon items in recycler view
 * it extends PagedListAdapter which gets data from PagedList
 * and displays in recycler view as data is available in PagedList
 */
public class CoursePagedAdapter extends PagedListAdapter<Course, CoursePagedAdapter.CourseViewHolder> {
    private static final String TAG = "CoursePagedAdapter";

    //DiffUtil is used to find out whether two object in the list are same or not
    public static DiffUtil.ItemCallback<Course> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Course>() {
                @Override
                public boolean areItemsTheSame(@NonNull Course oldCourse,
                                               @NonNull Course newCourse) {
                    return oldCourse.getId() == newCourse.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Course oldCourse,
                                                  @NonNull Course newCourse) {
                    return oldCourse.equals(newCourse);
                }
            };
    private final Context mContext;

    private OnItemClickListener listener;

    public CoursePagedAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCourseBinding binding = ItemCourseBinding.inflate(layoutInflater, parent, false);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = getItem(position);
        if (course != null)
            holder.bindTo(course);

        holder.mBinding.card.setOnClickListener(view -> {
            if (listener != null)
                listener.setOnCourseCardClickListener(getItem(position));
        });
        holder.mBinding.favButton.setOnClickListener(view -> {
            if (listener != null && position < getItemCount()) {
                Course c = getItem(position);
                c.setFavourite(!c.isFavourite());
                listener.setOnFavouriteCourseClickListener(c);
            }
        });
    }

    public interface OnItemClickListener {
        void setOnCourseCardClickListener(Course course);

        void setOnFavouriteCourseClickListener(Course course);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        ItemCourseBinding mBinding;

        public CourseViewHolder(ItemCourseBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindTo(Course course) {
            // Update views
            mBinding.description.setText(course.getLongDescription());
            mBinding.title.setText(course.getTitle());
            mBinding.date.setText(course.getDate());

            if (!course.getImage().isEmpty()) {
                GlideUrl url = new GlideUrl(course.getImage(), new LazyHeaders.Builder()
                        .addHeader("User-Agent", "Sharks-App")
                        .build());
                Glide.with(mContext)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.mipmap.boy)
                        .into(mBinding.image);
            }
            if (course.isFavourite()) {
                mBinding.favButton.setColorFilter(ContextCompat.getColor(mContext, R.color.orange_800), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                mBinding.favButton.setColorFilter(Color.argb(255, 255, 255, 255));
            }

            if (course.getWebsite() == null || course.getWebsite().isEmpty()) {
                mBinding.moreBtn.setVisibility(View.GONE);
            } else {
                mBinding.moreBtn.setVisibility(View.VISIBLE);
                mBinding.moreBtn.setOnClickListener(view -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(course.getWebsite()));
                    mContext.startActivity(browserIntent);
                });
            }
        }
    }
}
