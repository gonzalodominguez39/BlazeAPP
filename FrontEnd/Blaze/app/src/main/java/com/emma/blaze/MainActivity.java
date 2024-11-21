package com.emma.blaze;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.emma.blaze.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Enviar SMS a un número de prueba
        sendVerificationCode();
    }

    private void sendVerificationCode() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1 3856789023")         // Número al que enviar el código
                .setTimeout(60L, TimeUnit.SECONDS)   // Tiempo para esperar el código
                .setActivity(this)                  // Actividad actual
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    private FirebaseException e;

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Se verifica automáticamente (por ejemplo, con Smart Lock)
                        Log.d("Firebase", "Verificación completada automáticamente");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        this.e = e;
                    }



                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // Código enviado correctamente
                        Log.d("Firebase", "Código enviado: " + verificationId);
                        // Puedes guardar verificationId para usarlo luego al verificar manualmente
                    }


                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}