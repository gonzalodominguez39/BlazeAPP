package com.emma.blaze.data.repository;

import android.content.Context;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.dto.ImageResponse;
import com.emma.blaze.data.service.UploadImageService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Part;

public class UploadImageRepository {
    private final UploadImageService uploadImageService;

    public UploadImageRepository(Context context) {
        this.uploadImageService = RetrofitClient.getRetrofitInstance(context).create(UploadImageService.class);
    }

   public Call<ImageResponse> uploadImage(@Part MultipartBody.Part file){
        return uploadImageService.uploadPic(file);
    }
}

