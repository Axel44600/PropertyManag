package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Bilan;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "Bilan des comptes des loyers")
@RequestMapping("/app/bilan")
public class BilanController implements PathConfig {

    private AppartServiceImpl appartService;
    private LocataireServiceImpl locataireService;

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService, LocataireServiceImpl locataireService) {
        this.appartService = appartService;
        this.locataireService = locataireService;
    }

    @GetMapping("/{lastName}")
    @Operation(summary = "Liste des bilans des comptes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la liste des bilans"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(@Parameter(description = "Nom du locataire") @PathVariable(value = "lastName") String lastName, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<Bilan> listOfBilans = appartService.getListOfBilans().stream().filter(bilan -> bilan.getIdLoc().getNom().equals(lastName)).toList();
        Locataire locataire = locataireService.getLocataireByNom(lastName);
        model.addAttribute("listOfBilans", listOfBilans);
        model.addAttribute("locataire", locataire);
        return "/app/loc/bilan/home";
    }
}
