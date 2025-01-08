package com.emma.Blaze.repository;

import com.emma.Blaze.model.UserPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPictureRepository extends JpaRepository<UserPicture, Long> {

    @Override
    UserPicture save(UserPicture picture);

    @Query("SELECT up FROM UserPicture up WHERE up.user.id = :userId")
    List<UserPicture> findByUserId(@Param("userId") Long userId);
}
