package com.innova.sharks.ui.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.innova.sharks.R;
import com.innova.sharks.databinding.ActivityQuizBinding;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.models.Quiz;
import com.innova.sharks.utils.CustomCountDownTimer;
import com.innova.sharks.views.QuizView;

import java.util.List;

import static com.innova.sharks.utils.Constants.QUIZ_LESSON;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private ActivityQuizBinding mBinding;
    private List<Quiz> mQuizzes;
    private boolean isLoading = true;
    private QuizViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        Lesson lesson = getIntent().getParcelableExtra(QUIZ_LESSON);
        mBinding.progressText.setText(0 + "%");
        mBinding.progressBar.setProgress(0);
        mBinding.score.setText(String.valueOf(0));

        mBinding.loadingProgressBar.setVisibility(View.VISIBLE);
        if (lesson != null) {
            mViewModel.getQuizzes(lesson.getId()).observe(this, quizzes -> {
                mBinding.loadingProgressBar.setVisibility(View.GONE);

                mQuizzes = quizzes;
                isLoading = false;
                if (quizzes.size() > 0) {
                    mBinding.quizView.addQuestions(mQuizzes);
                    showDialog();
                }
            });
        } else {
            Toast.makeText(this, "Lesson and quizzes not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mBinding.quizView.addEventListener(new QuizView.EventListener() {
            @Override
            public void onScoreChange(int score, int total) {
                mBinding.score.setText(String.valueOf(score));
            }

            @Override
            public void onProgressChange(int progress) {
                mBinding.progressText.setText(progress + "%");
                mBinding.progressBar.setProgress(progress);
            }

            @Override
            public void onGameOver(int score, int total) {
                Intent intent = new Intent(QuizActivity.this, QuizOverActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("score", score);
                intent.putExtra("total", total);
                intent.putExtra(QUIZ_LESSON, lesson);
                lesson.setQuizScore(score + "/" + total);
                mViewModel.insertLesson(lesson);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnswerChosen(boolean correct) {
                if (correct) {
                    Toast.makeText(QuizActivity.this, "Correct.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuizActivity.this, "Not correct.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.quizView.addCountDownTimerListener(new CustomCountDownTimer.TimerTickListener() {
            @Override
            public void onTick(long millisLeft) {
                int minutes = (int) millisLeft / (60 * 1000);
                int seconds = (int) (millisLeft / 1000) % 60;
                String m = String.format("%02d", minutes);
                String s = String.format("%02d", seconds);
                mBinding.timer.setText(m + ":" + s);
            }

            @Override
            public void onFinish() {
                mBinding.quizView.endGame();
            }

            @Override
            public void onCancel() {
            }
        });

        mBinding.close.setOnClickListener(view -> {
            onBackPressed();
            mBinding.quizView.cancelGame();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.quizView.cancelGame();
    }

    private void showDialog() {
        Dialog filterDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.quiz_start_dialog, null);

        final Button start = rootView.findViewById(R.id.start_button);
        final Button nope = rootView.findViewById(R.id.nope_button);
        final TextView infoText = rootView.findViewById(R.id.quiz_info);
        infoText.setText(getString(R.string.quiz_start_info, mQuizzes.size(), mQuizzes.get(0).getDuration()));

        builder.setView(rootView);
        builder.setCancelable(false);

        filterDialog = builder.create();
        start.setOnClickListener(view -> {
            if (!isLoading) {
                filterDialog.dismiss();
                mBinding.quizView.startGame();
            } else {
                Toast.makeText(this, "Loading ... Please wait.", Toast.LENGTH_SHORT).show();
            }
        });
        nope.setOnClickListener(view -> onBackPressed());

        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        Window window = filterDialog.getWindow();
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp2.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp2);
        Window dialogWindow = filterDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL);

//        filterDialog.getWindow().setWindowAnimations(R.style.CustomDialogAnimation);
        filterDialog.show();
    }
}