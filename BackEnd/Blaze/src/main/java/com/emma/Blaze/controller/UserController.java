package com.emma.Blaze.controller;

import com.emma.Blaze.model.User;
import com.emma.Blaze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
   public List<User>getAllUsers() {
   return  userService.getAllUsers();
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User createUser) {
            User user= new User();
            user.setName(createUser.getName());
            user.setEmail(createUser.getEmail());
            user.setPhoneNumber(createUser.getPhoneNumber());
            user.setBirthDate(createUser.getBirthDate());
            user.setGender(createUser.getGender());
            user.setGenderInterest(createUser.getGenderInterest());
            user.setBiography(createUser.getBiography());
            user.setProfilePicture(createUser.getProfilePicture());
            user.setRelationshipType(createUser.getRelationshipType());
            user.setPrivacySetting(createUser.getPrivacySetting());
            user.setStatus(createUser.isStatus());
            User savedUser = userService.createUser(user);
        if (savedUser!=null) {
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
            user.setProfilePicture(updatedUser.getProfilePicture());
            user.setRelationshipType(updatedUser.getRelationshipType());
            user.setPrivacySetting(updatedUser.getPrivacySetting());
            user.setStatus(updatedUser.isStatus());
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
