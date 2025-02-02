package com.emma.Blaze.controller;

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
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LocationService locationService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(userService::mapUserToUserResponse).toList();
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve la información de un usuario en base a su ID.")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(userService.mapUserToUserResponse(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email", description = "Devuelve la información de un usuario en base a su email si está activo.")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent() && userOptional.get().isStatus()) {
            return ResponseEntity.ok(userService.mapUserToUserResponse(userOptional.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "Obtener usuario por teléfono", description = "Devuelve la información de un usuario en base a su número de teléfono.")
    public ResponseEntity<UserResponse> getUserByPhone(@PathVariable String phone) {
        Optional<User> userOptional = userService.getUserByPhone(phone);
        return userOptional.map(user -> ResponseEntity.ok(userService.mapUserToUserResponse(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar un nuevo usuario", description = "Crea un nuevo usuario y lo guarda en la base de datos.")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest createUser) {
        if (createUser.getProfilePictures() == null) createUser.setProfilePictures(List.of());
        if (createUser.getInterests() == null) createUser.setInterests(List.of());

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
        user.setMatchesAsUser2(List.of());
        user.setMatchesAsUser1(List.of());
        user.setSwipes(List.of());
        user.setStatus(true);

        User savedUser = userService.createUser(user);
        savedUser.setLocation(locationService.saveLocation(userService.createLocation(savedUser, createUser.getLocation())));
        userService.saveUserPictures(
                savedUser.getUserId(),
                (createUser.getProfilePictures() == null || createUser.getProfilePictures().isEmpty())
                        ? userService.createDefaultImages()
                        : createUser.getProfilePictures()
        );
        savedUser.setInterests(userService.mapUsInterest(savedUser.getUserId(), createUser.getInterests()));
        userService.updateUser(savedUser);

        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(userService.mapUserToUserResponse(savedUser));
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario en base a su ID.")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUser) {
        Optional<User> existingUserOpt = userService.getUserById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User existingUser = existingUserOpt.get();
        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getBirthDate() != null) existingUser.setBirthDate(userService.parseBirthdateToLocalDate(updatedUser.getBirthDate()));
        if (updatedUser.getGender() != null) existingUser.setGender(userService.parseGender(updatedUser.getGender()));
        if (updatedUser.getRelationshipType() != null) existingUser.setRelationshipType(userService.parseRelationship(updatedUser.getRelationshipType()));
        if (updatedUser.getGenderInterest() != null) existingUser.setGenderInterest(userService.parseGenderInterest(updatedUser.getGenderInterest()));
        if (updatedUser.getBiography() != null) existingUser.setBiography(updatedUser.getBiography());
        if (updatedUser.getPrivacySetting() != null) existingUser.setPrivacySetting(userService.parsePrivacySetting(updatedUser.getPrivacySetting()));
        if (updatedUser.isStatus() != existingUser.isStatus()) existingUser.setStatus(updatedUser.isStatus());

        User savedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(userService.mapUserToUserResponse(savedUser));
    }

    @GetMapping("/{id}/photos")
    @Operation(summary = "Obtener fotos del usuario", description = "Devuelve una lista con las URLs de las fotos de un usuario.")
    public ResponseEntity<List<String>> getUserPhoto(@PathVariable Long id) {
        List<String> photoUrls = userService.getUserPhotoUrls(id);
        return (photoUrls != null) ? ResponseEntity.ok(photoUrls) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Eliminar usuario", description = "Desactiva un usuario en base a su ID.")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            existingUser.get().setStatus(false);
            userService.updateUser(existingUser.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
