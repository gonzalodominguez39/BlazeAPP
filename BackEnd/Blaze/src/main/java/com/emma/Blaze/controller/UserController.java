package com.emma.Blaze.controller;

import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.Location;
import com.emma.Blaze.model.User;
import com.emma.Blaze.dto.UserRequest;
import com.emma.Blaze.dto.UserResponse;
import com.emma.Blaze.service.EmailService;
import com.emma.Blaze.service.LocationService;
import com.emma.Blaze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : users) {
            userResponseList.add(userService.mapUserToUserResponse(user));
        }
        return userResponseList;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()&&userOptional.get().isStatus()) {
            User user = userOptional.get();
            UserResponse userResponse = userService.mapUserToUserResponse(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserResponse> getUserByPhone(@PathVariable String phone){
        Optional<User> userOptional = userService.getUserByPhone(phone);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            UserResponse userResponse = userService.mapUserToUserResponse(user);
            return ResponseEntity.ok(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest createUser) {

        if (createUser.getProfilePictures() == null) {
            createUser.setProfilePictures(new ArrayList<>());
        }
        if (createUser.getInterests() == null) {
            createUser.setInterests(new ArrayList<>());
        }

        UserResponse userResponse = new UserResponse();
        User user = new User();
        user.setName(createUser.getName());
        user.setEmail(createUser.getEmail());
        user.setPhoneNumber(createUser.getPhoneNumber());
        user.setBirthDate(userService.parseBirthdateToLocalDate(createUser.getBirthDate()));
        user.setGender(userService.parseGender(createUser.getGender()));
        user.setGenderInterest(userService.parseGenderInterest(createUser.getGenderInterest()));
        user.setBiography(createUser.getBiography());
        user.setPassword(userService.EncriptPassword(createUser.getPassword()));
        user.setRelationshipType(userService.parseRelationship(createUser.getRelationshipType()));
        user.setMatchesAsUser2(new ArrayList<>());
        user.setMatchesAsUser1(new ArrayList<>());
        user.setSwipes(new ArrayList<>());
        user.setStatus(true);
        User savedUser = userService.createUser(user);
        Location location =locationService.saveLocation(userService.createLocation(savedUser,createUser.getLocation()));
        user.setLocation(location);
        userService.saveUserPictures(savedUser.getUserId(), createUser.getProfilePictures());
        List<Interest> interests = userService.mapUsInterest(savedUser.getUserId(), createUser.getInterests());
        savedUser.setInterests(interests);
        userService.updateUser(savedUser);
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
        userResponse = userService.mapUserToUserResponse(savedUser);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUser) {
        Optional<User> existingUserOpt = userService.getUserById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User existingUser = existingUserOpt.get();
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getBirthDate() != null) {
            existingUser.setBirthDate(userService.parseBirthdateToLocalDate(updatedUser.getBirthDate()));
        }
        if (updatedUser.getGender() != null) {
            existingUser.setGender(userService.parseGender(updatedUser.getGender()));
        }
        if (updatedUser.getGenderInterest() != null) {
            existingUser.setGenderInterest(userService.parseGenderInterest(updatedUser.getGenderInterest()));
        }
        if (updatedUser.getBiography() != null) {
            existingUser.setBiography(updatedUser.getBiography());
        }
        if (updatedUser.getPrivacySetting() != null) {
            existingUser.setPrivacySetting(userService.parsePrivacySetting(updatedUser.getPrivacySetting()));
        }
        if (updatedUser.isStatus() != existingUser.isStatus()) {
            existingUser.setStatus(updatedUser.isStatus());
        }

        User savedUser = userService.updateUser(existingUser);

        return ResponseEntity.ok(userService.mapUserToUserResponse(savedUser));
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<List<String>> getUserPhoto(@PathVariable Long id) {
        List<String> photoUrls = userService.getUserPhotoUrls(id);
        if (photoUrls == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photoUrls);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            existingUser.get().setStatus(false);
            User newUser = existingUser.get();
            userService.updateUser(newUser);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
