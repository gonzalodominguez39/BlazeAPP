package com.emma.blaze.ui.phone;

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

public class CodePhoneViewModel extends AndroidViewModel {
    private final MutableLiveData<String> verificationId = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCodeSent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public CodePhoneViewModel(@NonNull Application application) {
        super(application);
        isCodeSent.setValue(false);
    }

    public LiveData<String> getVerificationId() {
        return verificationId;
    }

    public MutableLiveData<String> getPhoneNumberLiveData() {
        return phoneNumberLiveData;

    }

    public LiveData<Boolean> isCodeSent() {
        return isCodeSent;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void startPhoneVerification(Activity activity) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+1 385-465-6765")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Log.d("CodePhoneViewModel", "Verificación automática completada");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        errorMessage.postValue("Error: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        Log.d("CodePhoneViewModel", "Código enviado: verificationId=" + verificationId);
                        CodePhoneViewModel.this.verificationId.setValue(verificationId);
                        CodePhoneViewModel.this.isCodeSent.postValue(true);
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyCode(String code, OnCompleteListener<AuthResult> listener) {
        String verificationIdValue = verificationId.getValue();

        if (verificationIdValue == null) {
            errorMessage.postValue("Error: verificationId no está disponible. Reintenta enviar el código.");
            return;
        }

        isLoading.setValue(true);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIdValue, code);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            isLoading.setValue(false);
            listener.onComplete(task);
        });
    }

    public void resetValues() {
        isCodeSent.setValue(false);
        phoneNumberLiveData.setValue(null);
    }
    public MutableLiveData<Boolean> getIsCodeSent() {
        return isCodeSent;
    }
}
