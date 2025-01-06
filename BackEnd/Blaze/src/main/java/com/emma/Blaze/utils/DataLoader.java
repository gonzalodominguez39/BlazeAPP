package com.emma.Blaze.utils;
import com.emma.Blaze.service.InterestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    @Autowired
    private InterestService interesService;

    public DataLoader() {

    }

    @PostConstruct
    public void init() {
        interesService.uploadInterest();
        System.out.println("Intereses cargados al iniciar la aplicación.");
    }
}
