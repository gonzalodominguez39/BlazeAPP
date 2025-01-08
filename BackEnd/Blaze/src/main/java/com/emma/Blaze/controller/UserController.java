package com.emma.Blaze.controller;

import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.model.UserPicture;
import com.emma.Blaze.request.UserRequest;
import com.emma.Blaze.request.UserResponse;
import com.emma.Blaze.service.EmailService;
import com.emma.Blaze.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : users) {
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

            userResponseList.add(userResponse);
        }

        return userResponseList;
    }
    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody UserRequest createUser) {
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
        user.setLocation(null);
        user.setInterests(null);
        user.setMatchesAsUser2(null);
        user.setMatchesAsUser1(null);
        user.setSwipes(null);
        System.out.println("profilePictures" + createUser.getProfilePictures());
        //user.setRelationshipType(userService.parseRelationship(createUser.getRelationshipType()));
        //user.setPrivacySetting(createUser.getPrivacySetting());
        user.setStatus(createUser.isStatus());
        User savedUser = userService.createUser(user);
        userService.saveUserPictures(savedUser.getUserId(),createUser.getProfilePictures());
        List<Interest> interests = userService.mapUsInterest(savedUser.getUserId(), createUser.getInterests());
        savedUser.setInterests(interests);  // Asignar los intereses ya mapeados
        userService.updateUser(savedUser);
        // Enviar el correo de bienvenida
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setBirthDate(updatedUser.getBirthDate());
            user.setGender(updatedUser.getGender());
            user.setGenderInterest(updatedUser.getGenderInterest());
            user.setBiography(updatedUser.getBiography());
            //user.setProfilePicture(updatedUser.getProfilePicture());
           // user.setRelationshipType(updatedUser.getRelationshipType());
            user.setPrivacySetting(updatedUser.getPrivacySetting());
            user.setStatus(updatedUser.isStatus());
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            existingUser.get().setStatus(false);
            User newUser = existingUser.get();
            userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
