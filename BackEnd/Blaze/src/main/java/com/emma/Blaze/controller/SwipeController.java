package com.emma.Blaze.controller;

import com.emma.Blaze.requestresponse.SwipeRequest;
import com.emma.Blaze.requestresponse.SwipeResponse;
import com.emma.Blaze.service.MatchService;
import com.emma.Blaze.service.SwipeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swipes")
public class SwipeController {
    @Autowired
    private SwipeService swipeService;
    @Autowired
    private MatchService matchService;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveSwipe(@RequestBody SwipeRequest swipeDto) {
        try {
            SwipeResponse swipeCreated = swipeService.createSwipe(swipeDto);
            Boolean match = matchService.checkForMatch(swipeDto.getUserId(), swipeDto.getSwipedUserId());
            System.out.println("match" + match);
            return ResponseEntity.status(HttpStatus.CREATED).body(match);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
