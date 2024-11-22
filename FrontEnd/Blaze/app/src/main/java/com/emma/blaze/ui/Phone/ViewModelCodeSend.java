package com.emma.blaze.ui.Phone;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ViewModelCodeSend  extends AndroidViewModel {
    private final MutableLiveData<String> verificationId = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCodeSent = new MutableLiveData<>();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public ViewModelCodeSend(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getVerificationId() {
        return verificationId;
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isCodeSent() {
        return isCodeSent;
    }

    public  void setPhoneNumber(String number) {
        phoneNumber.setValue(number);
    }

    public void startPhoneVerification(Activity activity, String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity) // Se necesita la actividad
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Log.d("Firebase", "Verificación completada automáticamente");
                        // En caso de éxito automático (por ejemplo, Smart Lock)
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        errorMessage.postValue("Error: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        Log.d("CodeNumber", "Código enviado");
                        ViewModelCodeSend.this.verificationId.setValue(verificationId);;
                        ViewModelCodeSend.this.isCodeSent.postValue(true);
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyCode(String code, OnCompleteListener<AuthResult> listener) {
        String verificationIdValue = verificationId.getValue();
        Log.d("Auth", "verificationIdValue ="+verificationIdValue);
        if (verificationIdValue != null) {
            Log.d("CodeNumber", "codigo verificado con exito");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIdValue, code);
            auth.signInWithCredential(credential).addOnCompleteListener(listener);
        } else {
            errorMessage.postValue("Error: verificationId no disponible");
        }
    }
}

