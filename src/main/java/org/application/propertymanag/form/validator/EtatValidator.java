package org.application.propertymanag.form.validator;

import org.application.propertymanag.entity.EtatDesLieux;
import org.application.propertymanag.form.appart.etat.UpdateEtatForm;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EtatValidator {

    public void editRef(AppartServiceImpl appartService, EtatDesLieux etat) {
        if(etat.getDate() != null && !etat.getRemarques().isEmpty()) {
            etat.setRef(etat.getRef() + "_close");
            appartService.createEtat(etat);
        }
    }
    public String editEtat(AppartServiceImpl appartService, UpdateEtatForm form) {

        Integer idEtat = form.getIdEtat();
        LocalDateTime date = form.getDate();
        String remarques = form.getRemarques();
        EtatDesLieux etat = appartService.getEtatById(idEtat);

        boolean validEdit = (!date.equals(etat.getDate()) && !remarques.equals(etat.getRemarques()) || !remarques.isEmpty());
        if(validEdit && !etat.getRef().contains("close")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String dateF = dateFormat.format(date);
                LocalDateTime dateTime = LocalDateTime.parse(dateF, dateFormat);

                etat.setDate(dateTime);
                etat.setRemarques(remarques);
                if(!remarques.isEmpty()) {
                    editRef(appartService, etat);
                }
                appartService.createEtat(etat);
                // "Les informations de l'état des lieux ont été modifier avec succès."
                return "{\"success\": \"yes\"}";
            } else {
                return "{\"nochange\": true}";
            }
    }

}
