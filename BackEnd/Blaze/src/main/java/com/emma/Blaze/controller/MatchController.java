package com.emma.Blaze.controller;



import com.emma.Blaze.dto.MatchResponse;
import com.emma.Blaze.model.UserMatch;
import com.emma.Blaze.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/{userId}")
    public List<MatchResponse> getAllMatchesByUserId(@PathVariable Long userId) {
        return matchService.getAllMatchesByUserId(userId);
    }
}
