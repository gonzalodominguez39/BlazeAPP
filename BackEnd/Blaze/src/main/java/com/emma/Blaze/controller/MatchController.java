package com.emma.Blaze.controller;



import com.emma.Blaze.dto.MatchResponse;
import com.emma.Blaze.model.UserMatch;
import com.emma.Blaze.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Obtener coincidencias de un usuario", description = "Devuelve una lista de coincidencias para un usuario dado su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de coincidencias obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}")
    public List<MatchResponse> getAllMatchesByUserId(@PathVariable Long userId) {
        return matchService.getAllMatchesByUserId(userId);
    }
}
