package com.emma.Blaze.repository;

import com.emma.Blaze.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query("SELECT i FROM Interest i WHERE i.name = :name")
    Optional<Interest> findByName(String name);

    @Query("SELECT COUNT(i) > 0 FROM Interest i WHERE i.name = :name")
    boolean existsByName(String name);




}
