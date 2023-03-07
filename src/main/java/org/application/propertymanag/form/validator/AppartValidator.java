package org.application.propertymanag.form.validator;

import org.application.propertymanag.entity.*;
import org.application.propertymanag.form.appart.CreateAppartForm;
import org.application.propertymanag.form.appart.UpdateAppartForm;
import org.application.propertymanag.service.impl.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.application.propertymanag.configuration.PathConfig.FRAIS_AGENCE;

public class AppartValidator {

    public String addRef(MainServiceImpl mainService, Appartement a, boolean type) {
        String ref;
        if(type) {
            ref = "LOC_N" + a.getIdLoc().getIdLoc() +
                    "_" + a.getIdLoc().getNom().toUpperCase() +
                    "_" + a.getIdLoc().getPrenom().toUpperCase() +
                    "_" + mainService.getRandomStr(10) + "_IN";
        } else {
            ref = "LOC_N" + a.getIdLoc().getIdLoc() +
                    "_" + a.getIdLoc().getNom().toUpperCase() +
                    "_" + a.getIdLoc().getPrenom().toUpperCase() +
                    "_" + mainService.getRandomStr(10) + "_OUT";
        }
        return ref;
    }
    public EtatDesLieux addEtat(AppartServiceImpl appartService, Appartement a, String refEtat, boolean type) {
        EtatDesLieux result;
        if(type) {
            var etatInBuild = EtatDesLieux.builder().idAppart(a).type("Emménagement").ref(refEtat).build();
            appartService.createEtat(etatInBuild);
            result = appartService.getEtatByRef(refEtat);
        } else {
            var etatOutBuild = EtatDesLieux.builder().idAppart(a).type("Déménagement").ref(refEtat).build();
            appartService.createEtat(etatOutBuild);
            result = appartService.getEtatByRef(refEtat);
        }
        return result;
    }
    public void addBilan(AppartServiceImpl appartService, Appartement a) {
        LocalDate dateDebut = null;
        LocalDate dateFin = null;

        Integer nbLoyers = 0;
        Long montantTotal = 0L;
        List<Loyer> listOfLoyersOfLoc = appartService.getListOfLoyers().stream().filter(
                l -> l.getRef().contains(a.getIdLoc().getNom().toUpperCase())).toList();

        for(int i = 0; i < listOfLoyersOfLoc.size(); i++) {
            nbLoyers++;
            montantTotal += listOfLoyersOfLoc.get(i).getMontant();
            if(i == 0) {
                dateFin = listOfLoyersOfLoc.get(i).getDate();
            } else if(i == listOfLoyersOfLoc.size()-1) {
                dateDebut = listOfLoyersOfLoc.get(i).getDate();
            }
        }
        var bilan = Bilan.builder().idAppart(a).idLoc(a.getIdLoc()).dateDebut(dateDebut).dateFin(dateFin).nbLoyers(nbLoyers)
                .montantTotal(montantTotal)
                .build();
        appartService.createBilan(bilan);
    }
    public void addDepot(AppartServiceImpl appartService, Appartement a, String value) {
        var depotGarantieBuild = DepotDeGarantie.builder().idAppart(a).montant(a.getMontantDepotGarantie()).statut(false)
                .ref(value.substring(0, value.length()-3))
                .build();
        appartService.createDepot(depotGarantieBuild);
    }


