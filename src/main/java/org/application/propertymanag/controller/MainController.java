package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
@Tag(name = "Application")
public class MainController implements PathConfig {

    @GetMapping
    @Operation(summary = "Page d'accueil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page d'accueil"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getIndex(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "index";
        } else {
            response.sendRedirect(APP_PATH + "/home");
            return "/app/loc/home";
        }
    }

    @GetMapping("/app/header")
    @Operation(summary = "Menu vertical")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération du menu vertical"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHeader(Authentication authentication, Model model) {
            String role = authentication.getAuthorities().toString();
            model.addAttribute("role", role.substring(1, role.length()-1));
            model.addAttribute("appName", APP_NAME);
            return "/app/header";
    }

}
