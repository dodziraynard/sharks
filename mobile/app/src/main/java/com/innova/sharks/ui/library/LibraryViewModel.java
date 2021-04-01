package com.innova.sharks.ui.library;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.sharks.AppRoomDatabase;
import com.innova.sharks.models.Book;
import com.innova.sharks.network.ApiService;
import com.innova.sharks.network.RestApiFactory;
import com.innova.sharks.network.response_models.BooksResponse;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.innova.sharks.AppRoomDatabase.databaseWriteExecutor;

public class LibraryViewModel extends AndroidViewModel {
    private static final String TAG = "LibraryViewModel";
    private final AppRoomDatabase db;
    private final ApiService API_SERVICE;
    private final Application mApplication;
    MutableLiveData<List<Book>> mBooks = new MutableLiveData<>();

    public LibraryViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        db = AppRoomDatabase.getDatabase(application);
        API_SERVICE = RestApiFactory.create(application);
    }

    public LiveData<List<Book>> getBooks(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getBooks(query).enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mBooks.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                mBooks.setValue(new ArrayList<>());
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mBooks;
    }

    public void refreshData(String query) {
        NetworkState.getInstance().setStatus(NetworkState.Status.LOADING);
        API_SERVICE.getBooks(query).enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                NetworkState.getInstance().setStatus(NetworkState.Status.LOADED);
                if (response.body() != null) {
                    mBooks.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                NetworkState.getInstance().setStatus(NetworkState.Status.FAILED);
                mBooks.setValue(new ArrayList<>());
                Toast.makeText(mApplication, "Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void insertBook(Book lesson) {
        databaseWriteExecutor.execute(() -> {
            db.BookDao().insertBook(lesson);
        });
    }

    public void deleteBook(Book lesson) {
        databaseWriteExecutor.execute(() -> {
            db.BookDao().deleteBook(lesson);
        });
    }
}