    // Créer un appartement
    public final String createAppart(AppartServiceImpl appartService, CreateAppartForm form) {
        String adresse = form.getAdressForm();
        String adresseComp = form.getAdressCompForm();
        String ville = form.getVilleForm();
        Integer codePostal = form.getcPostalForm();
        Integer montantLoyer = form.getLoyerForm();
        Integer montantCharges = form.getChargesForm();
        Integer montantDepotGarantie = form.getDepotGForm();
        LocalDate dateCreation = form.getDateForm();

        String result;

        if (appartService.getAppartByAdresse(adresse) == null) {
            Integer montantFraisAgence = (montantLoyer * FRAIS_AGENCE) / 100;

            var appartement = Appartement.builder()
                    .idAppart(null)
                    .adresse(adresse)
                    .adresseComp(adresseComp)
                    .ville(ville)
                    .codePostal(codePostal)
                    .montantLoyer(montantLoyer)
                    .montantCharges(montantCharges)
                    .montantDepotGarantie(montantDepotGarantie)
                    .dateCreation(dateCreation)
                    .montantFraisAgence(montantFraisAgence)
                    .build();
            appartService.createAppart(appartement);
            // "Le profil du locataire a été créer avec succès."
            result = "{\"success\": \"yes\"}";
        } else {
            // "Il existe déjà un appartement enregistré à cette adresse."
            result = "{\"error\": \"two\"}";
        }
        return result;
    }

