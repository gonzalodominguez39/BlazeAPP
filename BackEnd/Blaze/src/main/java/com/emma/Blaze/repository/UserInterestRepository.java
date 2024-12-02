package com.emma.Blaze.repository;

import com.emma.Blaze.relationship.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {



    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.userId = :userId")
    List<UserInterest> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(ui) > 0 THEN true ELSE false END FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.interest.id = :interestId")
    boolean existsById(Long userId, Long interestId);

    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.interest.id = :interestId")
    Optional<UserInterest> findById(Long userId, Long interestId);
}
