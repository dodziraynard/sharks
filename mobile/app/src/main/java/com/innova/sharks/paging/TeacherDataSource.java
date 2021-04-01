package com.innova.sharks.paging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.innova.sharks.DataRepository;
import com.innova.sharks.models.Teacher;

import java.util.List;

public class TeacherDataSource extends PageKeyedDataSource<Integer, Teacher> {
    private static final String TAG = "TeacherDataSource";
    //    private final TeacherDao mTeacherDao;
    private final DataRepository mRepository;

    // You can also pass in the dao directly.
    public TeacherDataSource(Context context) {
        mRepository = new DataRepository(context);
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Teacher> callback) {
        List<Teacher> courses = mRepository.getPageTeachers(0, params.requestedLoadSize);
        callback.onResult(courses, null, courses.size() + 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Teacher> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Teacher> callback) {
        List<Teacher> courses = mRepository.getPageTeachers(params.key, params.requestedLoadSize);
        int nextKey = params.key + courses.size();
        callback.onResult(courses, nextKey);
    }
}
