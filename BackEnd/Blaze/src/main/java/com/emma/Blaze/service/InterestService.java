package com.emma.Blaze.service;



import com.emma.Blaze.model.Interest;
import com.emma.Blaze.repository.InterestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class InterestService {

    private final InterestRepository interestRepository;

    @Autowired
    public InterestService(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    public Optional<Interest> getInterestById(Long interestId) {
        return interestRepository.findById(interestId);
    }

    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }

    public Interest updateInterest(Interest interest) {
        if (interestRepository.existsById(interest.getInterestId())) {
            return interestRepository.save(interest);
        }
        throw new RuntimeException("Interest not found");
    }
    @Transactional
    public void uploadInterest() {
        List<String> intereses = Arrays.asList(
                "Programación", "Senderismo", "Fotografía", "Videojuegos", "Música",
                "Viajes", "Lectura", "Deportes", "Pintura", "Cocina", "Yoga", "Correr",
                "Bailar", "Escritura", "Jardinería", "Natación", "Ciclismo", "Fitness",
                "Tecnología", "Cine", "Teatro", "Arte", "Historia", "Ciencia", "Astronomía",
                "Voluntariado", "Idiomas", "Educación", "Manualidades", "Diseño", "Moda",
                "Meditación", "Escalada", "Pesca", "Ajedrez", "Coleccionismo", "Animales",
                "Robótica", "Cómics", "Automovilismo", "Inversiones", "Podcasting", "Blogging"
        );

        for (String nombreInteres : intereses) {
            if (interestRepository.findAll().stream().noneMatch(i -> i.getName().equals(nombreInteres))) {
                Interest interest = new Interest();
                interest.setName(nombreInteres);
                interestRepository.save(interest);
                System.out.println("Interés insertado: " + nombreInteres);
            }
        }
    }

}
