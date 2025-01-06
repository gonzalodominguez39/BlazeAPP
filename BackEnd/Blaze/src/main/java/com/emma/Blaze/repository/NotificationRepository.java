package com.emma.Blaze.repository;

import com.emma.Blaze.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {



    boolean existsById(Long notificationId);
}
