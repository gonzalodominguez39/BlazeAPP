package com.emma.blaze.ui.signup;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.ui.login.validation.EmailValidation;
import com.google.firebase.auth.FirebaseAuth;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;


public class SignUpViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> setUser = new MutableLiveData<>();
    private final MutableLiveData<FirebaseAuth> mAuth = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> lastName = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> birthDate = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        setUser.setValue(false);
        mAuth.setValue(FirebaseAuth.getInstance());
    }

    public MutableLiveData<FirebaseAuth> getmAuth() {
        return mAuth;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<Boolean> getSetUser() {
        return setUser;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public MutableLiveData<String> getGender() {
        return gender;
    }

    public MutableLiveData<LocalDate> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<Boolean> getIsEmailValid() {
        return isEmailValid;
    }

    public void setUser() {
        Pair<String, String> fullName = splitFullName(Objects.requireNonNull(Objects.requireNonNull(mAuth.getValue()).getCurrentUser()).getDisplayName());
        name.setValue(fullName.first);
        lastName.setValue(fullName.second);
        email.setValue(mAuth.getValue().getCurrentUser().getEmail());


    }

    public static Pair<String, String> splitFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new Pair<>("", "");
        }
        String[] parts = fullName.trim().split("\\s+");
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[parts.length - 1] : "";

        return new Pair<>(firstName, lastName);
    }
    public void validateEmail(@NonNull CharSequence email) {
       isEmailValid.setValue(EmailValidation.isValidEmail(email.toString()));
    }

    @SuppressLint("NewApi")
    public static LocalDate parseLongToLocalDate(long timestamp) {
        try {
            return Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (Exception e) {
            System.err.println("Error parsing timestamp: " + e.getMessage());
            return null;
        }
    }

    public Boolean validateForm() {
        if (name.getValue() == null || name.getValue().isEmpty()) return false;
        if (lastName.getValue() == null || lastName.getValue().isEmpty()) return false;
        if (email.getValue() == null || email.getValue().isEmpty()) return false;
        if (password.getValue() == null || password.getValue().isEmpty()) return false;
        if (confirmPassword.getValue() == null || confirmPassword.getValue().isEmpty()) return false;
        if (gender.getValue() == null || gender.getValue().isEmpty()) return false;
        if (birthDate.getValue() == null) return false;
        return true;
    }

    public Boolean matchPasswords() {
        return password.getValue().equals(confirmPassword.getValue());
    }
}