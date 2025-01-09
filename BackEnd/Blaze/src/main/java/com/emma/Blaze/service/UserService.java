package com.emma.Blaze.service;


import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.model.UserPicture;
import com.emma.Blaze.repository.UserPictureRepository;
import com.emma.Blaze.repository.UserRepository;
import com.emma.Blaze.request.UserResponse;
import com.emma.Blaze.utils.UserFunction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private InterestService interestService;
    @Autowired
    private UserPictureRepository userPictureRepository;
    private static UserFunction userFunction;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User updateUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }


    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Interest> mapUsInterest(Long userId, List<String> interests) {
        return mapInterests(userId, interests);
    }

    public LocalDate parseBirthdateToLocalDate(String birthdate) {
        return UserFunction.stringToLocalDate(birthdate);
    }

    public User.Gender parseGender(String gender) {
        try {
            return User.Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }
    }

    public User.GenderInterest parseGenderInterest(String genderInterest) {
        try {
            return User.GenderInterest.valueOf(genderInterest.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + genderInterest);
        }
    }


    public User.RelationshipType parseRelationship(String relation) {
        try {
            return User.RelationshipType.valueOf(relation.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + relation);
        }
    }

    @Transactional
    public List<Interest> mapInterests(Long userID, List<String> interestNames) {
        List<Interest> availableInterests = interestService.getAllInterests();
        Optional<User> userOpt = userRepository.findById(userID);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return interestNames.stream()
                    .map(name -> availableInterests.stream()
                            .filter(i -> i.getName().equalsIgnoreCase(name))
                            .findFirst()
                            .orElse(null)
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Usuario con ID " + userID + " no encontrado.");
        }
    }

    public UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setBiography(user.getBiography());
        userResponse.setGender(user.getGender().toString());
        userResponse.setGenderInterest(user.getGenderInterest().toString());
        userResponse.setRelationshipType(user.getRelationshipType().toString());
        userResponse.setPrivacySetting(user.getPrivacySetting().toString());
        userResponse.setRegistrationDate(user.getRegistrationDate().toString());
        userResponse.setStatus(user.isStatus());
        List<String> pictureUrls = new ArrayList<>();
        for (UserPicture picture : user.getPictures()) {
            pictureUrls.add(picture.getImagePath());
        }
        userResponse.setPictureUrls(pictureUrls);
        return userResponse;
    }


    @Transactional
    public void saveUserPictures(Long userId, List<String> imagePaths) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (String imagePath : imagePaths) {
            UserPicture userPicture = new UserPicture(user, imagePath);
            userPictureRepository.save(userPicture);
        }
    }

    public String EncriptPassword(String pass) {
        return passwordEncoder.encode(pass);
    }


    public List<String> getUserPhotoUrls(Long userId) {
        List<UserPicture> photos = userPictureRepository.findByUserId(userId);

        return photos.stream()
                .map(photo -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/pictures/")
                        .path(photo.getImagePath())
                        .toUriString())
                .collect(Collectors.toList());
    }
}
