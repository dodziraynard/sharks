package com.innova.sharks.paging.course;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.innova.sharks.models.Course;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.CoursesResponse;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveCourseDataSource extends PageKeyedDataSource<Integer, Course> {
    private static final String TAG = "CourseDataSource";
    private final ApiService API_SERVICE;
    private final Context mContext;
    private String mQuery;

    public LiveCourseDataSource(Context context, String query) {
        mContext = context;
        mQuery = query;
        API_SERVICE = RestApiFactory.create(context);
    }

    public void setQuery(String query) {
        mQuery = query;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Course> callback) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        Call<CoursesResponse> call = API_SERVICE.getCourses(mQuery, 0, params.requestedLoadSize);
        call.enqueue(new Callback<CoursesResponse>() {
            @Override
            public void onResponse(Call<CoursesResponse> call, Response<CoursesResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    ArrayList<Course> courses = response.body().getResults();
                    callback.onResult(courses, null, courses.size() + 1);
                } else Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CoursesResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {
        Call<CoursesResponse> call = API_SERVICE.getCourses(mQuery, params.key, params.requestedLoadSize);
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);

        call.enqueue(new Callback<CoursesResponse>() {
            @Override
            public void onResponse(Call<CoursesResponse> call, Response<CoursesResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);

                if (response.body() != null) {
                    ArrayList<Course> courses = response.body().getResults();
                    int nextKey = params.key + params.requestedLoadSize;
                    callback.onResult(courses, nextKey);
                } else Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CoursesResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
