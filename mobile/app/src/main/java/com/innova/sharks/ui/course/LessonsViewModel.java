package com.innova.sharks.ui.course;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.LessonsResponse;
import com.innova.sharks.utils.NetworkState;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;


public class LessonsViewModel extends AndroidViewModel {
    private static final String TAG = "LessonsViewModel";
    private final ApiService API_SERVICE;
    private final AppRoomDatabase db;
    private final Application mApplication;
    MutableLiveData<List<Lesson>> mLessons = new MutableLiveData<>();

    public LessonsViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public LiveData<List<Lesson>> getQuizzedLessons() {
        return db.LessonDao().getLessons();
    }

    public LiveData<List<Lesson>> getLessons(long courseId) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getLessons("", courseId).enqueue(new Callback<LessonsResponse>() {
            @Override
            public void onResponse(Call<LessonsResponse> call, Response<LessonsResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mLessons.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<LessonsResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mLessons;
    }

    public void refreshData(long courseId) {
        getLessons(courseId);
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
