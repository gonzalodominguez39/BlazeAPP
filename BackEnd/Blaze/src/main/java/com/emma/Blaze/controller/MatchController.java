package com.emma.Blaze.controller;

import com.emma.Blaze.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/check/{userId}/{userId2}")
    public ResponseEntity<Boolean> checkMatch(@PathVariable long userId, @PathVariable long userId2) {
        try {
            boolean existMatch = matchService.checkForMatch(userId, userId2);
            return ResponseEntity.ok(existMatch);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
