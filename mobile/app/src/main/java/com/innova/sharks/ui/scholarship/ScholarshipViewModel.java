package com.innova.sharks.ui.scholarship;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Scholarship;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.ScholarshipsResponse;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;


public class ScholarshipViewModel extends AndroidViewModel {
    private static final String TAG = "ScholarshipsViewModel";
    private final AppRoomDatabase db;
    private final ApiService API_SERVICE;
    private final Application mApplication;

    MutableLiveData<List<Scholarship>> mScholarships = new MutableLiveData<>();

    public ScholarshipViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public LiveData<List<Scholarship>> getScholarships(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getScholarships(query).enqueue(new Callback<ScholarshipsResponse>() {
            @Override
            public void onResponse(Call<ScholarshipsResponse> call, Response<ScholarshipsResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mScholarships.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ScholarshipsResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                mScholarships.setValue(new ArrayList<>());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mScholarships;
    }

    public void refreshData(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getScholarships(query).enqueue(new Callback<ScholarshipsResponse>() {
            @Override
            public void onResponse(Call<ScholarshipsResponse> call, Response<ScholarshipsResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mScholarships.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ScholarshipsResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                mScholarships.setValue(new ArrayList<>());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void insertScholarship(Scholarship lesson) {
        databaseWriteExecutor.execute(() -> {
            db.ScholarshipDao().insertScholarship(lesson);
        });
    }

    public void deleteScholarship(Scholarship lesson) {
        databaseWriteExecutor.execute(() -> {
            db.ScholarshipDao().deleteScholarship(lesson);
        });
    }
}
