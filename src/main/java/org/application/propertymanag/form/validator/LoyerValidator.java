package org.application.propertymanag.form.validator;

import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.entity.Loyer;
import org.application.propertymanag.file.QuittancePDF;
import org.application.propertymanag.form.appart.loyer.LoyerForm;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LoyerValidator {

    // Créer un loyer
    public String createLoyer(AppartServiceImpl appartService, MainServiceImpl mainService, LocataireServiceImpl locataireService, LoyerForm form) {

        Integer idAppart = form.getIdAppart();
        Integer montant = form.getMontant();
        Boolean statut = form.getStatut();
        LocalDate date = form.getDate();
        String origine = form.getOrigine();
        String result;

        Appartement a = appartService.getAppartById(idAppart);
            boolean alReadyExist = appartService.getListOfLoyers().stream().anyMatch(loyer -> loyer.getIdAppart().getIdAppart().equals(a.getIdAppart()) &&
                            date.getYear() == loyer.getDate().getYear() &&
                            date.getMonthValue() == loyer.getDate().getMonthValue());

            if(!date.isBefore(a.getDateCreation()) && !date.isEqual(a.getDateCreation())) {
                if(!alReadyExist) {
                    String ref = "LOC_N" + a.getIdLoc().getIdLoc() +
                            "_" + a.getIdLoc().getNom().toUpperCase() +
                            "_" + a.getIdLoc().getPrenom().toUpperCase() +
                            "_" + mainService.getRandomStr(10) +
                            "_" + date;

                    if(Boolean.FALSE.equals(statut)) {
                        int solde = montant - (montant*2);
                        Locataire locataire = locataireService.getLocataireById(a.getIdLoc().getIdLoc());
                        locataire.setSolde(solde);
                    }
                    var loyer = Loyer.builder()
                            .idAppart(a)
                            .montant(montant)
                            .statut(statut)
                            .date(date)
                            .originePaiement(origine)
                            .ref(ref)
                            .build();
                    appartService.createLoyer(loyer);
                    // "Le profil du locataire a été créer avec succès."
                    result = "{\"success\": \"yes\"}";
                } else {
                    System.out.println(a.getIdAppart());
                    // "Un loyer a déjà été enregistrer pour ce mois."
                    result = "{\"error\": \"one\"}";
                }
            } else {
                // "La date du loyer ne peut pas être plus ancienne que l'appartement."
                result = "{\"error\": \"two\"}";
            }
        return result;
    }

    // Modifier un loyer
    public String editLoyer(AppartServiceImpl appartService, LocataireServiceImpl locataireService, LoyerForm form) {

        Integer idLoyer = form.getIdLoyer();
        Loyer l = appartService.getLoyerById(idLoyer);
        Boolean statut = form.getStatut();
        LocalDate date = form.getDate();
        String origine = form.getOrigine();
        String result;

            if(!statut.equals(l.getStatut()) || !date.isEqual(l.getDate()) || !origine.equals(l.getOriginePaiement())) {
                Appartement a = appartService.getAppartById(l.getIdAppart().getIdAppart());
                boolean alreadyExist = false;
                if(l.getDate().getYear() == date.getYear()) {
                    List<Loyer> listOfLoyers = appartService.getListOfLoyers().stream().filter(loyer -> !Objects.equals(loyer.getIdLoyer(), idLoyer)).toList();
                    alreadyExist = listOfLoyers.stream().anyMatch(loyer -> loyer.getIdAppart().getIdAppart().equals(a.getIdAppart()) && loyer.getDate().getMonthValue() == date.getMonthValue());
                }

                if (!date.isBefore(a.getDateCreation()) && !date.isEqual(a.getDateCreation())) {
                    if(!alreadyExist) {
                        if(Boolean.TRUE.equals(statut)) {
                            Locataire locataire = locataireService.getLocataireById(a.getIdLoc().getIdLoc());
                            int solde = locataire.getSolde() + l.getMontant();
                            locataire.setSolde(solde);
                        }
                        l.setStatut(statut);
                        l.setDate(date);
                        l.setOriginePaiement(origine);
                        String reference = l.getRef().substring(0, l.getRef().length()-10);
                        reference+=l.getDate();
                        l.setRef(reference);
                        appartService.createLoyer(l);
                        // "Le loyer a été modifier avec succès."
                        result = "{\"success\": \"yes\"}";
                    } else {
                        // "Un loyer a déjà été enregistrer pour ce mois."
                        result = "{\"error\": \"one\"}";
                    }
                } else {
                    // "La date du loyer ne peut pas être plus ancienne que l'appartement."
                    result = "{\"error\": \"two\"}";
                }
            } else {
                result = "{\"nochange\": \"yes\"}";
            }
        return result;
    }

    // Créer une quittance de loyer
    public String createQuittance(AppartServiceImpl appartService, LocataireServiceImpl locataireService, MainServiceImpl mainService, Integer idAppart, LocalDate dateD, LocalDate dateF) {
        Appartement appart = appartService.getAppartById(idAppart);
        Locataire loc = locataireService.getLocataireById(appart.getIdLoc().getIdLoc());
        List<Loyer> listOfLoyers = appartService.getListOfLoyers().stream().filter(loyer -> loyer.getIdAppart().getIdAppart().equals(idAppart)).toList();
        List<Loyer> selectLoyers = new ArrayList<>();
        if(appartService.getAppartById(idAppart).getIdLoc().getSolde() >= 0) {
            for(Loyer l : listOfLoyers) {
                if((l.getDate().isEqual(dateD) || l.getDate().isAfter(dateD)) && (l.getDate().equals(dateF) || l.getDate().isBefore(dateF))) {
                    selectLoyers.add(l);
                }
            }
        }

        String absolutePath = new File("pom.xml").getAbsolutePath();
        String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
        String titleFile = loc.getNom().toUpperCase()+"_"+loc.getPrenom()+"_Quittance_de_loyer_APPART_N°"+idAppart+"_"+mainService.getRandomStr(8)+".pdf";
        String url = filePath+"\\pdf\\quittance\\"+titleFile;
        String urlWeb = PathConfig.PATH+"/pdf/quittance/"+titleFile;

        List<LocalDate> listOfDates = new ArrayList<>();
        for(Loyer l : selectLoyers) {
            listOfDates.add(l.getDate());
        }
        LocalDate dateDebut = Collections.min(listOfDates);
        LocalDate dateFin = Collections.max(listOfDates);

        QuittancePDF pdf = new QuittancePDF();
        pdf.createQuittance(appart, loc, dateDebut, dateFin, url);
        return "{\"success\": \"yes\"," +
                "\"urlWeb\": \"" + urlWeb + "\"}";
    }
}