    // Modifier un appartement
    public final String editAppart(AppartServiceImpl appartService, LocataireServiceImpl locataireService, MainServiceImpl mainService, UpdateAppartForm form) {
        String result;
        String adresse = form.getAdressForm();
        Integer idLoc = form.getIdLoc();
        Integer loyer = form.getLoyerForm();
        Integer charges = form.getChargesForm();
        Integer depotGarantie = form.getDepotGForm();
        Appartement a = appartService.getAppartByAdresse(adresse);

        boolean[] valueBool = new boolean[3]; // 0: Changement locataire /\ 1: Emménagement /\ 2: Déménagement
        String[] valueStr = new String[6]; // 0: refEtatIn /\ 1: refEtatOut /\ 2: lastNameOldLoc /\ 3: firstNameOldLoc /\ 4: lastNameNewLoc /\ 5: firstNameNewLoc
        EtatDesLieux[] valueEtat = new EtatDesLieux[2]; // 0: Déménagement /\ 1: Emménagement

        if (a.getIdLoc() == null && idLoc != null) { // Emménagement
            valueBool[0] = true;
            valueBool[1] = true;
        } else if (!Objects.equals(idLoc, a.getIdLoc().getIdLoc())) { // Déménagement + Emménagement
            valueBool[0] = true;
            valueBool[1] = true;
            valueBool[2] = true;
        } else if (idLoc == null) { // Déménagement
            valueBool[0] = true;
            valueBool[2] = true;
        }

        if (valueBool[0] || !loyer.equals(a.getMontantLoyer()) || !charges.equals(a.getMontantCharges()) || !depotGarantie.equals(a.getMontantDepotGarantie())) {
            if(a.getIdLoc() == null || a.getIdLoc().getSolde() >= 0) {

            Integer montantFraisAgence = (loyer * FRAIS_AGENCE) / 100;
            if ((idLoc != null)) {
                if (a.getIdLoc() != null && (valueBool[2])) { // DEMENAGEMENT
                        valueStr[1] = addRef(mainService, a, false);

                        // ADD ETAT DES LIEUX
                        valueEtat[0] = addEtat(appartService, a, valueStr[1], false);

                        // ADD BILAN DES COMPTES
                        addBilan(appartService, a);
                        for(Loyer l : appartService.getListOfLoyers()) {
                            if(l.getIdAppart().getIdAppart().equals(a.getIdAppart())) {
                                appartService.deleteLoyer(l);
                            }
                        }

                        valueStr[2] = locataireService.getLocataireById(valueEtat[0].getIdAppart().getIdLoc().getIdLoc()).getNom();
                        valueStr[3] = locataireService.getLocataireById(valueEtat[0].getIdAppart().getIdLoc().getIdLoc()).getPrenom();
                    }

                a.setIdLoc(locataireService.getLocataireById(idLoc));
                a.getIdLoc().setIdLoc(idLoc);

            } else if(a.getIdLoc() != null) {  // DEMENAGEMENT

                    valueStr[1] = addRef(mainService, a, false);

                    // ADD ETAT DES LIEUX
                    valueEtat[0] = addEtat(appartService, a, valueStr[1], false);

                    // ADD BILAN DES COMPTES
                    addBilan(appartService, a);
                    for(Loyer l : appartService.getListOfLoyers()) {
                        if(l.getIdAppart().getIdAppart().equals(a.getIdAppart())) {
                            appartService.deleteLoyer(l);
                        }
                    }
                    valueStr[2] = locataireService.getLocataireById(valueEtat[0].getIdAppart().getIdLoc().getIdLoc()).getNom();
                    valueStr[3] = locataireService.getLocataireById(valueEtat[0].getIdAppart().getIdLoc().getIdLoc()).getPrenom();

                    a.setIdLoc(null);
            }

                a.setMontantLoyer(loyer);
                a.setMontantCharges(charges);
                a.setMontantDepotGarantie(depotGarantie);
                a.setMontantFraisAgence(montantFraisAgence);
                appartService.createAppart(a);


            if (idLoc != null && valueBool[1]) { // EMMENAGEMENT
                valueStr[0] = addRef(mainService, a, true);

                // ADD ETAT DES LIEUX
                valueEtat[1] = addEtat(appartService, a, valueStr[0], true);

                valueStr[4] = locataireService.getLocataireById(valueEtat[1].getIdAppart().getIdLoc().getIdLoc()).getNom();
                valueStr[5] = locataireService.getLocataireById(valueEtat[1].getIdAppart().getIdLoc().getIdLoc()).getPrenom();

                // ADD DEPOT DE GARANTIE
                addDepot(appartService, a, valueStr[0]);
            }


                if (valueBool[0]) {
                    // "Les informations de l'appartement ont été modifier avec succès."

                    // Déménagement et emménagement
                    if (valueEtat[0] != null && valueEtat[1] != null) {
                        result = "{\"success\": \"yes\"," +
                                "\"idEtatOut\": \"" + valueEtat[0].getIdEtat() + "\"," +
                                "\"lastNameOldLoc\": \"" + valueStr[2] + "\"," +
                                "\"firstNameOldLoc\": \"" + valueStr[3] + "\"," +
                                "\"idEtatIn\": \"" + valueEtat[1].getIdEtat() + "\"," +
                                "\"idAppart\": \"" + valueEtat[1].getIdAppart().getIdAppart() + "\"," +
                                "\"lastNameNewLoc\": \"" + valueStr[4] + "\"," +
                                "\"firstNameNewLoc\": \"" + valueStr[5] + "\"}";
                    } else if (valueEtat[0] == null) {
                        // Emménagement
                        assert valueEtat[1] != null;
                        result = "{\"success\": \"yes\"," +
                                "\"idEtatIn\": \"" + valueEtat[1].getIdEtat() + "\"," +
                                "\"idAppart\": \"" + valueEtat[1].getIdAppart().getIdAppart() + "\"," +
                                "\"lastNameNewLoc\": \"" + valueStr[4] + "\"," +
                                "\"firstNameNewLoc\": \"" + valueStr[5] + "\"}";
                    } else {
                        // Déménagement
                        result = "{\"success\": \"yes\"," +
                                "\"idEtatOut\": \"" + valueEtat[0].getIdEtat() + "\"," +
                                "\"lastNameOldLoc\": \"" + valueStr[2] + "\"," +
                                "\"firstNameOldLoc\": \"" + valueStr[3] + "\"}";
                    }
                } else {
                    // "Les informations de l'appartement ont été modifier avec succès."
                    result = "{\"success\": \"yes\"}";
                }
            } else {
                // "Opération impossible, le locataire actuel n'est pas en règle sur le paiement de ses loyers."
                result = "{\"error\": \"one\"}";
            }
        } else {
            result = "{\"nochange\": true}";
        }
        return result;
    }
}
