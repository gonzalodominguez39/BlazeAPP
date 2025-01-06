package com.emma.Blaze.repository;

import com.emma.Blaze.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.user.userId = :userId")
    Optional<Location> findByUserId(Long userId);


    @Modifying
    @Transactional
    @Query("UPDATE Location l SET l.latitude = :latitude, l.longitude = :longitude WHERE l.user.userId= :userId")
    void updateLocation(Double latitude, Double longitude, Long userId);

}
