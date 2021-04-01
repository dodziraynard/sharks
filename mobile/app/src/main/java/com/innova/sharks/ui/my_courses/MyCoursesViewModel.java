package com.innova.sharks.ui.my_courses;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Course;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.CoursesResponse;
import com.innova.sharks.paging.course.CourseDataSourceFactory.DBCourseDataSourceFactory;
import com.innova.sharks.paging.course.CourseDataSourceFactory.LiveCourseDataSourceFactory;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;


public class MyCoursesViewModel extends AndroidViewModel {
    private static final String TAG = "MyCoursesViewModel";
    private final AppRoomDatabase db;
    private final LiveData<PagedList<Course>> mDBPagedCourses;
    private final PagedList.Config mConfig;
    private final DBCourseDataSourceFactory mDbCourseFactory;
    private final Application mContext;
    private final ApiService API_SERVICE;
    Executor executor;
    private LiveData<PagedList<Course>> mPagedCourses;
    private LiveCourseDataSourceFactory mLiveCourseFactory;

    public MyCoursesViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        db = AppRoomDatabase.getDatabase(application);
        executor = Executors.newFixedThreadPool(5);
        API_SERVICE = RestApiFactory.create(application);

        // Instantiate CourseDataSourceFactory
        mLiveCourseFactory = new LiveCourseDataSourceFactory(application);
        mDbCourseFactory = new DBCourseDataSourceFactory(application);

        mConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .setPageSize(50)
                .setPrefetchDistance(25)
                .build();
        mPagedCourses = new LivePagedListBuilder<>(mLiveCourseFactory, mConfig).setFetchExecutor(executor).build();
        mDBPagedCourses = new LivePagedListBuilder<>(mDbCourseFactory, mConfig).build();
    }

    public LiveData<PagedList<Course>> getPagedCourses() {
        return mPagedCourses;
    }

    public LiveData<PagedList<Course>> getPagedFilteredCourses(String query) {
        mLiveCourseFactory = new LiveCourseDataSourceFactory(mContext);
        mLiveCourseFactory.setQuery(query);
        mPagedCourses = new LivePagedListBuilder<>(mLiveCourseFactory, mConfig).setFetchExecutor(executor).build();
        return mPagedCourses;
    }

    public void invalidate() {
        mDbCourseFactory.invalidate();
    }

    public LiveData<PagedList<Course>> getDBPagedCourses() {
        return mDBPagedCourses;
    }

    public LiveData<List<Course>> getCourses() {
        return db.CourseDao().getCourses();
    }

    public void loadMyCourse(int level) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getMyCourses(level).enqueue(new Callback<CoursesResponse>() {
            @Override
            public void onResponse(Call<CoursesResponse> call, Response<CoursesResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    ArrayList<Course> courses = response.body().getResults();
                    databaseWriteExecutor.execute(() -> {
                        db.CourseDao().insertCourses(courses);
                    });
                } else Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CoursesResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
            }
        });
    }

    public void insertCourse(Course lesson) {
        databaseWriteExecutor.execute(() -> {
            db.CourseDao().insertCourse(lesson);
        });
    }

    public void deleteCourse(Course lesson) {
        databaseWriteExecutor.execute(() -> {
            db.CourseDao().deleteCourse(lesson);
        });
    }

    public static class MyCoursesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private final Application mApplication;

        public MyCoursesViewModelFactory(Application application) {
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MyCoursesViewModel(mApplication);
        }
    }
}
