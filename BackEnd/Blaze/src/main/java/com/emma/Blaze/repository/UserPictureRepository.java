package com.emma.Blaze.repository;

import com.emma.Blaze.model.User_Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPictureRepository extends JpaRepository<User_Picture, Long> {

    @Override
    User_Picture save(User_Picture picture);
}
