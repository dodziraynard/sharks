package com.innova.sharks.paging.course;

import android.content.Context;

import androidx.paging.DataSource;

import com.innova.sharks.models.Course;

import org.jetbrains.annotations.NotNull;


public class CourseDataSourceFactory {
    private static final String TAG = "CourseDataSourceFactory";

    public static class LiveCourseDataSourceFactory extends DataSource.Factory<Integer, Course> {
        private final Context mContext;
        private LiveCourseDataSource mDataSource;
        private String mQuery = "";

        public LiveCourseDataSourceFactory(Context context) {
            mContext = context;
        }

        @NotNull
        @Override
        public DataSource<Integer, Course> create() {
            if (mDataSource == null) {
                mDataSource = new LiveCourseDataSource(mContext, mQuery);
            }
            return mDataSource;
        }

        public void setQuery(String query) {
            mQuery = query;
        }
    }

    public static class DBCourseDataSourceFactory extends DataSource.Factory<Integer, Course> {
        private final Context mContext;
        private DBCourseDataSource mDataSource;

        public DBCourseDataSourceFactory(Context context) {
            mContext = context;
        }

        @Override
        public DataSource<Integer, Course> create() {
            if (mDataSource == null) {
                mDataSource = new DBCourseDataSource(mContext);
            }
            return mDataSource;
        }

        public void invalidate() {
            mDataSource.invalidate();
        }
    }
}
