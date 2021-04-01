package com.innova.sharks.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.sharks.R;
import com.innova.sharks.adapter.LessonAdapter;
import com.innova.sharks.databinding.ActivityMyQuizzesBinding;
import com.innova.sharks.ui.course.LessonsViewModel;
import com.innova.sharks.ui.lesson.LessonActivity;

import static com.innova.sharks.utils.Constants.LESSON_INTENT_LABEL;

public class MyQuizzesActivity extends AppCompatActivity {

    private LessonAdapter mAdapter;
    private LessonsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMyQuizzesBinding binding = ActivityMyQuizzesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.quiz_lessons));

        mViewModel = new ViewModelProvider(this).get(LessonsViewModel.class);
        mAdapter = new LessonAdapter(this);

        mAdapter.setOnItemClickListener(lesson -> {
            Intent intent = new Intent(this, LessonActivity.class);
            intent.putExtra(LESSON_INTENT_LABEL, lesson);
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);

        mViewModel.getQuizzedLessons().observe(this, lessons -> {
            if (lessons.size() > 0) {
                binding.noItemFound.setVisibility(View.GONE);
                mAdapter.setData(lessons);
            } else {
                binding.noItemFound.setVisibility(View.VISIBLE);
            }
        });
    }
}