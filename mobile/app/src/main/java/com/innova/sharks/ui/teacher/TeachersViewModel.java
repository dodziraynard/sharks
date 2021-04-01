package com.innova.sharks.ui.teacher;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Teacher;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.TeachersResponse;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;


public class TeachersViewModel extends AndroidViewModel {
    private static final String TAG = "TeachersViewModel";
    private final AppRoomDatabase db;
    private final ApiService API_SERVICE;
    private final Application mApplication;
    MutableLiveData<List<Teacher>> mTeachers = new MutableLiveData<>();

    public TeachersViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public LiveData<List<Teacher>> getTeachers(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getTeachers(query).enqueue(new Callback<TeachersResponse>() {
            @Override
            public void onResponse(Call<TeachersResponse> call, Response<TeachersResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mTeachers.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TeachersResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                mTeachers.setValue(new ArrayList<>());
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mTeachers;
    }

    public void refreshData(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getTeachers(query).enqueue(new Callback<TeachersResponse>() {
            @Override
            public void onResponse(Call<TeachersResponse> call, Response<TeachersResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mTeachers.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TeachersResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                mTeachers.setValue(new ArrayList<>());
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void insertTeacher(Teacher lesson) {
        databaseWriteExecutor.execute(() -> {
            db.TeacherDao().insertTeacher(lesson);
        });
    }

    public void deleteTeacher(Teacher lesson) {
        databaseWriteExecutor.execute(() -> {
            db.TeacherDao().deleteTeacher(lesson);
        });
    }
}
