package com.emma.blaze.data.service;

import com.emma.blaze.data.model.ImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImageService {
    @Multipart
    @POST("/api/pictures/upload")
    Call<ImageResponse> uploadPic(@Part MultipartBody.Part file);
}
