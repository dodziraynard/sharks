package com.innova.sharks.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.innova.sharks.R;
import com.innova.sharks.databinding.FragmentCourseDescriptionBinding;
import com.innova.sharks.models.Course;


public class DescriptionFragment extends Fragment {
    private static final String COURSE_PARAM = "param1";

    private Course mCourse;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(Course course) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(COURSE_PARAM, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getParcelable(COURSE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCourseDescriptionBinding binding = FragmentCourseDescriptionBinding.inflate(inflater, container, false);

        binding.title.setText(mCourse.getTitle());
        binding.description.setText(mCourse.getLongDescription());
        binding.level.setText(String.valueOf(mCourse.getLevel()));

        GlideUrl url = new GlideUrl(mCourse.getImage(), new LazyHeaders.Builder()
                .addHeader("User-Agent", "Sharks-App")
                .build());
        Glide.with(getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.boy)
                .into(binding.image);
        return binding.getRoot();
    }
}