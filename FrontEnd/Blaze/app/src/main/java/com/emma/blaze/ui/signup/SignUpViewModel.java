package com.emma.blaze.ui.signup;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.emma.blaze.data.model.Location;
import com.emma.blaze.data.model.User;
import com.emma.blaze.ui.login.validation.EmailValidation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;



public class SignUpViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> userVerified = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> lastName = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthDate = new MutableLiveData<>();
    private final MutableLiveData<Location> location= new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userVerified.setValue(false);
    }

    public MutableLiveData<Location> getLocation() {
        return location;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserVerified() {
        return userVerified;
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

    public MutableLiveData<String> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<Boolean> getIsEmailValid() {
        return isEmailValid;
    }

    public void setUser(User user)  {
        if(user == null){return;}
        userVerified.setValue(true);
        Pair<String, String> fullName = splitFullName(user.getName());
        name.setValue(fullName.first);
        lastName.setValue(fullName.second);
        email.setValue(user.getEmail());
    }

    public User createUser() {
        User userRequest = new User();
        userRequest.setEmail(email.getValue());
        userRequest.setPassword(password.getValue());
        userRequest.setPhoneNumber(Objects.requireNonNull(userMutableLiveData.getValue()).getPhoneNumber());
        userRequest.setName(name.getValue());
        userRequest.setLastName(lastName.getValue());
        userRequest.setBirthDate(birthDate.getValue());
        userRequest.setGender(gender.getValue());
        userRequest.setLocation(location.getValue());
    return userRequest;
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

    @SuppressLint("SimpleDateFormat")
    public static String longToDateString(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public Boolean validateForm() {
        if (name.getValue() == null || name.getValue().isEmpty()) return false;
        if (lastName.getValue() == null || lastName.getValue().isEmpty()) return false;
        if (email.getValue() == null || email.getValue().isEmpty()) return false;
        if (password.getValue() == null || password.getValue().isEmpty()) return false;
        if (confirmPassword.getValue() == null || confirmPassword.getValue().isEmpty())
            return false;
        if (gender.getValue() == null || gender.getValue().isEmpty()) return false;
        if (birthDate.getValue() == null) return false;
        return true;
    }

    public Boolean matchPasswords() {
        return password.getValue().equals(confirmPassword.getValue());
    }
}