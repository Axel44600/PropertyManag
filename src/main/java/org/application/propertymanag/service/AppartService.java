package org.application.propertymanag.service;

import org.application.propertymanag.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface AppartService {

    // APPARTEMENT
    Appartement getAppartById(Integer idAppart);

    Appartement getAppartByAdresse(String adresse);

    Appartement getAppartByIdLocataire(Integer idLoc);

    List<Appartement> getListOfApparts();

    void createAppart(Appartement a);


    // ETAT DES LIEUX
    EtatDesLieux getEtatById(Integer idEtat);

    List<EtatDesLieux> getListOfEtats();

    void createEtat(EtatDesLieux e);

    EtatDesLieux getEtatByRef(String ref);


    // LOYER
    Loyer getLoyerById(Integer idLoyer);

    Loyer getLoyerByDate(LocalDate date);

    List<Loyer> getListOfLoyers();

    void createLoyer(Loyer l);

    void deleteLoyer(Loyer l);


    // DEPOT DE GARANTIE
    DepotDeGarantie getDepotById(Integer idDepot);

    List<DepotDeGarantie> getListOfDepots();

    void createDepot(DepotDeGarantie d);


    // BILAN DES COMPTES DES LOYERS
    Bilan getBilanById(Integer idBilan);

    List<Bilan> getListOfBilans();

    void createBilan(Bilan b);


}
