package org.application.propertymanag.controller;

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
    public String getHome(@PathVariable(value = "idAppart") Integer idAppart, Model model) {
        List<Loyer> listOfLoyers = appartService.getListOfLoyers().stream().filter(
                loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart)).toList();
        List<Loyer> listOfLoyersPayed = appartService.getListOfLoyers().stream().filter(
                loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart) && loyer.getStatut().equals(true)).toList();
        Appartement a = appartService.getAppartById(idAppart);

        model.addAttribute("appName", APP_NAME);
        model.addAttribute("appart", a);
        model.addAttribute("listOfLoyers", listOfLoyers);
        model.addAttribute("listOfLoyersPayed", listOfLoyersPayed);
        return "/app/appart/loyer/home";
    }

    @GetMapping("/editLoyer/{idLoyer}")
    public String getEditLoyer(@PathVariable(name = "idLoyer") Integer idLoyer, HttpServletResponse response, Model model) throws IOException {
        if(appartService.getLoyerById(idLoyer) != null) {
            Loyer l = appartService.getLoyerById(idLoyer);
            model.addAttribute("loyer", l);
            model.addAttribute("appart", appartService.getAppartById(l.getIdAppart().getIdAppart()));
            model.addAttribute("appName", APP_NAME);
            return "/app/appart/loyer/edit_loyer";
        } else {
            response.sendRedirect("/app/appart/loyer/home");
            return "/app/appart/loyer/home";
        }
    }

    @PostMapping(value = "/researchLoyer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String createQuittance(@RequestParam(name = "dateL") LocalDate date, @RequestParam(name = "idAppart") Integer idAppart) {
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
    public String editLoyer(@ModelAttribute @Valid LoyerForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"three\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editLoyer(appartService, locataireService, form);
        }
}

    @DeleteMapping(value = "/deleteLoyer")
    public void deleteLoyer(@RequestParam("idLoyer") Integer idLoyer, HttpServletResponse response) throws IOException {
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
    public String createQuittance(@RequestParam(name = "idAppart") Integer idAppart,
                                  @RequestParam(name = "dateD") LocalDate dateD,
                                  @RequestParam(name = "dateF") LocalDate dateF) {

        if(idAppart != null && dateD != null && dateF != null) {
            return validator.createQuittance(appartService, locataireService, mainService, idAppart, dateD, dateF);
        } else {
            return "{\"error\": \"yes\"}";
        }

    }

}

