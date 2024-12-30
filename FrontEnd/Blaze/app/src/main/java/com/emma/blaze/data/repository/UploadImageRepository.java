package com.emma.blaze.data.repository;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.ImageResponse;
import com.emma.blaze.data.service.UploadImageService;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Part;

public class UploadImageRepository {
    private final UploadImageService uploadImageService;

    public UploadImageRepository() {
        this.uploadImageService = RetrofitClient.getRetrofitInstance().create(UploadImageService.class);
    }

   public Call<ImageResponse> uploadImage(@Part MultipartBody.Part file){
        return uploadImageService.uploadPic(file);
    }
}

