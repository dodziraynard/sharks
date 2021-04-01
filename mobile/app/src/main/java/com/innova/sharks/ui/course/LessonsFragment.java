package com.innova.sharks.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.sharks.adapter.LessonAdapter;
import com.innova.sharks.databinding.FragmentLessonsBinding;
import com.innova.sharks.models.Course;
import com.innova.sharks.ui.lesson.LessonActivity;
import com.innova.sharks.utils.NetworkState;

import static com.innova.sharks.utils.Constants.LESSON_INTENT_LABEL;

public class LessonsFragment extends Fragment {
    private static final String TAG = "LessonsFragment";
    private static final String COURSE_PARAM = "course-param";
    private LessonsViewModel mViewModel;
    private LessonAdapter mAdapter;
    private Course mCourse;

    public LessonsFragment() {
        // Required empty public constructor
    }

    public static LessonsFragment newInstance(Course course) {
        LessonsFragment fragment = new LessonsFragment();
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
        mViewModel = new ViewModelProvider(this).get(LessonsViewModel.class);
        mAdapter = new LessonAdapter(getContext());

        mAdapter.setOnItemClickListener(lesson -> {
            Intent intent = new Intent(getContext(), LessonActivity.class);
            intent.putExtra(LESSON_INTENT_LABEL, lesson);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLessonsBinding binding = FragmentLessonsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);

        mViewModel.getLessons(mCourse.getId()).observe(getViewLifecycleOwner(), lessons -> {
            binding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (lessons.size() > 0) {
                binding.noItemFound.setVisibility(View.GONE);
            } else {
                binding.noItemFound.setVisibility(View.VISIBLE);
            }
            mAdapter.setData(lessons);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> mViewModel.refreshData(mCourse.getId()));
        return binding.getRoot();
    }
}