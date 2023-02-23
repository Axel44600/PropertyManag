package org.application.propertymanag.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.application.propertymanag.auth.service.AuthenticationService;
import org.application.propertymanag.configuration.PathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@Controller
@RequiredArgsConstructor
public class AuthenticationController implements PathConfig {

    @Autowired
    private final AuthenticationService service;

    @GetMapping("/auth")
    public String getAuth(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "auth";
        } else {
            response.sendRedirect("app/home");
            return "/app/loc/home";
        }
    }

    @GetMapping("/first_auth")
    public String getFirstAuth(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "first_auth";
        } else {
            response.sendRedirect("app/home");
            return "/app/loc/home";
        }
    }

    @PostMapping(value = "/first_auth", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String submitFirstAuth(
            @RequestParam(name = "registerKey") String key,
            @RequestParam(name = "pseudo") String pseudo,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "repassword") String repassword) {

        boolean pseudoAlreadyUsed = service.getListOfUsers().stream().anyMatch(users -> users.getPseudo().equals(pseudo));
        boolean validKey = service.getListOfUsers().stream().anyMatch(
                users -> users.getRegisterKey() != null && users.getRegisterKey().equals(key));

        if (key.isEmpty() || pseudo.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            return "{\"error\": \"five\"}";
            // "Veuillez remplir tous les champs du formulaire."
        } else if (!password.equals(repassword)) {
            // "Les mots de passe ne correspondent pas."
            return "{\"error\": \"one\"}";
        } else if (!service.verifInputs(pseudo, password)) {
            // "Format du pseudonyme ou du mot de passe incorrect, veuillez relire la notice."
            return "{\"error\": \"two\"}";
        }
        else if(pseudoAlreadyUsed) {
            // "Ce pseudonyme est déjà utilisé."
            return "{\"error\": \"three\"}";
        } else {
            if (validKey) {
                service.firstLogin(key, pseudo, password);
                // "Votre compte est désormais activé, vous pouvez vous connecter !"
                return "{\"success\": \"yes\"}";
            } else {
                // "Votre clé d'enregistrement n'est pas valide."
                return "{\"error\": \"four\"}";
            }
        }
    }

}
