package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.DepotDeGarantie;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@Tag(name = "Dépôt de garantie")
@RequestMapping("/app/appart/depotGarantie")
public class GarantieController implements PathConfig {

    private AppartServiceImpl appartService;

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService) {
        this.appartService = appartService;
    }

    @GetMapping("/{idAppart}")
    @Operation(summary = "Liste des dépôts de garantie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la liste des dépôts de garantie"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(@Parameter(description = "ID de l'appartement") @PathVariable(value = "idAppart") Integer idAppart, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<DepotDeGarantie> listOfDepots = appartService.getListOfDepots().stream().filter(
                garantie -> garantie.getIdAppart().getIdAppart().equals(idAppart)).toList();

        model.addAttribute("listOfDepots", listOfDepots);
        return "app/appart/garantie/home";
    }

    @PostMapping(value = "/validDepot")
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Valider le reçu d'un dépôt de garantie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation d'un dépôt de garantie"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Ce dépôt de garantie n'existe pas")
    })
    public void editDepot(@Parameter(description = "ID du dépôt de garantie") @RequestParam(name = "idDepot") Integer idDepot, HttpServletResponse response) throws IOException {
        if(idDepot != null) {
            DepotDeGarantie d = appartService.getDepotById(idDepot);
            d.setStatut(true);
            appartService.createDepot(d);
            response.sendRedirect("app/appart/depotGarantie/"+d.getIdAppart().getIdAppart());
        }
    }

}
