package com.emma.blaze.ui.uploadimage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.emma.blaze.data.model.ImageResponse;
import com.emma.blaze.data.repository.UploadImageRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
public class UploadImageViewModel extends AndroidViewModel {
    private  UploadImageRepository uploadImageRepository ;
    private final List<String> Paths = new ArrayList<>();
    private final MutableLiveData<List<String>> imagePaths = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> uploadPaths = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> uploadProgress = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isUploading = new MutableLiveData<>(false);
    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    public UploadImageViewModel(@NonNull Application application) {
        super(application);
        uploadImageRepository= new UploadImageRepository(application.getApplicationContext());
    }

    public LiveData<List<String>> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String path) {
        Paths.add(path);
        imagePaths.setValue(Paths);
    }

    public MutableLiveData<List<String>> getUploadPaths() {
        return uploadPaths;
    }

    public MutableLiveData<Boolean> getIsUploading() {
        return isUploading;
    }

    public MutableLiveData<Integer> getUploadProgress() {
        return uploadProgress;
    }

    @SuppressLint("NewApi")
    public void uploadImages() {
        if (imagePaths.getValue() == null || imagePaths.getValue().isEmpty()) {
            return;
        }

        isUploading.setValue(true);
        List<String> localPaths = imagePaths.getValue();
        List<String> uploadedPaths = Collections.synchronizedList(new ArrayList<>());
        int totalImages = localPaths.size();
        AtomicInteger uploadedCount = new AtomicInteger(0);

        for (String path : localPaths) {
            executorService.submit(() -> {
                try {
                    InputStream inputStream = getInputStreamFromUri(path,getApplication().getApplicationContext());
                    Log.d("path", "uploadImages: "+path);
                    if (inputStream == null) {
                        return;
                    }
                    Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap resizedBitmap = resizeBitmap(originalBitmap, 800, 800);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", new File(path).getName(), requestBody);

                    uploadImageRepository.uploadImage(part).enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<ImageResponse> call, @NonNull Response<ImageResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String remoteUrl = response.body().getImageUrl();
                                uploadedPaths.add(remoteUrl);
                                uploadPaths.setValue(uploadedPaths);
                                Log.d("imagesUploads", "onResponse: "+uploadedPaths);
                                int progress = (uploadedPaths.size() * 100) / totalImages;
                                uploadProgress.postValue(progress);
                                if (uploadedPaths.size() == totalImages) {
                                    isUploading.postValue(false);
                                }
                            } else {
                                handleUploadFailure();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ImageResponse> call, @NonNull Throwable t) {
                            handleUploadFailure();
                            t.printStackTrace();
                        }

                        private void handleUploadFailure() {
                            if (uploadedCount.incrementAndGet() == totalImages) {
                                isUploading.postValue(false);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Bitmap resizeBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        float ratio = Math.min((float) maxWidth / originalWidth, (float) maxHeight / originalHeight);

        int newWidth = Math.round(originalWidth * ratio);
        int newHeight = Math.round(originalHeight * ratio);

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

    private InputStream getInputStreamFromUri(String uriPath, Context context) throws IOException {
        Uri uri = Uri.parse(uriPath);
        InputStream inputStream = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            inputStream =  context.getContentResolver().openInputStream(uri);
        } else if (Objects.equals(uri.getScheme(), "file")) {
            File file = new File(uri.getPath());
            inputStream = new FileInputStream(file);
        }

        return inputStream;
    }

    public void clearImagePaths() {
        Paths.clear();
        imagePaths.setValue(Paths);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }


}
