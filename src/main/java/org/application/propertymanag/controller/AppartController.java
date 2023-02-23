package org.application.propertymanag.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/app")
public class AppartController implements PathConfig {

    @Autowired
    private AppartServiceImpl appartService;
    @Autowired
    private LocataireServiceImpl locataireService;
    @Autowired
    private MainServiceImpl mainService;

    @GetMapping("/appart")
    public String getHome(Model model) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("listOfApparts", appartService.getListOfApparts());
        return "/app/appart/home";
    }

    @GetMapping("/edit_appart/{idAppart}")
    public String getEditAppart(@PathVariable(name = "idAppart") Integer idAppart, HttpServletResponse response, Model model) throws IOException {
        try {
            Appartement appart = appartService.getAppartById(idAppart);
            model.addAttribute("appart", appart);
            List<Locataire> listOfLocataires;

            if(appart.getId_loc() != null)  {
                listOfLocataires = locataireService.getListOfLocataires().stream().filter(
                        locataire -> !Objects.equals(locataire.getId_loc(), appart.getId_loc().getId_loc()) &&
                                appartService.getAppartByIdLocataire(locataire.getId_loc()) == null).toList();
            } else {
                listOfLocataires = locataireService.getListOfLocataires().stream().filter(
                        locataire -> appartService.getAppartByIdLocataire(locataire.getId_loc()) == null).toList();
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
    public String researchAppart(@RequestParam(name = "addressOrNameL") String value) {

            String valueM = mainService.maj(value);
            boolean[] appartFound = {false, false};
            for (Appartement appart : appartService.getListOfApparts()) {
                if(appart.getId_loc() == null) {
                    if (value.equals(appart.getAdresse())) {
                        appartFound[0] = true;
                    }
                } else {
                    if (value.equals(appart.getAdresse())) {
                        appartFound[0] = true;
                    } else if (valueM.equals(appart.getId_loc().getNom())) {
                        appartFound[1] = true;
                    }
                }
            }

            if (!appartFound[0] && !appartFound[1]) {
                return "{" +
                        "\"success\": \"no\"}";
            } else {
                Appartement a;
                if (appartFound[0]) {
                    a = appartService.getAppartByAdresse(value);
                } else {
                    Locataire l = locataireService.getLocataireByNom(valueM);
                    a = appartService.getAppartByIdLocataire(l.getId_loc());
                }
                return "{" +
                        "\"success\": \"yes\"," +
                        "\"id\": \"" + a.getId_appart() + "\"," +
                        "\"adresse\": \"" + a.getAdresse() + "\"," +
                        "\"urlActions\": \"action" + a.getId_appart() + "\"," +
                        "\"urlEdit\": \"./edit_appart/" + a.getId_appart() + "\"," +
                        "\"urlSeeLoyer\": \"./loyer/" + a.getId_appart() + "\"," +
                        "\"urlSeeEtat\": \"./etat/" + a.getId_appart() + "\"," +
                        "\"urlSeeDepot\": \"./depot/" + a.getId_appart() + "\"}";
            }
    }

    @PostMapping(value = "/createAppart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String createAppart(
            @RequestParam(name = "adressForm") String adresse,
            @RequestParam(name = "adressCompForm") String adresseComp,
            @RequestParam(name = "villeForm") String ville,
            @RequestParam(name = "cPostalForm") Integer codePostal,
            @RequestParam(name = "loyerForm") Integer montantLoyer,
            @RequestParam(name = "chargesForm") Integer montantCharges,
            @RequestParam(name = "depotGForm") Integer montantDepotGarantie,
            @RequestParam(name = "dateForm") @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate dateCreation
            ) {

        if (!adresse.isEmpty() && !ville.isEmpty() && dateCreation != null && codePostal != null &&
                montantLoyer != null && montantCharges != null && montantDepotGarantie != null) {

            if (appartService.getAppartByAdresse(adresse) == null) {
                if (codePostal.toString().length() == 5) {

                    if(!mainService.verifSpecialChar(adresseComp) && !mainService.verifSpecialChar(codePostal.toString()) &&
                            !mainService.verifSpecialChar(montantLoyer.toString()) && !mainService.verifSpecialChar(montantCharges.toString()) &&
                            !mainService.verifSpecialChar(montantDepotGarantie.toString())) {

                        if(dateCreation.getYear() <= LocalDate.now().getYear()) {
                            Integer montantFraisAgence = (montantLoyer * 8) / 100;
                            var appartement = Appartement.builder()
                                    .id_loc(null)
                                    .adresse(adresse)
                                    .adresse_comp(adresseComp)
                                    .ville(ville)
                                    .code_postal(codePostal)
                                    .montant_loyer(montantLoyer)
                                    .montant_charges(montantCharges)
                                    .montant_depot_garantie(montantDepotGarantie)
                                    .date_creation(dateCreation)
                                    .montant_frais_agence(montantFraisAgence)
                                    .build();
                            appartService.createAppart(appartement);
                            // "Le profil du locataire a été créer avec succès."
                            return "{\"success\": \"yes\"}";
                        } else {
                            // "Impossible d'ajouter un appartement qui sera construit dans les années à venir."
                            return "{\"error\": \"date\"}";
                        }
                    } else {
                            // "Vérifier bien que les champs du formulaire ne comportent pas de caractères spéciaux."
                            return "{\"error\": \"one\"}";
                        }
                    } else {
                        // "Le code postal n'est pas valide."
                        return "{\"error\": \"two\"}";
                    }
                } else {
                    // "Il existe déjà un appartement enregistré à cette adresse."
                    return "{\"error\": \"three\"}";
                }
            } else {
                // "Veuillez remplir tous les champs obligatoires du formulaire."
                return "{\"error\": \"four\"}";
            }
    }

    @PostMapping(value = "/edit_appart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured({"ADMIN", "EMPLOYE"})
    public String editAppart(
            @RequestParam(name = "idLoc") @Nullable Integer idLoc,
            @RequestParam(name = "adresse") String adresse,
            @RequestParam(name = "loyer") Integer loyer,
            @RequestParam(name = "charges") Integer charges,
            @RequestParam(name = "depotGarantie") Integer depotGarantie) {

        if(loyer != null && charges != null && depotGarantie != null) {
            Appartement a = appartService.getAppartByAdresse(adresse);
            boolean changeLoc = false;

            if(a.getId_loc() == null) {
                if(idLoc != null) {
                    changeLoc = true;
                }
            } else {
                if(!Objects.equals(idLoc, a.getId_loc().getId_loc()) || idLoc == null) {
                    changeLoc = true;
                }
            }

            if(Boolean.TRUE.equals(changeLoc) || !Objects.equals(loyer, a.getMontant_loyer()) ||
                    !Objects.equals(charges, a.getMontant_charges()) || !Objects.equals(depotGarantie, a.getMontant_depot_garantie())) {
                if(loyer > 0 && charges > 0 && depotGarantie > 0) {

                    Integer montantFraisAgence = (loyer * 8) / 100;
                    if((idLoc != null && a.getId_loc() != null) || (idLoc != null && a.getId_loc() == null)) {
                        a.setId_loc(locataireService.getLocataireById(idLoc));
                        a.getId_loc().setId_loc(idLoc);
                    } else if(idLoc == null && a.getId_loc() != null) {
                        a.setId_loc(null);
                    }
                        a.setMontant_loyer(loyer);
                        a.setMontant_charges(charges);
                        a.setMontant_depot_garantie(depotGarantie);
                        a.setMontant_frais_agence(montantFraisAgence);
                    appartService.createAppart(a);
                    // "Les informations de l'appartement ont été modifier avec succès."
                    return "{\"success\": \"yes\"}";
                } else {
                    // "Les valeurs négatives ne sont pas autorisées."
                    return "{\"error\": \"one\"}";
                }
            } else {
                return "{\"nochange\": true}";
            }
        } else {
            // "Veuillez remplir tous les champs du formulaire."
            return "{\"error\": \"two\"}";
        }
    }
}
