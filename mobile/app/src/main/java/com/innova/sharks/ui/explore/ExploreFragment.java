package com.innova.sharks.ui.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.innova.sharks.adapter.CourseLoadStateAdapter;
import com.innova.sharks.adapter.CoursePagedAdapter;
import com.innova.sharks.databinding.FragmentExploreBinding;
import com.innova.sharks.models.Course;
import com.innova.sharks.ui.course.CourseDetailActivity;
import com.innova.sharks.ui.my_courses.MyCoursesViewModel;
import com.innova.sharks.utils.NetworkState;

import static com.innova.sharks.utils.Constants.COURSE_TO_COURSE_DETAILS;

public class ExploreFragment extends Fragment {
    private static final String TAG = "ExploreFragment";
    private FragmentExploreBinding mBinding;
    private MyCoursesViewModel mViewModel;
    private CoursePagedAdapter mAdapter;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this,
                new MyCoursesViewModel.MyCoursesViewModelFactory(getActivity().getApplication()))
                .get(MyCoursesViewModel.class);
        mAdapter = new CoursePagedAdapter(getContext());
        mAdapter.withLoadStateFooter(new CourseLoadStateAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentExploreBinding.inflate(inflater, container, false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCourses();
        mBinding.swipeRefresh.setOnRefreshListener(this::loadCourses);

        mAdapter.setOnItemClickListener(new CoursePagedAdapter.OnItemClickListener() {
            @Override
            public void setOnCourseCardClickListener(Course course) {
                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                intent.putExtra(COURSE_TO_COURSE_DETAILS, course);
                startActivity(intent);
            }

            @Override
            public void setOnFavouriteCourseClickListener(Course course) {
                if (course.isFavourite()) {
                    mViewModel.insertCourse(course);
                    View parentLayout = getActivity().findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Course added to list.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.searchBtn.setOnClickListener(view -> {
            hideKeyboardFrom();
            loadCourses();
        });
        return mBinding.getRoot();
    }

    private void loadCourses() {
        String newText = mBinding.search.getText().toString();
        mViewModel.getPagedFilteredCourses(newText).observe(getViewLifecycleOwner(), courses -> {
            mBinding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (courses.size() < 1) {
                mBinding.noItemFound.setVisibility(View.VISIBLE);
            } else {
                mBinding.noItemFound.setVisibility(View.GONE);
            }
            mAdapter.submitList(courses);
        });
    }

    public void hideKeyboardFrom() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        mViewModel = null;
        mAdapter = null;
    }
}