package com.innova.sharks.network;

import android.content.Context;

import com.innova.sharks.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.sharks.utils.Constants.LIVE_BASE_API_URL;
import static com.innova.sharks.utils.Constants.TEST_BASE_API_URL;

public class RestApiFactory {

    public static ApiService create(Context context) {
        // Setting up request interceptor.
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(context);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().addInterceptor(headerInterceptor)
                .build();
        String baseUrl = TEST_BASE_API_URL;
        if (!BuildConfig.DEBUG) {
            baseUrl = LIVE_BASE_API_URL;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

}
