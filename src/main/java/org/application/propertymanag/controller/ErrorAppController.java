package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Erreur")
public class ErrorAppController implements ErrorController {

    @GetMapping("/error")
    @Operation(summary = "Page d'erreur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Affichage de la page d'erreur 403 - Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Affichage de la page d'erreur 404 - Page introuvable")
    })
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        } else if(status != null && Integer.parseInt(status.toString()) == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        }
        return "error";
    }

}
