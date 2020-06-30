package com.example.android.camera2basic.NetWorking;

import com.example.android.camera2basic.Camera2BasicFragment;
import com.example.android.camera2basic.Image2Activity;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig2 {

    private static final String BASE_URL = Camera2BasicFragment.URL;
    private static Retrofit retrofit;
    public static Retrofit getRetrofitClient(Image2Activity context)
    {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
