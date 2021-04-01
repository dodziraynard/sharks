package com.innova.sharks.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.innova.sharks.R;
import com.innova.sharks.models.Quiz;
import com.innova.sharks.utils.CustomCountDownTimer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.innova.sharks.utils.DummyData.getDummyQuizzes;

public class QuizView extends ConstraintLayout {
    private static final String TAG = "QuizView";
    List<Quiz> mQuizzes;
    CustomCountDownTimer.TimerTickListener mTimerTickListener;
    private Context mContext;
    private Drawable mOptionsBackground;
    private Drawable mQuestionBackground;
    private float mQuestionTextSize = 15;
    private int mScoreIncrement;
    private int mQuestionTextColor;
    private int mOptionsTextColor;
    private float mOptionsTextSize = 15;
    private float mDisplayDensity;
    private int nextIndex = 0;
    private int currentScore = 0;
    private boolean gameOver = false;
    private EventListener listener;
    private CustomCountDownTimer mCountDownTimer;

    public QuizView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QuizView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QuizView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public void addEventListener(EventListener listener) {
        this.listener = listener;
    }

    public void addCountDownTimerListener(CustomCountDownTimer.TimerTickListener listener) {
        this.mTimerTickListener = listener;
        mCountDownTimer = new CustomCountDownTimer(5000, 1000, mTimerTickListener);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        mDisplayDensity = dm.density;

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.QuizView, defStyle, 0);

        mScoreIncrement = a.getInt(R.styleable.QuizView_scoreIncrement, 5);
        mQuestionTextSize = a.getDimension(R.styleable.QuizView_questionTextSize, 15f * mDisplayDensity);
        mOptionsTextSize = a.getDimension(R.styleable.QuizView_optionsTextSize, 15f * mDisplayDensity);
        mQuestionTextColor = a.getColor(R.styleable.QuizView_questionTextColor, Color.BLACK);
        mOptionsTextColor = a.getColor(R.styleable.QuizView_optionsTextColor, Color.BLACK);

        if (a.hasValue(R.styleable.QuizView_questionTextBackground)) {
            mQuestionBackground = a.getDrawable(R.styleable.QuizView_questionTextBackground);
            mQuestionBackground.setCallback(this);
        }

        if (a.hasValue(R.styleable.QuizView_optionsBackground)) {
            mOptionsBackground = a.getDrawable(R.styleable.QuizView_optionsBackground);
            mOptionsBackground.setCallback(this);
        }
        a.recycle();

        setId(R.id.quiz_view);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        if (isInEditMode()) {
            mQuizzes = getDummyQuizzes();
            startGame();
        }
    }

    public void addQuestions(List<Quiz> quizzes) {
        mQuizzes = quizzes;
    }

    public void startGame() {
        if (mCountDownTimer != null) {
            Quiz quiz = mQuizzes.get(nextIndex);
            mCountDownTimer.startFrom(1000 * (quiz.getDuration() + 1));
        }
        currentScore = 0;
        nextQuestion();
    }

    public void endGame() {
        gameOver = true;
        if (listener != null) {
            if (mCountDownTimer != null)
                mCountDownTimer.cancel();
            listener.onGameOver(currentScore, mQuizzes.size() * mScoreIncrement);
        }
    }

    public void cancelGame() {
        gameOver = true;
        if (listener != null) {
            if (mCountDownTimer != null)
                mCountDownTimer.cancel();
        }
    }

    private void nextQuestion() {
        if (gameOver) return;
        if (nextIndex >= mQuizzes.size()) {
            endGame();
        } else {
            Quiz quiz = mQuizzes.get(nextIndex);
            displayQuiz(quiz);
            if (mCountDownTimer != null) {
                mCountDownTimer.restartFrom(1000 * (quiz.getDuration() + 1));
            }
        }

        if (listener != null) {
            int progress = (int) ((float) nextIndex / (float) mQuizzes.size() * 100);
            listener.onProgressChange(progress);
        }
        nextIndex++;
    }

    private void displayQuiz(Quiz quiz) {
        removeAllViews();

        TextView questionTextView = new TextView(mContext);
        questionTextView.setId(View.generateViewId());
        questionTextView.setTextSize(mQuestionTextSize);
        questionTextView.setPadding((int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity));
        questionTextView.setText(quiz.getQuestion());
        questionTextView.setTextColor(mQuestionTextColor);
        questionTextView.setBackground(mQuestionBackground);

        // Add view to the layout.
        addView(questionTextView, new ConstraintLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT));

        // Display Options
        String[] options = quiz.getOptions().split("\n");
        // Randomizing options
        List<String> strList = Arrays.asList(options);
        Collections.shuffle(strList);
        String[] finalOptions = strList.toArray(new String[strList.size()]);

        String correctOption = quiz.getCorrectOption().trim();

        GridView gridView = new GridView(mContext);
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing(15);
        gridView.setHorizontalSpacing(15);
        gridView.setId(View.generateViewId());
        QuizAdapter quizAdapter = new QuizAdapter(mContext, options);
        gridView.setAdapter(quizAdapter);

        // Add view to the layout.
        addView(gridView, new LinearLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT));

        // Option selection listener.
        gridView.setOnItemClickListener((view, view1, i, l) -> {
            if (gameOver) return;
            String chosen = finalOptions[i];
            boolean correct = false;
            if (chosen.trim().equals(correctOption)) {
                updateScore();
                correct = true;
            }
            if (listener != null)
                listener.onAnswerChosen(correct);
            nextQuestion();
        });

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        // Constraint Options Text
        constraintSet.connect(gridView.getId(), ConstraintSet.BOTTOM, getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.connect(gridView.getId(), ConstraintSet.END, getId(), ConstraintSet.END, 0);
        constraintSet.connect(gridView.getId(), ConstraintSet.START, getId(), ConstraintSet.START, 0);
        constraintSet.connect(gridView.getId(), ConstraintSet.TOP, questionTextView.getId(), ConstraintSet.BOTTOM, 0);

        // Constraint Question Text
        constraintSet.connect(questionTextView.getId(), ConstraintSet.END, getId(), ConstraintSet.END, 0);
        constraintSet.connect(questionTextView.getId(), ConstraintSet.START, getId(), ConstraintSet.START, 0);
        constraintSet.connect(questionTextView.getId(), ConstraintSet.TOP, getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(questionTextView.getId(), ConstraintSet.BOTTOM, gridView.getId(), ConstraintSet.TOP, 20);
        constraintSet.setHorizontalBias(questionTextView.getId(), 0.5f);
        constraintSet.applyTo(this);
    }

    private void updateScore() {
        currentScore += mScoreIncrement;
        if (listener != null)
            listener.onScoreChange(currentScore, mQuizzes.size() * mScoreIncrement);
    }

    public interface EventListener {
        void onScoreChange(int score, int total);

        void onProgressChange(int progress);

        void onGameOver(int score, int total);

        void onAnswerChosen(boolean correct);
    }

    public class QuizAdapter extends BaseAdapter {
        Context context;
        LayoutInflater mInflater;
        String[] mOptions;

        public QuizAdapter(Context applicationContext, String[] options) {
            this.context = applicationContext;
            this.mOptions = options;
            mInflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return mOptions.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            String option = mOptions[position];

            TextView textView = new TextView(getContext());
            textView.setTextSize(mOptionsTextSize);
            textView.setPadding((int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity), (int) (10 * mDisplayDensity));
            textView.setTextColor(mOptionsTextColor);
            textView.setText(option);
            textView.setBackgroundResource(R.drawable.quiz_option_background);
            return textView;
        }
    }

}