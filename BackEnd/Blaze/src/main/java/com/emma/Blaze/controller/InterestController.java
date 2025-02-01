package com.emma.Blaze.controller;


import com.emma.Blaze.model.Interest;
import com.emma.Blaze.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;


    @Operation(summary = "Obtener todos los intereses", description = "Devuelve una lista de todos los intereses disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de intereses obtenida exitosamente")
    @GetMapping
    public List<Interest> getAllInterest() {
        return interestService.getAllInterests();
    }
}
