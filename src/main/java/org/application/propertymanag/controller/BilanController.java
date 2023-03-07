package org.application.propertymanag.controller;

import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Bilan;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app/bilan")
public class BilanController implements PathConfig {

    private AppartServiceImpl appartService;
    private LocataireServiceImpl locataireService;

    @Autowired
    public void setInjectedBean(AppartServiceImpl appartService, LocataireServiceImpl locataireService) {
        this.appartService = appartService;
        this.locataireService = locataireService;
    }

    @GetMapping("/{lastName}")
    public String getHome(@PathVariable(value = "lastName") String lastName, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<Bilan> listOfBilans = appartService.getListOfBilans().stream().filter(bilan ->
                bilan.getIdLoc().getNom().equals(lastName)).toList();
        Locataire locataire = locataireService.getLocataireByNom(lastName);

        model.addAttribute("listOfBilans", listOfBilans);
        model.addAttribute("locataire", locataire);
        return "/app/loc/bilan/home";
    }
}
