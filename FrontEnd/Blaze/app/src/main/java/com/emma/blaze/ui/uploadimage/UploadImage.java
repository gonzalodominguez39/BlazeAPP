package com.emma.blaze.ui.uploadimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emma.blaze.R;
import com.emma.blaze.databinding.FragmentUploadImageBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UploadImage extends Fragment {

    private GridLayout photoGridLayout;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private List<String> imagePaths = new ArrayList<>();
    private UploadImageViewModel uploadImageViewModel;
    private FragmentUploadImageBinding binding;

    @SuppressLint("InlinedApi")
    private static final String[] PERMISSIONS_TIRAMISU_AND_HIGHER = {
            Manifest.permission.READ_MEDIA_IMAGES
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
uploadImageViewModel = new ViewModelProvider(this).get(UploadImageViewModel.class);
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
                            uploadImageViewModel.setImagePaths(String.valueOf(imageUri));
                        }
                    } else {
                        Toast.makeText(getActivity(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= FragmentUploadImageBinding.inflate(inflater, container, false);
        uploadImageViewModel.getImagePaths().observe(getViewLifecycleOwner(), paths -> {
            if (paths != null) {
                for (String path : paths) {
                    if (!imagePaths.contains(path)){
                        updateImageInGrid(Uri.parse(path));
                        imagePaths.add(path);
                }
                }
            }
        });
        binding.uploadImageButton.setOnClickListener(v -> {
uploadImageViewModel.uploadImages();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        photoGridLayout = view.findViewById(R.id.photoGridLayout);
        for (int i = 0; i < photoGridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) photoGridLayout.getChildAt(i);
            cardView.setOnClickListener(v -> checkAndRequestPermissions());
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
            openImageChooser();
        }
    }


    @SuppressLint("IntentReset")
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);


    }

    @SuppressLint("DiscouragedApi")
    private void updateImageInGrid(Uri imageUri) {
        try {
            for (int i = 0; i < photoGridLayout.getChildCount(); i++) {
                CardView cardView = (CardView) photoGridLayout.getChildAt(i);

                ImageView imageView = cardView.findViewById(
                        getResources().getIdentifier("photo" + (i + 1), "id", requireActivity().getPackageName())
                );

                if (imageView.getDrawable() == null) {
                    Picasso.get()
                            .load(imageUri)
                            .resize(800, 800)
                            .centerInside()
                            .placeholder(R.drawable.alarm_add_svgrepo_com)
                            .error(R.drawable.cancel_svg_com)
                            .into(imageView);
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Ocurrió un error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }

    }


}
