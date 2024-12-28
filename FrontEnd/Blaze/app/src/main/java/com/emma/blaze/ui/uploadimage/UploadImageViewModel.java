package com.emma.blaze.ui.uploadimage;

import android.app.Application;
import android.net.Uri;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.repository.UserRepository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadImageViewModel extends AndroidViewModel {
    private final UserRepository userRepository = new UserRepository();

    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();

    public UploadImageViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public void uploadImage(Uri imageUri) {
        try {
            // Obtener InputStream del URI
            InputStream inputStream = getApplication().getContentResolver().openInputStream(imageUri);

            // Crear un archivo temporal en el directorio de caché
            File tempFile = new File(getApplication().getCacheDir(), "temp_image.jpg");

            // Escribir el InputStream al archivo temporal
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            // Crear el RequestBody y MultipartBody.Part para la imagen
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", tempFile.getName(), requestBody);

            // Realizar la solicitud de carga de la imagen
            Call<ResponseBody> call = userRepository.uploadImage(part);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        statusMessage.setValue("Imagen subida exitosamente");
                    } else {
                        statusMessage.setValue("Error al subir la imagen");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    statusMessage.setValue("Error de conexión");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            statusMessage.setValue("Error al procesar la imagen");
        }
    }

}
