package com.emma.blaze.ui.login;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emma.blaze.R;
import com.emma.blaze.data.model.User;
import com.emma.blaze.databinding.FragmentLoginBinding;
import com.emma.blaze.ui.sharedViewModel.UserViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private LoginViewModel loginViewModel;
    private UserViewModel userViewModel;

    private final ActivityResultLauncher<IntentSenderRequest> intentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            firebaseAuthWithGoogle(idToken);
                        }
                    } catch (ApiException e) {
                        Log.e("SignIn", "Error al procesar las credenciales: " + e.getMessage());
                    }
                } else {
                    Log.e("SignIn", "Inicio de sesión cancelado o fallido.");
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(requireActivity());
        this.loginViewModel = new LoginViewModel(requireActivity().getApplication());
        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.userIsLogin();
       userViewModel.getIsLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                navigateScreen(R.id.action_login_to_navigation_home);
            }
        });

        setupListeners();


        return binding.getRoot();
    }

    private void setupListeners() {
        binding.singInPhone.setOnClickListener(v -> navigateScreen(R.id.action_login_to_PhoneCodeSend));
        binding.singInGoogle.setOnClickListener(v -> startGoogleSignIn());
    }

    private void startGoogleSignIn() {
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), result -> {
                    try {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        intentLauncher.launch(intentSenderRequest);
                    } catch (Exception e) {
                        Log.e("SignIn", "Error al iniciar One Tap: " + e.getMessage());
                    }
                })
                .addOnFailureListener(requireActivity(), e -> Log.e("SignIn", "One Tap falló: " + e.getMessage()));
    }

    private void firebaseAuthWithGoogle(String idToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.singInGoogle.setEnabled(false);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.singInGoogle.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            loginViewModel.login(user.getEmail());
                            loginViewModel.getCurrentUser().observe(getViewLifecycleOwner(), currentUser -> {
                                        if (currentUser != null) {
                                            Toast.makeText(getContext(), "Bienvenido"+ currentUser.getName(), Toast.LENGTH_SHORT).show();
                                            userViewModel.createUserCache(currentUser);
                                            navigateScreen(R.id.action_login_to_navigation_home);
                                        } else {
                                            User newUser = new User();
                                            newUser.setName(user.getDisplayName());
                                            newUser.setEmail(user.getEmail());
                                            userViewModel.getUserLiveData().setValue(newUser);
                                            navigateScreen(R.id.action_login_to_PhoneCodeSend);
                                        }
                                    }
                            );
                        }
                    } else {
                        Log.e("SignIn", "Fallo en la autenticación con Google", task.getException());
                        Toast.makeText(getContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

    private void navigateScreen(int actionId) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(actionId, null, new  NavOptions.Builder().setPopUpTo(R.id.login, true).build());
    }
}
