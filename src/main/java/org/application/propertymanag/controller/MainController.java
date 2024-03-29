package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@Tag(name = "Application")
public class MainController {

    @Value("${config.application.name}")
    public String APP_NAME;

    @Value("${config.application.path}")
    public String APP_PATH;

    @Value("${config.agence.id}")
    public Integer AGENCY_ID;

    private AppartServiceImpl appartService;

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService) {
        this.appartService = appartService;
    }

    @GetMapping("/")
    @Operation(summary = "Page d'accueil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page d'accueil"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getIndex(Authentication authentication, HttpServletResponse response) throws IOException {
        if(authentication == null) {
            response.sendRedirect("/auth");
            return "index";
        } else {
            response.sendRedirect(APP_PATH + "/home");
            return "app/loc/home";
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
            model.addAttribute("agencyName", appartService.getNameAgency(AGENCY_ID));
            return "app/header";
    }

}
