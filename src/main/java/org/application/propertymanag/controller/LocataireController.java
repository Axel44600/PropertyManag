package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.form.locataire.CreateLocForm;
import org.application.propertymanag.form.locataire.UpdateLocForm;
import org.application.propertymanag.form.validator.LocataireValidator;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@Tag(name = "Locataire")
@RequestMapping("/app")
public class LocataireController implements PathConfig {

    private AdminServiceImpl adminService;
    private LocataireServiceImpl locataireService;
    private MainServiceImpl mainService;
    private final LocataireValidator validator = new LocataireValidator();

    @Autowired
    public void setInjectedBean(AdminServiceImpl adminService, LocataireServiceImpl locataireService, MainServiceImpl mainService) {
        this.adminService = adminService;
        this.locataireService = locataireService;
        this.mainService = mainService;
    }

    @GetMapping("/home")
    @Operation(summary = "Page locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page locataire"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(Authentication authentication, Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("userFirstName", adminService.getUserByPseudo(authentication.getName()).getPrenom());
        return "/app/loc/home";
    }

    @GetMapping("/data/listOfLocs")
    @Operation(summary = "Liste des locataires")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération de la liste des locataires"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getListOfLocataires(Model model) {
        model.addAttribute("listOfLocataires", locataireService.getListOfLocataires());
        return "/app/loc/data/list_locataires";
    }

    @GetMapping("/editLocataire/{lastName}")
    @Operation(summary = "À propos d'un locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération des informations d'un locataire"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getEditLoc(@Parameter(description = "Nom du locataire") @PathVariable(name = "lastName") String nom, Authentication authentication, Model model) {
         model.addAttribute("locataire", locataireService.getLocataireByNom(nom));
         model.addAttribute("appName", APP_NAME);
         model.addAttribute("pseudo", authentication.getName());
         return "/app/loc/edit_locataire";
    }

    @PostMapping(value = "/researchLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Rechercher un locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locataire trouvé, affichage de ses informations"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Ce locataire n'existe pas")
    })
    public String findLoc(@Parameter(description = "Nom du locataire") @RequestParam(name = "name") String lastName) {
        String realLastName = mainService.maj(lastName);
        boolean lastNameFound = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getNom().equals(realLastName));

        if(lastNameFound){
            Locataire l = locataireService.getLocataireByNom(realLastName);
            return "{" +
                    "\"success\": \"yes\"," +
                    "\"id\": \""+l.getIdLoc()+"\"," +
                    "\"nom\": \""+l.getNom()+"\"," +
                    "\"prenom\": \""+l.getPrenom()+"\"," +
                    "\"email\": \""+l.getEmail()+"\"," +
                    "\"url\": \"./editLocataire/"+l.getNom()+"\"," +
                    "\"urlBilan\": \"./bilan/"+l.getNom()+"\"}";
        } else {
            return "{\"success\": \"no\"}";
        }
    }

    @PostMapping(value = "/createLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Créer un profil locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un profil locataire"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le CreateLocForm")
    })
    public String createLoc(@ModelAttribute @Valid CreateLocForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.createLoc(mainService, locataireService, form);
        }
    }

    @PostMapping(value = "/editLocataire", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    @Operation(summary = "Modifier le profil d'un locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification d'un profil locataire"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le UpdateLocForm")
    })
    public String editLoc(@ModelAttribute @Valid UpdateLocForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editLoc(mainService, locataireService, form);
        }
}


}
