package com.example.android.camera2basic.NetWorking;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiMetaData {
    @Multipart
    @POST("/get_photo_metadata/")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part multipartBody);
}
