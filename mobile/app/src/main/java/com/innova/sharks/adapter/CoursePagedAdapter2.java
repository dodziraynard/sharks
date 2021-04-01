package com.innova.sharks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.sharks.databinding.ItemCourseBinding;
import com.innova.sharks.models.Course;


/**
 * this adapter displays coupon items in recycler view
 * it extends PagedListAdapter which gets data from PagedList
 * and displays in recycler view as data is available in PagedList
 */
public class CoursePagedAdapter2 extends PagingDataAdapter<Course, CoursePagedAdapter2.CourseViewHolder> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int COURSE_ITEM = 1;
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

    private OnItemClickListener listener;

    public CoursePagedAdapter2() {
        super(DIFF_CALLBACK);
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
            if (listener != null) {
                Course c = getItem(position);
                if (c == null) return;
                c.setFavourite(!c.isFavourite());
                listener.setOnFavouriteCourseClickListener(c);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? COURSE_ITEM : LOADING_ITEM;
    }

    public interface OnItemClickListener {
        void setOnCourseCardClickListener(Course course);

        void setOnFavouriteCourseClickListener(Course course);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
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
            if (course.isFavourite()) {
                //TODO: Change color
            }
            if (course.getWebsite() != null && course.getWebsite().isEmpty()) {
                mBinding.moreBtn.setVisibility(View.GONE);
            } else {
                mBinding.moreBtn.setVisibility(View.VISIBLE);
            }
        }
    }
}
