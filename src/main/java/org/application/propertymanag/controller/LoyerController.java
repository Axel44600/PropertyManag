package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Loyer;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/app/appart/loyer")
public class LoyerController implements PathConfig {

    @Autowired
    private LoyerServiceImpl loyerService;

    @Autowired
    private AppartServiceImpl appartService;

    @Autowired
    private MainServiceImpl mainService;

    @GetMapping("/{idAppart}")
    public String getHome(@PathVariable(value = "idAppart") Integer idAppart, Model model) {
        List<Loyer> listOfLoyers = loyerService.getListOfLoyers().stream().filter(
                loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart)).toList();
        Appartement a = appartService.getAppartById(idAppart);

        model.addAttribute("appName", APP_NAME);
        model.addAttribute("appart", a);
        model.addAttribute("listOfLoyers", listOfLoyers);
        return "/app/appart/loyer/home";
    }

    @GetMapping("/editLoyer/{idLoyer}")
    public String getEditLoyer(@PathVariable(name = "idLoyer") Integer idLoyer, HttpServletResponse response, Model model) throws IOException {
        if(loyerService.getLoyerById(idLoyer) != null) {
            Loyer l = loyerService.getLoyerById(idLoyer);
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
    public String findLoyer(@RequestParam(name = "dateL") LocalDate date, @RequestParam(name = "idAppart") Integer idAppart) {
        if(date != null){
            Loyer l = loyerService.getLoyerByDate(date);
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
    public String createLoyer(
            @RequestParam(name = "date") @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "montant") Integer montant,
            @RequestParam(name = "statut") Boolean statut,
            @RequestParam(name = "origine") String origine,
            @RequestParam(name = "idAppart") Integer idAppart) {

        if (date != null && montant != null && statut != null && origine != null && idAppart != null) {
            Appartement a = appartService.getAppartById(idAppart);
            boolean alReadyExist;

            alReadyExist = loyerService.getListOfLoyers().stream().anyMatch(
                        loyer -> loyer.getIdAppart().getIdAppart().equals(a.getIdAppart()) &&
                                date.getYear() == loyer.getDate().getYear() &&
                                date.getMonthValue() == loyer.getDate().getMonthValue());

            if(!date.isBefore(a.getDateCreation()) && !date.isEqual(a.getDateCreation())) {
                if(!alReadyExist) {
                    String ref = "LOC_N" + a.getIdLoc().getIdLoc() +
                            "_" + a.getIdLoc().getNom().toUpperCase() +
                            "_" + a.getIdLoc().getPrenom().toUpperCase() +
                            "_" + mainService.getRandomStr(10) +
                            "_" + date;

                    var loyer = Loyer.builder()
                            .idAppart(a)
                            .montant(montant)
                            .statut(statut)
                            .date(date)
                            .originePaiement(origine)
                            .ref(ref)
                            .build();
                    loyerService.createLoyer(loyer);
                    // "Le profil du locataire a été créer avec succès."
                    return "{\"success\": \"yes\"}";
                } else {
                    // "Un loyer a déjà été enregistrer pour ce mois."
                    return "{\"error\": \"one\"}";
                }
            } else {
                // "La date du loyer ne peut pas être plus ancienne que l'appartement."
                return "{\"error\": \"date\"}";
            }
        } else {
            // "Veuillez remplir tous les champs du formulaire."
            return "{\"error\": \"two\"}";
        }
    }


    @PostMapping(value = "/editLoyer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String editLoyer(
            @RequestParam(name = "idLoyer") Integer idLoyer,
            @RequestParam(name = "statut") Boolean statut,
            @RequestParam(name = "date") @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "origine") String origine) {

        if(idLoyer != null && statut != null && date != null && !origine.isEmpty()) {
            Loyer l = loyerService.getLoyerById(idLoyer);
            if(!statut.equals(l.getStatut()) || !date.isEqual(l.getDate()) || !origine.equals(l.getOriginePaiement())) {

                Appartement a = appartService.getAppartById(l.getIdAppart().getIdAppart());
                boolean alreadyExist = false;
                if(l.getDate().getYear() == date.getYear()) {

                    List<Loyer> listOfLoyers = loyerService.getListOfLoyers().stream().filter(
                            loyer -> !Objects.equals(loyer.getIdLoyer(), idLoyer)).toList();

                    alreadyExist = listOfLoyers.stream().anyMatch(
                            loyer -> loyer.getIdAppart().getIdAppart().equals(a.getIdAppart()) &&
                            loyer.getDate().getMonthValue() == date.getMonthValue());
                }

                if (!date.isBefore(a.getDateCreation()) && !date.isEqual(a.getDateCreation())) {
                    if(!alreadyExist) {
                        l.setStatut(statut);
                        l.setDate(date);
                        l.setOriginePaiement(origine);
                        String reference = l.getRef().substring(0, l.getRef().length()-10);
                        reference+=l.getDate();
                        l.setRef(reference);
                        loyerService.createLoyer(l);
                        // "Le loyer a été modifier avec succès."
                        return "{\"success\": \"yes\"}";
                    } else {
                        // "Un loyer a déjà été enregistrer pour ce mois."
                        return "{\"error\": \"one\"}";
                    }
                } else {
                    // "La date du loyer ne peut pas être plus ancienne que l'appartement."
                    return "{\"error\": \"date\"}";
                }
            } else {
                return "{\"nochange\": \"yes\"}";
            }
        } else {
            // "Veuillez remplir tous les champs du formulaire."
            return "{\"error\": \"two\"}";
        }
}


    @DeleteMapping(value = "/deleteLoyer")
    public void deleteLoyer(@RequestParam("idLoyer") Integer idLoyer, HttpServletResponse response) throws IOException {
        if(idLoyer != null && loyerService.getLoyerById(idLoyer) != null) {
                Loyer loyer = loyerService.getLoyerById(idLoyer);
                loyerService.deleteLoyer(loyer);
                response.sendRedirect("/app/appart/loyer/"+loyer.getIdAppart().getIdAppart());
        }
    }

}
