package com.innova.sharks.network;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.innova.sharks.utils.Functions.getUserToken;


public class HeaderInterceptor implements Interceptor {
    private Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    public void removeContext() {
        this.context = null;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (context != null) {
            String token = getUserToken(context);

            if (!token.isEmpty()) {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Token " + token)
                        .build();
                return chain.proceed(request);
            }
        }
        return chain.proceed(chain.request());
    }
}
