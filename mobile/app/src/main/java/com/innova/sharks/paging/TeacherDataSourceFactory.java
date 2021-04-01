package com.innova.sharks.paging;

import android.content.Context;

import androidx.paging.DataSource;

import com.innova.sharks.models.Teacher;


public class TeacherDataSourceFactory extends DataSource.Factory<Integer, Teacher> {
    private final Context mContext;
    private TeacherDataSource mDataSource;

    public TeacherDataSourceFactory(Context context) {
        mContext = context;
    }

    @Override
    public DataSource<Integer, Teacher> create() {
        if (mDataSource == null) {
            mDataSource = new TeacherDataSource(mContext);
        }
        return mDataSource;
    }
}
