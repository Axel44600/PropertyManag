package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.*;
import org.application.propertymanag.form.appart.CreateAppartForm;
import org.application.propertymanag.form.appart.UpdateAppartForm;
import org.application.propertymanag.form.validator.AppartValidator;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/app")
public class AppartController implements PathConfig {

    private AppartServiceImpl appartService;
    private LocataireServiceImpl locataireService;
    private MainServiceImpl mainService;
    private final AppartValidator validator = new AppartValidator();

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService, LocataireServiceImpl locataireService, MainServiceImpl mainService) {
        this.appartService = appartService;
        this.locataireService = locataireService;
        this.mainService = mainService;
    }

    @GetMapping("/appart")
    public String getHome(Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("listOfApparts", appartService.getListOfApparts());
        return "/app/appart/home";
    }

    @GetMapping("/editAppart/{idAppart}")
    public String getEditApart(@PathVariable(name = "idAppart") Integer idAppart, HttpServletResponse response, Model model) throws IOException {
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
            return "/app/appart/edit_appart";
        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "/app/loc/home";
        }
    }

    @PostMapping(value = "/researchAppart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String findApart(@RequestParam(name = "address") String value) {
            boolean appartFound = appartService.getListOfApparts().stream().anyMatch(
                    appartement -> appartement.getAdresse().equals(value));

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
    public String editApart(@ModelAttribute @Valid UpdateAppartForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editAppart(appartService, locataireService, mainService, form);
        }
    }

}
