package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.EtatDesLieux;
import org.application.propertymanag.form.appart.etat.UpdateEtatForm;
import org.application.propertymanag.form.validator.EtatValidator;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@Tag(name = "État des lieux")
@RequestMapping("/app/appart/etat")
public class EtatController implements PathConfig {

    private AppartServiceImpl appartService;
    private final EtatValidator validator = new EtatValidator();

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService) {
        this.appartService = appartService;
    }

    @GetMapping("/{idAppart}")
    @Operation(summary = "Liste des états des lieux")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la liste des états des lieux"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(@Parameter(description = "ID de l'appartement") @PathVariable(value = "idAppart") Integer idAppart, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<EtatDesLieux> listOfEtats = appartService.getListOfEtats().stream().filter(etatDesLieux -> etatDesLieux.getIdAppart().getIdAppart().equals(idAppart)).toList();
        model.addAttribute("listOfEtats", listOfEtats);
        return "app/appart/etat/home";
    }

    @GetMapping("/editEtat/{idEtat}")
    @Operation(summary = "À propos d'un état des lieux")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération des informations d'un état des lieux"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getEditEtat(@Parameter(description = "ID de l'état des lieux") @PathVariable(name = "idEtat") Integer idEtat, HttpServletResponse response, Model model) throws IOException {
        try {
            EtatDesLieux etat = appartService.getEtatById(idEtat);
            model.addAttribute("etat", etat);
            model.addAttribute("appName", APP_NAME);
            return "app/appart/etat/edit_etat";

        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "app/appart/etat/home";
        }
    }

    @PostMapping(value = "/editEtat", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Modifier un état des lieux")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification d'un état des lieux"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le UpdateEtatForm")
    })
    public String editEtat(@ModelAttribute @Valid UpdateEtatForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"one\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editEtat(appartService, form);
        }
    }
}
