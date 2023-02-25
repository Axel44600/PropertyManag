package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/app")
public class LocataireController implements PathConfig {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private LocataireServiceImpl locataireService;

    @Autowired
    private MainServiceImpl mainService;

    @GetMapping("/home")
    public String getHome(Authentication authentication, Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("userFirstName", adminService.getUserByPseudo(authentication.getName()).getPrenom());
        model.addAttribute("listOfLocataires", locataireService.getListOfLocataires());
        return "/app/loc/home";
    }

    @GetMapping("/editLocataire/{lastName}")
    public String getEditLoc(@PathVariable(name = "lastName") String nom, Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(locataireService.getLocataireByNom(nom) != null) {
            model.addAttribute("locataire", locataireService.getLocataireByNom(nom));
            model.addAttribute("appName", APP_NAME);
            model.addAttribute("pseudo", authentication.getName());
            return "/app/loc/edit_locataire";
        } else {
            response.sendRedirect("/app/home");
            return "/app/loc/home";
        }
    }

    @PostMapping(value = "/researchLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String findLoc(@RequestParam(name = "name") String lastName) {

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
                    "\"url\": \"./editLocataire/"+l.getNom()+"\"}";
        } else {
            return "{\"success\": \"no\"}";
        }
    }

    @PostMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String createLoc(
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "firstName") String prenom,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "tel") String tel) {

        if (!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !tel.isEmpty()) {
            String nomM = mainService.maj(nom);
            String prenomM = mainService.maj(prenom);

            boolean telAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getTel().equals(tel));
            if (locataireService.getLocataireByNom(nomM) == null && !telAlreadyUsed) {
                if (!mainService.verifDigit(nomM) && !mainService.verifDigit(prenomM)
                        && !mainService.verifSpecialChar(nomM) && !mainService.verifSpecialChar(prenomM)) {

                    boolean emailAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getEmail().equals(email));
                    if(!emailAlreadyUsed) {
                        if(mainService.verifFormatTel(tel) && !mainService.verifSpecialChar(tel)) {
                            var locataire = Locataire.builder()
                                    .nom(nomM)
                                    .prenom(prenomM)
                                    .email(email)
                                    .tel(tel)
                                    .solde(0)
                                    .build();
                            locataireService.createLocataire(locataire);
                            // "Le profil du locataire a été créer avec succès."
                            return "{\"success\": \"yes\"}";
                        } else {
                            // "Le numéro de téléphone ne peut comporter que des chiffres."
                            return "{\"error\": \"one\"}";
                        }
                    } else {
                        // "Un locataire possède déjà cette adresse email."
                        return "{\"error\": \"two\"}";
                    }
                } else {
                    // "Le nom ou le prénom ne peut pas comporter de chiffres ni de caractères spéciaux."
                    return "{\"error\": \"three\"}";
                }
            } else {
                // "Ce profil locataire existe déjà."
                return "{\"error\": \"four\"}";
            }
        } else {
            // "Veuillez remplir tous les champs du formulaire."
            return "{\"error\": \"five\"}";
        }
    }

    @PostMapping(value = "/editLocataire", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String editLoc(
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "firstName") String prenom,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "tel") String tel,
            @RequestParam(name = "solde") Integer solde) {

        if(!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !tel.isEmpty()) {
            String prenomM = mainService.maj(prenom);

            Locataire locat = locataireService.getLocataireByNom(nom);
            if(prenomM.equals(locat.getPrenom()) && email.equals(locat.getEmail()) && tel.equals(locat.getTel()) && solde.equals(locat.getSolde())) {
                return "{\"nochange\": \"yes\"}";
            } else {
                boolean telAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(
                        locataire -> !locataire.getTel().equals(locat.getTel()) && locataire.getTel().equals(tel));
                if(!telAlreadyUsed) {
                    if (!mainService.verifDigit(prenomM) && !mainService.verifSpecialChar(prenomM)) {

                        boolean emailAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(
                                locataire -> locataire.getEmail().equals(email) && !locataire.getNom().equals(nom));
                        if (!emailAlreadyUsed) {
                            if (mainService.verifFormatTel(tel) && !mainService.verifSpecialChar(tel)) {
                                Locataire l = locataireService.getLocataireByNom(nom);
                                l.setPrenom(prenomM);
                                l.setEmail(email);
                                l.setTel(tel);
                                l.setSolde(solde);
                                locataireService.createLocataire(l);
                                // "Le profil du locataire a été modifier avec succès."
                                return "{\"success\": \"yes\"}";
                            } else {
                                // "Le numéro de téléphone ne peut comporter que des chiffres."
                                return "{\"error\": \"one\"}";
                            }
                        } else {
                            // "Un locataire possède déjà cette adresse email."
                            return "{\"error\": \"two\"}";
                        }
                    } else {
                        // "Le prénom ne peut pas comporter de chiffres ni de caractères spéciaux."
                        return "{\"error\": \"three\"}";
                    }
                } else {
                    // "Un locataire possède déjà ce numéro de téléphone."
                    return "{\"error\": \"four\"}";
                }
            }
        } else {
            // "Veuillez saisir tous les champs du formulaire."
            return "{\"status\": \"five\"}";
        }
}
}
