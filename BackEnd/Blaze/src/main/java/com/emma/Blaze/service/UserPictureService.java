package com.emma.Blaze.service;


import com.emma.Blaze.model.UserPicture;
import com.emma.Blaze.repository.UserPictureRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPictureService {

    private final UserPictureRepository userPictureRepository;


    public UserPictureService(UserPictureRepository userPictureRepository) {
        this.userPictureRepository = userPictureRepository;
    }

    public UserPicture createPicture(UserPicture picture) {
        return userPictureRepository.save(picture);
    }

}
