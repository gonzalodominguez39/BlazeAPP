package com.emma.Blaze.repository;

import com.emma.Blaze.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Override
    Picture save(Picture picture);

    @Query("SELECT p FROM Picture p WHERE p.user.id = :userId")
    List<Picture> findByUserId(@Param("userId") Long userId);
}
