package com.emma.Blaze.service;


import com.emma.Blaze.model.Picture;
import com.emma.Blaze.repository.PictureRepository;
import org.springframework.stereotype.Service;

@Service
public class PictureService {

    private final PictureRepository userPictureRepository;


    public PictureService(PictureRepository userPictureRepository) {
        this.userPictureRepository = userPictureRepository;
    }

    public Picture createPicture(Picture picture) {
        return userPictureRepository.save(picture);
    }

}
