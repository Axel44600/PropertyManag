package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.EtatDesLieux;
import org.application.propertymanag.service.impl.EtatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/app/appart/etat")
public class EtatController implements PathConfig {

    @Autowired
    private EtatServiceImpl etatService;

    @GetMapping("/{idAppart}")
    public String getHome(@PathVariable(value = "idAppart") Integer idAppart, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<EtatDesLieux> listOfEtats = etatService.getListOfEtats().stream().filter(
                etatDesLieux -> etatDesLieux.getIdAppart().getIdAppart().equals(idAppart)).toList();

        model.addAttribute("listOfEtats", listOfEtats);
        return "/app/appart/etat/home";
    }

    @GetMapping("/editEtat/{idEtat}")
    public String getEditEtat(@PathVariable(name = "idEtat") Integer idEtat, HttpServletResponse response, Model model) throws IOException {
        try {
            EtatDesLieux etat = etatService.getEtatById(idEtat);
            model.addAttribute("etat", etat);
            model.addAttribute("appName", APP_NAME);
            return "/app/appart/etat/edit_etat";

        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "/app/appart/etat/home";
        }
    }

    @PostMapping(value = "/editEtat", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String editEtat(
            @RequestParam(name = "idEtat") Integer idEtat,
            @RequestParam(name = "date") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime date,
            @RequestParam(name = "remarques") String remarques) {

        boolean validEdit = false;
        if(idEtat != null && date != null) {
            EtatDesLieux etat = etatService.getEtatById(idEtat);
            if(etat.getDate() == null) {
                if(!remarques.equals(etat.getRemarques())) {
                    validEdit = true;
                }
            } else {
                if(!date.equals(etat.getDate()) || !remarques.equals(etat.getRemarques())) {
                    validEdit = true;
                }
            }

            if(validEdit && !etat.getRef().contains("close")) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    String dateF = dateFormat.format(date);
                    LocalDateTime dateTime = LocalDateTime.parse(dateF, dateFormat);

                    etat.setDate(dateTime);
                    etat.setRemarques(remarques);
                    etatService.createEtat(etat);

                    if(etat.getDate() != null && !etat.getRemarques().isEmpty()) {
                        etat.setRef(String.valueOf(etat.getRef() + "_close"));
                        etatService.createEtat(etat);
                    }
                    // "Les informations de l'état des lieux ont été modifier avec succès."
                    return "{\"success\": \"yes\"}";
            } else {
                return "{\"nochange\": true}";
            }
        } else {
                // "Veuillez remplir tous les champs du formulaire."
                return "{\"error\": \"one\"}";
        }
    }

}
