package com.innova.sharks.ui.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.innova.sharks.databinding.ActivityQuizOverBinding;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.ui.MainActivity;

import static com.innova.sharks.utils.Constants.QUIZ_LESSON;

public class QuizOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityQuizOverBinding binding = ActivityQuizOverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        Lesson lesson = getIntent().getParcelableExtra(QUIZ_LESSON);
        binding.tvScore.setText(score + "/" + total);

        //TODO: Write lesson quizzes to database.
        binding.restart.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(QUIZ_LESSON, lesson);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(intent);
            finish();
        });

        binding.home.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}