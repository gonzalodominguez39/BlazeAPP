package com.emma.blaze.ui.uploadimage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadImageViewModel extends AndroidViewModel {
    private final UploadImageRepository uploadImageRepository = new UploadImageRepository();
    private final List<String> Paths = new ArrayList<>();
    private final MutableLiveData<List<String>> imagePaths = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> uploadProgress = new MutableLiveData<>(0); // Progreso de carga (en %).
    private final MutableLiveData<Boolean> isUploading = new MutableLiveData<>(false); // Estado de carga.
    private final ExecutorService executorService = Executors.newFixedThreadPool(3); // 3 subidas concurrentes.

    public UploadImageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getImagePaths() {
        return imagePaths;
    }

    public LiveData<Integer> getUploadProgress() {
        return uploadProgress;
    }

    public LiveData<Boolean> getIsUploading() {
        return isUploading;
    }

    public void setImagePaths(String path) {
        Paths.add(path);
        imagePaths.setValue(Paths);
    }
    @SuppressLint("NewApi")
    public void uploadImages() {
        if (imagePaths.getValue() == null || imagePaths.getValue().isEmpty()) {
            return;
        }

        isUploading.setValue(true);
        List<String> localPaths = imagePaths.getValue();
        List<String> uploadedPaths = Collections.synchronizedList(new ArrayList<>());  // Usar una lista sincronizada
        int totalImages = localPaths.size();
        AtomicInteger uploadedCount = new AtomicInteger(0);  // Para contar el número de imágenes subidas

        for (String path : localPaths) {
            executorService.submit(() -> {
                try {
                    // Obtener un InputStream desde la URI (deberías obtener la URI del archivo, no solo la ruta)
                    InputStream inputStream = getInputStreamFromUri(path); // Método que obtendrá el InputStream
                    if (inputStream == null) {
                        // Manejo de error si no se puede obtener el InputStream
                        handleUploadFailure();
                        return;
                    }

                     RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), inputStream.readAllBytes());
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", new File(path).getName(), requestBody);

                    uploadImageRepository.uploadImage(part).enqueue(new Callback<ImageResponse>() {
                        @Override
                        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // El servidor devuelve información sobre la imagen subida.
                                String remoteUrl = response.body().getImageUrl();
                                uploadedPaths.add(remoteUrl);

                                // Actualizar progreso.
                                int progress = (uploadedPaths.size() * 100) / totalImages;
                                uploadProgress.postValue(progress);

                                // Verificar si todas las imágenes se subieron.
                                if (uploadedPaths.size() == totalImages) {
                                    isUploading.postValue(false);
                                }
                            } else {
                                // Manejo de error si la respuesta no es exitosa
                                handleUploadFailure();
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageResponse> call, Throwable t) {
                            handleUploadFailure();
                            t.printStackTrace();
                        }

                        private void handleUploadFailure() {
                            // Asegúrate de que solo se marque como no subiendo si todas las imágenes fallan
                            if (uploadedCount.incrementAndGet() == totalImages) {
                                isUploading.postValue(false);
                            }
                        }
                    });
                } catch (IOException e) {
                    // Captura el error si no puedes obtener el InputStream correctamente
                    handleUploadFailure();
                    e.printStackTrace();
                }
            });
        }
    }

    // Método para obtener el InputStream desde la URI
    private InputStream getInputStreamFromUri(String uriPath) throws IOException {
        // Aquí debes manejar correctamente el URI, dependiendo de cómo lo recibes (debe ser un Content URI)
        Uri uri = Uri.parse(uriPath);  // Asegúrate de que la ruta esté correctamente formateada
        InputStream inputStream = null;

        if (uri.getScheme().equals("content")) {
            // Usar ContentResolver para obtener el InputStream
            ContentResolver contentResolver = getApplication().getContentResolver();
            inputStream = contentResolver.openInputStream(uri);
        } else if (uri.getScheme().equals("file")) {
            File file = new File(uri.getPath());
            inputStream = new FileInputStream(file);
        }

        return inputStream;
    }
    private void handleUploadFailure() {

    }
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}