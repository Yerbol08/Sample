package com.example.android.camera2basic.NetWorking;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GetPhotoAlign {


    @GET("/get_photo_align/")
    Call<ResponseBody> getData(@QueryMap Map<String, String> options);
}
