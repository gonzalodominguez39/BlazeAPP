package com.emma.Blaze.service;


import org.springframework.stereotype.Service;

@Service
public class UserInterestService {

  /*  private final UserInterestRepository userInterestRepository;

    @Autowired
    public UserInterestService(UserInterestRepository userInterestRepository) {
        this.userInterestRepository = userInterestRepository;
    }

    public UserInterest createUserInterest(UserInterest userInterest) {
        return userInterestRepository.save(userInterest);
    }

    public Optional<UserInterest> getUserInterestById(Long userId, Long interestId) {
        return userInterestRepository.findById(userId, interestId);
    }

    public List<UserInterest> getUserInterestsByUserId(Long userId) {
        return userInterestRepository.findByUserId(userId);
    }

    public void deleteUserInterest(Long userId, Long interestId) {
        if (userInterestRepository.existsById(userId, interestId)) {
            userInterestRepository.deleteByUserId(userId);
        } else {
            throw new RuntimeException("User Interest not found");
        }
    }*/
}
