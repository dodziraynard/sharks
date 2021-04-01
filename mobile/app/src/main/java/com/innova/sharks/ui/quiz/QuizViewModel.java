package com.innova.sharks.ui.quiz;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.models.Quiz;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.QuizzesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;


public class QuizViewModel extends AndroidViewModel {
    private static final String TAG = "QuizViewModel";
    private final ApiService API_SERVICE;
    private final AppRoomDatabase db;
    MutableLiveData<List<Quiz>> mQuizzes = new MutableLiveData<>();
    private final Application mApplication;


    public QuizViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public LiveData<List<Quiz>> getQuizzes(long lessonsId) {
        API_SERVICE.getQuizzes(lessonsId).enqueue(new Callback<QuizzesResponse>() {
            @Override
            public void onResponse(Call<QuizzesResponse> call, Response<QuizzesResponse> response) {
                if (response.body() != null) {
                    mQuizzes.setValue(response.body().getResults());
                } else {
                    Toast.makeText(mApplication, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    mQuizzes.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<QuizzesResponse> call, Throwable t) {
                mQuizzes.setValue(new ArrayList<>());
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return mQuizzes;
    }

    public void insertLesson(Lesson lesson) {
        databaseWriteExecutor.execute(() -> {
            db.LessonDao().insertLesson(lesson);
        });
    }

    public void deleteLesson(Lesson lesson) {
        databaseWriteExecutor.execute(() -> {
            db.LessonDao().deleteLesson(lesson);
        });
    }
}
