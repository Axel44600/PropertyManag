package org.application.propertymanag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.*;
import org.application.propertymanag.form.admin.CreateUserForm;
import org.application.propertymanag.form.admin.UpdateUserForm;
import org.application.propertymanag.form.validator.AdminValidator;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@Tag(name = "Administration")
@RequestMapping("/app/admin")
public class AdminController implements PathConfig {

    private AdminServiceImpl adminService;
    private MainServiceImpl mainService;
    private final AdminValidator validator = new AdminValidator();

    @Autowired
    public void setInjectedBean(AdminServiceImpl adminService, MainServiceImpl mainService) {
        this.adminService = adminService;
        this.mainService = mainService;
    }

    @GetMapping("/home")
    @Operation(summary = "Page administration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affichage de la page administration"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getHome(Model model, Authentication auth) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("user", adminService.getUserByPseudo(auth.getName()));
        return "app/admin/home";
    }

    @GetMapping("/data/listOfUsers")
    @Operation(summary = "Liste des utilisateurs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération de la liste des utilisateurs"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getListOfUsers(Model model, Authentication auth) {
        model.addAttribute("listOfUsers", adminService.getListOfUsers());
        model.addAttribute("user", adminService.getUserByPseudo(auth.getName()));
        return "app/admin/data/list_users";
    }

    @GetMapping("/editUser/{id}")
    @Operation(summary = "À propos d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération des informations d'un utilisateur"),
            @ApiResponse(responseCode = "403", description = "Accès interdit, redirection vers la page d'authentification"),
            @ApiResponse(responseCode = "404", description = "Page introuvable")
    })
    public String getEditUser(@Parameter(description = "ID de l'utilisateur") @PathVariable(name = "id") Integer id, HttpServletResponse response, Model model) throws IOException {
        try {
            Users u = adminService.getUserById(id);
            model.addAttribute("user", u);
            List<Users> listOfUsers = adminService.getListOfUsers();

            model.addAttribute("listOfUsers", listOfUsers);
            model.addAttribute("appName", APP_NAME);
            return "app/admin/edit_user";
        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "app/admin/home";
        }
    }

    @PostMapping(value = "/researchUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    @Operation(summary = "Rechercher un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé, affichage de ses informations"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Cet utilisateur n'existe pas")
    })
    public String findUser(@Parameter(description = "Nom de l'utilisateur") @RequestParam(name = "name") String nom, Authentication auth) {
        String nomM = mainService.maj(nom);
        boolean userFound = adminService.getListOfUsers().stream().anyMatch(users -> users.getNom().equals(nomM));

        if(userFound) {
            Users u = adminService.getUserByNom(nomM);
            String itsMe = "no";
            if(u.getPseudo().equals(auth.getName())) {
                itsMe = "yes";
            }
            return "{" +
                    "\"success\": \"yes\"," +
                    "\"id\": \"" + u.getId() + "\"," +
                    "\"nom\": \"" + u.getNom() + "\"," +
                    "\"prenom\": \"" + u.getPrenom() + "\"," +
                    "\"pseudo\": \"" + u.getPseudo() + "\"," +
                    "\"role\": \"" + u.getRole().name() + "\"," +
                    "\"itsMe\": \"" + itsMe + "\"," +
                    "\"urlEdit\": \"./editUser/" + u.getId() + "\"}";
        } else {
            return "{" + "\"success\": \"no\"}";
        }
    }

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    @Operation(summary = "Créer un compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un compte utilisateur"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le CreateUserForm")
    })
    public String createUser(@ModelAttribute @Valid CreateUserForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.createUser(adminService, mainService, form);
        }
    }

    @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    @Operation(summary = "Modifier un compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification d'un compte utilisateur"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "L'une des informations saisies ne respectent pas le UpdateUserForm")
    })
    public String editUser(@ModelAttribute @Valid UpdateUserForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                        "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editUser(adminService, mainService, form);
        }
    }

    @DeleteMapping(value = "/deleteUser")
    @Operation(summary = "Supprimer un compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppression d'un compte utilisateur"),
            @ApiResponse(responseCode = "403", description = "Opération interdite"),
            @ApiResponse(responseCode = "405", description = "Cet utilisateur n'existe pas")
    })
    public void deleteUser(@Parameter(description = "ID de l'utilisateur") @RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        if(id != null && adminService.getUserById(id) != null) {
            Users user = adminService.getUserById(id);
            adminService.deleteUser(user);
            response.sendRedirect("/app/admin/home");
        }
    }
}
