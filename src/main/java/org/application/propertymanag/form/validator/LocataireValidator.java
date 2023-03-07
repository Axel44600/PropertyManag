package org.application.propertymanag.form.validator;

import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.form.locataire.CreateLocForm;
import org.application.propertymanag.form.locataire.UpdateLocForm;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;

public class LocataireValidator {

    // Créer un profil locataire
    public String createLoc(MainServiceImpl mainService, LocataireServiceImpl locataireService, CreateLocForm form) {
        String nom = mainService.maj(form.getLastName());
        String prenom = mainService.maj(form.getFirstName());
        String tel = form.getTel();
        String email = form.getEmail();
        String result;

            boolean telAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getTel().equals(tel));
            if (locataireService.getLocataireByNom(nom) == null && !telAlreadyUsed) {

                boolean emailAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getEmail().equals(email));

                if(!emailAlreadyUsed) {
                    var locataire = Locataire.builder()
                            .nom(nom)
                            .prenom(prenom)
                            .email(email)
                            .tel(tel)
                            .solde(0)
                            .build();
                    locataireService.createLocataire(locataire);
                    // "Le profil du locataire a été créer avec succès."
                    result = "{\"success\": \"yes\"}";
                } else {
                    // "Un locataire possède déjà cette adresse email."
                    result = "{\"error\": \"one\"}";
                }
            } else {
                // "Ce profil locataire existe déjà."
                result = "{\"error\": \"two\"}";
            }
        return result;
    }

    // Modifier un profil locataire
    public String editLoc(MainServiceImpl mainService, LocataireServiceImpl locataireService, UpdateLocForm form) {
        String nom = mainService.maj(form.getLastName());
        String prenom = mainService.maj(form.getFirstName());
        String tel = form.getTel();
        String email = form.getEmail();
        Integer solde = form.getSolde();
        String result;

            Locataire locat = locataireService.getLocataireByNom(nom);
            if(prenom.equals(locat.getPrenom()) && email.equals(locat.getEmail()) && tel.equals(locat.getTel()) && solde.equals(locat.getSolde())) {
                result = "{\"nochange\": \"yes\"}";
            } else {
                boolean telAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(
                        locataire -> !locataire.getTel().equals(locat.getTel()) && locataire.getTel().equals(tel));

                if(!telAlreadyUsed) {
                        boolean emailAlreadyUsed = locataireService.getListOfLocataires().stream().anyMatch(locataire -> locataire.getEmail().equals(email) && !locataire.getNom().equals(nom));
                        if (!emailAlreadyUsed) {
                                Locataire l = locataireService.getLocataireByNom(nom);
                                l.setPrenom(prenom);
                                l.setEmail(email);
                                l.setTel(tel);
                                l.setSolde(solde);
                                locataireService.createLocataire(l);
                                // "Le profil du locataire a été modifier avec succès."
                                result = "{\"success\": \"yes\"}";
                        } else {
                            // "Un locataire possède déjà cette adresse email."
                            result = "{\"error\": \"one\"}";
                        }
                } else {
                    // "Un locataire possède déjà ce numéro de téléphone."
                    result = "{\"error\": \"two\"}";
                }
            }
        return result;
    }
}
