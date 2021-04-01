package com.innova.sharks.ui.start;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Student;
import com.innova.sharks.models.Teacher;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.AuthenticationResponse;
import com.innova.sharks.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;
import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.sharks.utils.Constants.USER_ID;
import static com.innova.sharks.utils.Constants.USER_TOKEN;


public class StartViewModel extends AndroidViewModel {
    private static final String TAG = "StartViewModel";
    private final AppRoomDatabase db;
    private final ApiService API_SERVICE;
    private final Application mContext;

    private final MutableLiveData<Teacher> mTeacher = new MutableLiveData<>();
    private final MutableLiveData<Student> mStudent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoggedIn = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorMessage = new MutableLiveData<>();

    public StartViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    private void register(String username, String password, boolean isTeacher) {
        API_SERVICE.register(username, password, isTeacher).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                SharedPreferences.Editor prefsEditor = mContext.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).edit();
                if (response.body() != null) {
                    if (response.body().getErrorMessage() != null) {
                        mErrorMessage.setValue(response.body().getErrorMessage());
                    }
                    if (response.body().getToken() != null) {
                        prefsEditor.putString(USER_TOKEN, response.body().getToken());
                        prefsEditor.apply();
                    }
                    if (response.body().getStudent() != null) {
                        mStudent.setValue(response.body().getStudent());
                        prefsEditor.putLong(USER_ID, response.body().getStudent().getId());
                        prefsEditor.apply();
                        mIsLoggedIn.setValue(true);
                        insertStudent(response.body().getStudent());
                    }
                    if (response.body().getTeacher() != null) {
                        mTeacher.setValue(response.body().getTeacher());
                        prefsEditor.putLong(USER_ID, response.body().getTeacher().getId());
                        prefsEditor.apply();
                        mIsLoggedIn.setValue(true);
                        insertTeacher(response.body().getTeacher());
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mErrorMessage.setValue(t.getMessage());
            }
        });
    }

    public LiveData<Boolean> isLoggedIn() {
        return mIsLoggedIn;
    }

    public void login(String username, String password) {
        API_SERVICE.login(username, password).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                SharedPreferences.Editor prefsEditor = mContext.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).edit();
                if (response.body() != null) {
                    if (response.body().getErrorMessage() != null) {
                        mErrorMessage.setValue(response.body().getErrorMessage());
                    }
                    if (response.body().getToken() != null) {
                        prefsEditor.putString(USER_TOKEN, response.body().getToken());
                        prefsEditor.apply();
                    }
                    if (response.body().getStudent() != null) {
                        mStudent.setValue(response.body().getStudent());
                        prefsEditor.putLong(USER_ID, response.body().getStudent().getId());
                        prefsEditor.putInt(Constants.STUDENT_LEVEL, response.body().getStudent().getLevel());
                        prefsEditor.apply();
                        mIsLoggedIn.setValue(true);
                        insertStudent(response.body().getStudent());
                    }
                    if (response.body().getTeacher() != null) {
                        mTeacher.setValue(response.body().getTeacher());
                        prefsEditor.putLong(USER_ID, response.body().getTeacher().getId());
                        prefsEditor.apply();
                        mIsLoggedIn.setValue(true);
                        insertTeacher(response.body().getTeacher());
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mErrorMessage.setValue(t.getMessage());
            }
        });
    }

    public LiveData<Teacher> registerTeacher(String username, String password) {
        register(username, password, true);
        return mTeacher;
    }

    public LiveData<Student> registerStudent(String username, String password) {
        register(username, password, false);
        return mStudent;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void insertTeacher(Teacher lesson) {
        databaseWriteExecutor.execute(() -> {
            db.TeacherDao().insertTeacher(lesson);
        });
    }

    public void insertStudent(Student student) {
        databaseWriteExecutor.execute(() -> {
            db.StudentDao().insertStudent(student);
        });
    }

    public void deleteTeacher(Teacher lesson) {
        databaseWriteExecutor.execute(() -> {
            db.TeacherDao().deleteTeacher(lesson);
        });
    }
}
