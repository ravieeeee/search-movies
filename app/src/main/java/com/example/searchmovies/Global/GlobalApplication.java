package com.example.searchmovies.Global;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.searchmovies.Network.NetworkService;
import com.example.searchmovies.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalApplication extends Application {
    public static NetworkService networkService;
    public static Context context;
    public static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resources = getResources();
        retrofitInit();
    }

    private static void retrofitInit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder ongoing = chain.request().newBuilder();
                    ongoing.addHeader("X-Naver-Client-Id", resources.getString(R.string.clientID));
                    ongoing.addHeader("X-Naver-Client-Secret", resources.getString(R.string.clientSecret));
                    return chain.proceed(ongoing.build());
                }
            })
            .build();

        Retrofit retrofit =
            new Retrofit
                .Builder()
                .baseUrl("https://openapi.naver.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        networkService = retrofit.create(NetworkService.class);
    }
}
