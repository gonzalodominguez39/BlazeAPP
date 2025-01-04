package com.emma.Blaze.service;



import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.relationship.UserInterest;
import com.emma.Blaze.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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


}
