package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.*;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/app/admin")
public class AdminController implements PathConfig {

    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private MainServiceImpl mainService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String getHome(Model model, Authentication auth) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("user", adminService.getUserByPseudo(auth.getName()));
        model.addAttribute("listOfUsers", adminService.getListOfUsers());
        return "/app/admin/home";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUser(@PathVariable(name = "id") Integer id, HttpServletResponse response, Model model) throws IOException {
        try {
            Users u = adminService.getUserById(id);
            model.addAttribute("user", u);
            List<Users> listOfUsers = adminService.getListOfUsers();

            model.addAttribute("listOfUsers", listOfUsers);
            model.addAttribute("appName", APP_NAME);
            return "/app/admin/edit_user";
        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "/app/admin/home";
        }
    }

    @PostMapping(value = "/researchUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    public String findUser(@RequestParam(name = "name") String nom, Authentication auth) {
        String nomM = mainService.maj(nom);
        boolean userFound = adminService.getListOfUsers().stream().anyMatch(
              users -> users.getNom().equals(nomM));

        if (userFound) {
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
    public String createUser (
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "firstName") String prenom,
            @RequestParam(name = "role") Role role) {

        if (!nom.isEmpty() && !prenom.isEmpty() && role != null) {
            String nomM = mainService.maj(nom);
            String prenomM = mainService.maj(prenom);

            if (!mainService.verifDigit(nomM) && !mainService.verifDigit(prenomM)
                    && !mainService.verifSpecialChar(nomM) && !mainService.verifSpecialChar(prenomM)) {
                if (adminService.getUserByNom(nomM) == null) {

                    String key = adminService.getRandomStr(25);
                    var user = Users.builder()
                            .nom(nom)
                            .prenom(prenom)
                            .role(role)
                            .registerKey(key)
                            .build();
                    adminService.createUser(user);
                    // "La session employé a été créer avec succès."
                    // Voici la clé d'enregistrement à transmettre à l'employé concerné : key
                    return "{\"success\": \"yes\"," +
                            "\"key\": \"" + key + "\"}";
                } else {
                    // "Une session employé est déjà enregistré sous ce nom."
                    return "{\"error\": \"one\"}";
                }
            } else {
                // "Le nom ou le prénom ne peut pas comporter de chiffres ni de caractères spéciaux."
                return "{\"error\": \"two\"}";
            }
        } else {
            // "Veuillez remplir tous les champs du formulaire."
            return "{\"error\": \"three\"}";
        }
    }

    @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    public String editUser(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "firstName") String prenom,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "repassword") String repass,
            @RequestParam(name = "role") Role role) {

        if(!nom.isEmpty() && !prenom.isEmpty() && !role.name().isEmpty()) {
            Users user = adminService.getUserById(id);
            String nomM = mainService.maj(nom);
            String prenomM = mainService.maj(prenom);

            if (!user.getNom().equals(nomM) || !user.getPrenom().equals(prenomM) ||
                    !user.getRole().equals(role) || (!password.isEmpty() && !repass.isEmpty())) {

                List<Users> listOfUsers = adminService.getListOfUsers().stream().filter(users -> !users.getId().equals(id)).toList();
                boolean nomAlreadyUsed = listOfUsers.stream().anyMatch(users -> users.getNom().equals(nomM));

                if (!nomAlreadyUsed) {
                    if (!mainService.verifSpecialChar(nomM) && !mainService.verifSpecialChar(prenomM)) {
                        if (!mainService.verifDigit(nomM) && !mainService.verifDigit(prenomM)) {

                            if (password.isEmpty() && repass.isEmpty()) { // PAS DE CHANGEMENT DE MOT DE PASSE

                                user.setNom(nomM);
                                user.setPrenom(prenomM);
                                user.setRole(role);
                                adminService.createUser(user);
                                // "Le compte utilisateur a été modifié avec succès."
                                return "{\"success\": \"yes\"}";

                            } else { // CHANGEMENT DE MOT DE PASSE

                                if (mainService.verifSize(password, 8) && mainService.verifMaj(password)
                                        && mainService.verifDigit(password) && mainService.verifSpecialChar(password)) {

                                    if(password.equals(repass)) {
                                        user.setNom(nomM);
                                        user.setPrenom(prenomM);
                                        user.setRole(role);
                                        user.setPassword(passwordEncoder.encode(password));
                                        adminService.createUser(user);
                                        // "Le compte utilisateur a été modifié avec succès."
                                        return "{\"success\": \"yes\"}";
                                    } else {
                                        // "Les mots de passe ne correspondent pas."
                                        return "{\"error\": \"one\"}";
                                    }
                                } else {
                                    // "Le format du mot de passe est incorrect, veuillez relire la notice."
                                    return "{\"error\": \"two\"}";
                                }
                            }

                        } else {
                            // "Le nom ainsi que le prénom ne peuvent pas comporter de chiffres."
                            return "{\"error\": \"three\"}";
                        }
                    } else {
                        // "Le nom ainsi que le prénom ne peuvent pas comporter de caractères spéciaux."
                        return "{\"error\": \"four\"}";
                    }
                } else {
                    // "Un utilisateur est déjà enregistré sous ce nom."
                    return "{\"error\": \"five\"}";
                }
            } else {
                return "{\"nochange\": \"yes\"}";
            }
        } else {
            // "Veuillez remplir tous les champs obligatoires du formulaire."
            return "{\"error\": \"six\"}";
        }

    }


    @DeleteMapping(value = "/deleteUser")
    public void deleteUser(@RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        if(id != null && adminService.getUserById(id) != null) {
            Users user = adminService.getUserById(id);
            adminService.deleteUser(user);
            response.sendRedirect("/app/admin/home");
        }
    }
}
