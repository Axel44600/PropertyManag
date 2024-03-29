package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.entity.*;
import org.application.propertymanag.form.appart.CreateAppartForm;
import org.application.propertymanag.form.appart.UpdateAppartForm;
import org.application.propertymanag.form.validator.AppartValidator;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@Tag(name = "Appartement")
@RequestMapping("/app")
public class AppartController {

    private AppartServiceImpl appartService;
    private LocataireServiceImpl locataireService;
    private MainServiceImpl mainService;
    private final AppartValidator validator = new AppartValidator();

    @Value("${config.application.name}")
    public String APP_NAME;

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService, LocataireServiceImpl locataireService, MainServiceImpl mainService) {
        this.appartService = appartService;
        this.locataireService = locataireService;
        this.mainService = mainService;
    }

    @GetMapping("/appart")
    @Operation(summary = "Page appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page appartement"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("listOfApparts", appartService.getListOfApparts());
        return "app/appart/home";
    }

    @GetMapping("/data/listOfApparts")
    @Operation(summary = "Liste des appartements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération de la liste des appartements"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getListOfApparts(Model model) {
        model.addAttribute("listOfApparts", appartService.getListOfApparts());
        return "app/appart/data/list_apparts";
    }

    @GetMapping("/editAppart/{idAppart}")
    @Operation(summary = "À propos d'un appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération des informations d'un appartement"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getEditApart(@Parameter(description = "ID de l'appartement") @PathVariable(name = "idAppart") Integer idAppart, HttpServletResponse response, Model model) throws IOException {
        try {
            Appartement appart = appartService.getAppartById(idAppart);
            model.addAttribute("appart", appart);
            List<Locataire> listOfLocataires;

            if(appart.getIdLoc() != null)  {
                listOfLocataires = locataireService.getListOfLocataires().stream().filter(
                        locataire -> !Objects.equals(locataire.getIdLoc(), appart.getIdLoc().getIdLoc())).toList();
            } else {
                listOfLocataires = locataireService.getListOfLocataires();
            }
            model.addAttribute("listOfLocataires", listOfLocataires);
            model.addAttribute("appName", APP_NAME);
            return "app/appart/edit_appart";
        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "app/loc/home";
        }
    }

    @PostMapping(value = "/researchAppart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Rechercher un appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appartement trouvé, affichage des informations de l'appartement"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Cet appartement n'existe pas")
    })
    public String findApart(@Parameter(description = "Adresse de l'appartement") @RequestParam(name = "address") String value) {
            boolean appartFound = appartService.getListOfApparts().stream().anyMatch(appartement -> appartement.getAdresse().equals(value));

            if (!appartFound) {
                return "{" + "\"success\": \"no\"}";
            } else {
                Appartement a = appartService.getAppartByAdresse(value);
                return "{" +
                        "\"success\": \"yes\"," +
                        "\"id\": \"" + a.getIdAppart() + "\"," +
                        "\"adresse\": \"" + a.getAdresse() + "\"," +
                        "\"urlActions\": \"action" + a.getIdAppart() + "\"," +
                        "\"urlEdit\": \"./editAppart/" + a.getIdAppart() + "\"," +
                        "\"urlSeeLoyer\": \"./appart/loyer/" + a.getIdAppart() + "\"," +
                        "\"urlSeeEtat\": \"./appart/etat/" + a.getIdAppart() + "\"," +
                        "\"urlSeeDepot\": \"./appart/depotGarantie/" + a.getIdAppart() + "\"}";
            }
    }

    @PostMapping(value = "/createAppart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Créer un appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un appartement"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le CreateAppartForm")
    })
    public String createApart(@ModelAttribute @Valid CreateAppartForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"one\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.createAppart(appartService, form);
        }
    }

    @PostMapping(value = "/editAppart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Modifier un appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification d'un appartement"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le UpdateAppartForm")
    })
    public String editApart(@ModelAttribute @Valid UpdateAppartForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editAppart(appartService, locataireService, mainService, form);
        }
    }

    @DeleteMapping(value = "/deleteAppart")
    @Operation(summary = "Supprimer un appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppression d'un appartement"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Cet appartement n'existe pas")
    })
    public void deleteApart(@Parameter(description = "ID de l'appartement") @RequestParam("idAppart") Integer idAppart, HttpServletResponse response) throws IOException {
        if(idAppart != null) {
            Appartement a = appartService.getAppartById(idAppart);
            appartService.deleteAppart(a);
            response.sendRedirect("/app/appart");
        }
    }

}
