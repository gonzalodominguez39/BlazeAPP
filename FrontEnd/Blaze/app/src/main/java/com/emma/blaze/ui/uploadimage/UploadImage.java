package com.emma.blaze.ui.uploadimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.emma.blaze.R;

public class UploadImage extends Fragment {

    private GridLayout photoGridLayout;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @SuppressLint("InlinedApi")
    private static final String[] PERMISSIONS_TIRAMISU_AND_HIGHER = {
            Manifest.permission.READ_MEDIA_IMAGES
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestMultiplePermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    for (Boolean isGranted : permissions.values()) {
                        if (!isGranted) {
                            allGranted = false;
                            break;
                        }
                    }
                    if (allGranted) {
                        openImageChooser();
                    } else {
                        Toast.makeText(getActivity(), "Permisos denegados", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            updateImageInGrid(imageUri);
                        }
                    } else {
                        Toast.makeText(getActivity(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoGridLayout = view.findViewById(R.id.photoGridLayout);
        for (int i = 0; i < photoGridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) photoGridLayout.getChildAt(i);
            imageView.setOnClickListener(v -> checkAndRequestPermissions());
        }
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestMultiplePermissionsLauncher.launch(PERMISSIONS_TIRAMISU_AND_HIGHER);
            } else {
                openImageChooser();
            }
        } else {
            openImageChooser();        }
    }


    @SuppressLint("IntentReset")
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Especificamos que solo queremos imágenes
        imagePickerLauncher.launch(intent);
    }

    private void updateImageInGrid(Uri imageUri) {
        for (int i = 0; i < photoGridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) photoGridLayout.getChildAt(i);
            if (imageView.getDrawable() == null) {
                imageView.setImageURI(imageUri);
                break;
            }
        }
    }
}
