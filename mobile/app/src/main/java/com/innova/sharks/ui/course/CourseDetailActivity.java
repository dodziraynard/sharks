package com.innova.sharks.ui.course;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.innova.sharks.adapter.TabAdapter;
import com.innova.sharks.databinding.ActivityCourseDetailBinding;
import com.innova.sharks.models.Course;

import static com.innova.sharks.utils.Constants.COURSE_TO_COURSE_DETAILS;

public class CourseDetailActivity extends AppCompatActivity {
    private ActivityCourseDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        Course course = getIntent().getParcelableExtra(COURSE_TO_COURSE_DETAILS);
        setTitle(course.getTitle());


        // Setting up tab layout.
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(DescriptionFragment.newInstance(course), "Description");
        adapter.addFragment(LessonsFragment.newInstance(course), "Lessons");
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}