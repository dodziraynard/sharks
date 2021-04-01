package com.innova.sharks.ui.my_courses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.innova.sharks.adapter.CoursePagedAdapter;
import com.innova.sharks.databinding.FragmentMyCoursesBinding;
import com.innova.sharks.models.Course;
import com.innova.sharks.ui.course.CourseDetailActivity;
import com.innova.sharks.utils.NetworkState;

import static android.content.Context.MODE_PRIVATE;
import static com.innova.sharks.utils.Constants.COURSE_TO_COURSE_DETAILS;
import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.sharks.utils.Constants.STUDENT_LEVEL;

public class MyCoursesFragment extends Fragment {
    private static final String TAG = "MyCoursesFragment";

    private FragmentMyCoursesBinding mBinding;
    private MyCoursesViewModel mViewModel;
    private CoursePagedAdapter mAdapter;

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this,
                new MyCoursesViewModel.MyCoursesViewModelFactory(getActivity().getApplication()))
                .get(MyCoursesViewModel.class);
        mAdapter = new CoursePagedAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMyCoursesBinding.inflate(inflater, container, false);
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
                if (!course.isFavourite()) {
                    View parentLayout = getActivity().findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Remove course from My Courses List?", Snackbar.LENGTH_LONG)
                            .setAction("YES, REMOVE", view -> {
                                mViewModel.deleteCourse(course);
                                mViewModel.invalidate();
                                Toast.makeText(getContext(), "Course Removed.", Toast.LENGTH_SHORT).show();
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                }
            }
        });

        // Load my courses into the database if user is student and registered.
        if (getContext() != null) {
            SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE);
            int level = preferences.getInt(STUDENT_LEVEL, -1);
            if (level > 0) {
                mViewModel.loadMyCourse(level);
            }
        }

        mBinding.recyclerView.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    private void loadCourses() {
        mViewModel.getDBPagedCourses().observe(getViewLifecycleOwner(), pagedList -> {
            mBinding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (pagedList.isEmpty()) {
                mBinding.noItemFound.setVisibility(View.VISIBLE);
            } else {
                mBinding.noItemFound.setVisibility(View.GONE);
            }
            mAdapter.submitList(pagedList);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.setOnItemClickListener(null);
        mBinding.recyclerView.setAdapter(null);
        mBinding = null;
        mViewModel = null;
        mAdapter = null;
    }
}