package com.learn.mytodo.data.source.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by dong on 2017/4/1 0001.
 */

public class ServiceGenerator {

    public static final String API_BASE_URL = "http://10.0.2.2:8080/todoservlet/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}