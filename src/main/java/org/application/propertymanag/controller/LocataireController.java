package org.application.propertymanag.controller;

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
    public String getHome(Authentication authentication, Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("userFirstName", adminService.getUserByPseudo(authentication.getName()).getPrenom());
        model.addAttribute("listOfLocataires", locataireService.getListOfLocataires());
        return "/app/loc/home";
    }

    @GetMapping("/data/listOfLocs")
    public String getListOfLocataires(Model model) {
        model.addAttribute("listOfLocataires", locataireService.getListOfLocataires());
        return "/app/loc/data/list_locataires";
    }

    @GetMapping("/editLocataire/{lastName}")
    public String getEditLoc(@PathVariable(name = "lastName") String nom, Authentication authentication, Model model) {
         model.addAttribute("locataire", locataireService.getLocataireByNom(nom));
         model.addAttribute("appName", APP_NAME);
         model.addAttribute("pseudo", authentication.getName());
         return "/app/loc/edit_locataire";
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
                    "\"url\": \"./editLocataire/"+l.getNom()+"\"," +
                    "\"urlBilan\": \"./bilan/"+l.getNom()+"\"}";
        } else {
            return "{\"success\": \"no\"}";
        }
    }

    @PostMapping(value = "/createLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
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
    public String editLoc(@ModelAttribute @Valid UpdateLocForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editLoc(mainService, locataireService, form);
        }
}


}
