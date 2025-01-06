package com.emma.Blaze.service;


import com.emma.Blaze.model.User_Picture;
import com.emma.Blaze.repository.UserPictureRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPictureService {

    private final UserPictureRepository userPictureRepository;


    public UserPictureService(UserPictureRepository userPictureRepository) {
        this.userPictureRepository = userPictureRepository;
    }

    public User_Picture createPicture(User_Picture picture) {
        return userPictureRepository.save(picture);
    }
}
