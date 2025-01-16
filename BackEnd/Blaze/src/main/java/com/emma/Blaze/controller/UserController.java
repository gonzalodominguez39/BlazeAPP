package com.emma.Blaze.controller;

import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.requestresponse.UserRequest;
import com.emma.Blaze.requestresponse.UserResponse;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

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
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponse userResponse = userService.mapUserToUserResponse(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest createUser) {
        // Asegurarse de que las listas no sean nulas
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
        user.setLocation(null); // Asignar un valor predeterminado si es necesario
        user.setMatchesAsUser2(new ArrayList<>());
        user.setMatchesAsUser1(new ArrayList<>());
        user.setSwipes(new ArrayList<>());
        user.setStatus(createUser.isStatus());

        // Guardar el usuario
        User savedUser = userService.createUser(user);
        userService.saveUserPictures(savedUser.getUserId(), createUser.getProfilePictures());

        // Mapear los intereses y asignarlos al usuario
        List<Interest> interests = userService.mapUsInterest(savedUser.getUserId(), createUser.getInterests());
        savedUser.setInterests(interests);

        // Actualizar el usuario con los intereses mapeados
        userService.updateUser(savedUser);

        // Enviar el correo de bienvenida
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

        // Mapear el usuario a la respuesta
        userResponse = userService.mapUserToUserResponse(savedUser);

        // Retornar respuesta con el usuario guardado
        return ResponseEntity.ok(userResponse);
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
