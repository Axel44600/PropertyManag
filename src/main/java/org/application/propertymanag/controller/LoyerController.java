package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.entity.Loyer;
import org.application.propertymanag.form.appart.loyer.LoyerForm;
import org.application.propertymanag.form.validator.LoyerValidator;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@Tag(name = "Loyer")
@RequestMapping("/app/appart/loyer")
public class LoyerController implements PathConfig {

    private AppartServiceImpl appartService;
    private MainServiceImpl mainService;
    private LocataireServiceImpl locataireService;
    private final LoyerValidator validator = new LoyerValidator();

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService, MainServiceImpl mainService, LocataireServiceImpl locataireService) {
        this.appartService = appartService;
        this.mainService = mainService;
        this.locataireService = locataireService;
    }

    @GetMapping("/{idAppart}")
    @Operation(summary = "Page loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page loyer"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(@Parameter(description = "ID de l'appartement") @PathVariable(value = "idAppart") Integer idAppart, Model model) {
        List<Loyer> listOfLoyers = appartService.getListOfLoyers().stream().filter(
                loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart)).toList();
        List<Loyer> listOfLoyersPayed = appartService.getListOfLoyers().stream().filter(
                loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart) && loyer.getStatut().equals(true)).toList();
        Appartement a = appartService.getAppartById(idAppart);

        model.addAttribute("appName", APP_NAME);
        model.addAttribute("appart", a);
        model.addAttribute("listOfLoyers", listOfLoyers);
        model.addAttribute("listOfLoyersPayed", listOfLoyersPayed);
        return "app/appart/loyer/home";
    }

    @GetMapping("/data/listOfLoyers/{idAppart}")
    @Operation(summary = "Liste des loyers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération de la liste des loyers"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getListOfLoyers(@Parameter(description = "ID de l'appartement") @PathVariable(value = "idAppart") Integer idAppart, Model model) {
        List<Loyer> listOfLoyers = appartService.getListOfLoyers().stream().filter(loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart)).toList();
        model.addAttribute("listOfLoyers", listOfLoyers);
        return "app/appart/loyer/data/list_loyers";
    }

    @GetMapping("/editLoyer/{idLoyer}")
    @Operation(summary = "À propos d'un loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération des informations d'un loyer"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getEditLoyer(@Parameter(description = "ID du loyer") @PathVariable(name = "idLoyer") Integer idLoyer, HttpServletResponse response, Model model) throws IOException {
        if(appartService.getLoyerById(idLoyer) != null) {
            Loyer l = appartService.getLoyerById(idLoyer);
            model.addAttribute("loyer", l);
            model.addAttribute("appart", appartService.getAppartById(l.getIdAppart().getIdAppart()));
            model.addAttribute("appName", APP_NAME);
            return "app/appart/loyer/edit_loyer";
        } else {
            response.sendRedirect("/app/appart/loyer/home");
            return "app/appart/loyer/home";
        }
    }

    @PostMapping(value = "/researchLoyer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Rechercher un loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loyer trouvé, affichage des informations du loyer"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Aucun loyer trouvé à cette date")
    })
    public String findLoyer(@Parameter(description = "Date du loyer") @RequestParam(name = "dateL") LocalDate date, @Parameter(description = "ID de l'appartement") @RequestParam(name = "idAppart") Integer idAppart) {
        if(date != null){
            Loyer l = appartService.getLoyerByDate(date);
            if(l != null && l.getIdAppart().getIdAppart().equals(idAppart)) {
                String statut;
                if(Boolean.TRUE.equals(l.getStatut())) {
                    statut = "paid";
                } else {
                    statut = "unpaid";
                }
                return "{" +
                        "\"success\": \"yes\"," +
                        "\"date\": \"" + l.getDate() + "\"," +
                        "\"ref\": \"" + l.getRef() + "\"," +
                        "\"montant\": \"" + l.getMontant() + "\"," +
                        "\"statut\": \"" + statut + "\"," +
                        "\"idLoyer\": \"" + l.getIdLoyer() + "\"," +
                        "\"urlEdit\": \"./editLoyer/" + l.getIdLoyer() + "\"}";
            } else {
                return "{\"success\": \"no\"}";
            }
        } else {
            return "{\"error\": \"one\"}";
        }
    }

    @PostMapping(value = "/createLoyer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Créer un loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un loyer"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le LoyerForm")
    })
    public String createLoyer(@ModelAttribute @Valid LoyerForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.createLoyer(appartService, mainService, locataireService, form);
        }
    }

    @PostMapping(value = "/editLoyer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Modifier un loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification d'un loyer"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le LoyerForm")
    })
    public String editLoyer(@ModelAttribute @Valid LoyerForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editLoyer(appartService, locataireService, form);
        }
}

    @DeleteMapping(value = "/deleteLoyer")
    @Operation(summary = "Supprimer un loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppression d'un loyer"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Cet loyer n'existe pas")
    })
    public void deleteLoyer(@Parameter(description = "ID du loyer") @RequestParam("idLoyer") Integer idLoyer, HttpServletResponse response) throws IOException {
        if(idLoyer != null && appartService.getLoyerById(idLoyer) != null) {
                Loyer loyer = appartService.getLoyerById(idLoyer);
                if(Boolean.FALSE.equals(loyer.getStatut())) {
                    Locataire locataire = locataireService.getLocataireById(loyer.getIdAppart().getIdLoc().getIdLoc());
                    int solde = locataire.getSolde() + loyer.getMontant();
                    locataire.setSolde(solde);
                }
                appartService.deleteLoyer(loyer);
                response.sendRedirect("/app/appart/loyer/"+loyer.getIdAppart().getIdAppart());
        }
    }

    @PostMapping(value = "/createQuittance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Générer une quittance de loyer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Génération d'une quittance de loyer"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Aucun loyer n'a été trouver entre ces deux dates")
    })
    public String createQuittance(@Parameter(description = "ID de l'appartement") @RequestParam(name = "idAppart") Integer idAppart,
                                  @Parameter(description = "Date du premier loyer") @RequestParam(name = "dateD") LocalDate dateD,
                                  @Parameter(description = "Date du dernier loyer") @RequestParam(name = "dateF") LocalDate dateF) throws IOException {

        if(idAppart != null && dateD != null && dateF != null) {
            return validator.createQuittance(appartService, locataireService, mainService, idAppart, dateD, dateF);
        } else {
            return "{\"error\": \"yes\"}";
        }

    }

}

