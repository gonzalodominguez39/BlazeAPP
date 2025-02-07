package com.emma.Blaze.config;

import com.emma.Blaze.dto.LocationRequest;
import com.emma.Blaze.dto.UserRequest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.service.LocationService;
import com.emma.Blaze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;

@Configuration
public class UserInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    private static final Set<String> usedPhoneNumbers = new HashSet<>();

    @Bean
    public ApplicationRunner initializeUsers() {
        return args -> {
            List<String> femaleNames = List.of("Mariana", "Eliana", "Sofia", "Lucia", "Valeria");
            List<String> maleNames = List.of("Carlos", "Juan", "Andres", "Mateo", "Luis");
            String baseEmail = "user%d@example.com";

            // Crear 5 mujeres
            for (int i = 0; i < 5; i++) {
                createUserIfNotExists(femaleNames.get(i), String.format(baseEmail, i), "FEMALE", "MALE");
            }

            // Crear 5 hombres
            for (int i = 5; i < 10; i++) {
                createUserIfNotExists(maleNames.get(i - 5), String.format(baseEmail, i), "MALE", "FEMALE");
            }
        };
    }

    private void createUserIfNotExists(String name, String email, String gender, String genderInterest) {
        Optional<User> existingUser = userService.getUserByEmail(email);
        if (existingUser.isPresent()) {
            System.out.println("⚠️ El usuario con email " + email + " ya existe. No se creará nuevamente.");
            return;
        }

        String phoneNumber;
        do {
            phoneNumber = "+341234567" + (int) (Math.random() * 90 + 10);
        } while (usedPhoneNumbers.contains(phoneNumber));
        usedPhoneNumbers.add(phoneNumber);

        UserRequest createUser = new UserRequest();
        createUser.setName(name);
        createUser.setEmail(email);
        createUser.setPhoneNumber(phoneNumber);
        createUser.setBirthDate("1990-01-01");
        createUser.setGender(gender);
        createUser.setGenderInterest(genderInterest);
        createUser.setBiography("Usuario de prueba: " + name);
        createUser.setPassword("password123");
        createUser.setRelationshipType("CASUAL");
        createUser.setInterests(List.of("Música", "Deportes"));

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setLatitude(-27.7843019);
        locationRequest.setLongitude(-64.2566413);
        createUser.setLocation(locationRequest);


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


        userService.saveUserPictures(savedUser.getUserId(), userService.createDefaultImages());


        savedUser.setInterests(userService.mapUsInterest(savedUser.getUserId(), createUser.getInterests()));


        userService.updateUser(savedUser);

        System.out.println("✅ Usuario de prueba " + name + " creado correctamente.");
    }
}
