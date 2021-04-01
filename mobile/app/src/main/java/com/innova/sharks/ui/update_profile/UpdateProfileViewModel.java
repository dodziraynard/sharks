package com.innova.sharks.ui.update_profile;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

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

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.utils.Functions.removeUserToken;


public class UpdateProfileViewModel extends AndroidViewModel {
    private static final String TAG = "UpdateProfileViewModel";
    private final AppRoomDatabase db;
    private final ApiService API_SERVICE;
    private final Application mApplication;
    MutableLiveData<Teacher> mTeacher = new MutableLiveData<>();
    MutableLiveData<Student> mStudent = new MutableLiveData<>();
    MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    public UpdateProfileViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public void updateProfileImages(MultipartBody.Part backgroundImage) {
        mIsLoading.setValue(true);
        API_SERVICE.updateProfileImage(backgroundImage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mIsLoading.setValue(false);
                if (response.isSuccessful()) {
                    Toast.makeText(mApplication, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mApplication, "Failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mIsLoading.setValue(false);
                Toast.makeText(mApplication, "Failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProfileInfo(HashMap<String, String> profileInfo) {
        mIsLoading.setValue(true);

        API_SERVICE.updateProfileInfo(profileInfo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mIsLoading.setValue(false);
                Log.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mIsLoading.setValue(false);
            }
        });
    }

    public void logout() {
        API_SERVICE.logOut().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse: logout");
                removeUserToken(mApplication);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    public void getUser(long userId) {
        mIsLoading.setValue(true);
        API_SERVICE.getProfile(userId).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                mIsLoading.setValue(false);
                Log.d(TAG, "onResponse: ");
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: 2");
                    if (response.body().getStudent() != null) {
                        mStudent.setValue(response.body().getStudent());
                    }
                    if (response.body().getTeacher() != null) {
                        mTeacher.setValue(response.body().getTeacher());
                    }
                } else {
                    Toast.makeText(mApplication, "Profile not found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                mIsLoading.setValue(false);
                Toast.makeText(mApplication, "Failed. Please try again.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<Teacher> getTeacher() {
        return mTeacher;
    }

    public LiveData<Student> getStudent() {
        return mStudent;
    }

    public LiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }
}
