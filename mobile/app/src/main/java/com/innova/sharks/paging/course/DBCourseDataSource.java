package com.innova.sharks.paging.course;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.innova.sharks.DataRepository;
import com.innova.sharks.models.Course;
import com.innova.sharks.utils.NetworkState;

import java.util.List;

public class DBCourseDataSource extends PageKeyedDataSource<Integer, Course> {
    private static final String TAG = "CourseDataSource";
    private final DataRepository mRepository;

    // You can also pass in the dao directly.
    public DBCourseDataSource(Context context) {
        mRepository = new DataRepository(context);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Course> callback) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        List<Course> courses = mRepository.getPageCourses(0, params.requestedLoadSize);
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
        int index = courses.size() > 0 ? (int) (courses.get(courses.size() - 1).getId() + 1) : 0;
        callback.onResult(courses, null, index);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        List<Course> courses = mRepository.getPageCourses(params.key, params.requestedLoadSize);
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
        int nextKey = params.key + courses.size();
        callback.onResult(courses, nextKey);
    }
}
