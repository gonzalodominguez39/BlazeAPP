package com.emma.Blaze.controller;


import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import com.emma.Blaze.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;


    @GetMapping
    public List<Interest> getAllInterest() {
        return interestService.getAllInterests();
    }
}
