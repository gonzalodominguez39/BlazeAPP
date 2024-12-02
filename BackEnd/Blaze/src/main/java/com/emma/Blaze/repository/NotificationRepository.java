package com.emma.Blaze.repository;

import com.emma.Blaze.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId")
    List<Notification> findByUserId(Long userId);


    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId AND n.isRead = false")
    List<Notification> findByUserIdAndIsReadFalse(Long userId);


    @Query("SELECT n FROM Notification n WHERE n.notificationId = :notificationId AND n.user.userId = :userId")
    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);


    boolean existsById(Long notificationId);
}
