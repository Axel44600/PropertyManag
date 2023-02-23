package org.application.propertymanag.service;

import org.application.propertymanag.entity.Appartement;

import java.util.List;

public interface AppartService {

    Appartement getAppartById(Integer idAppart);

    Appartement getAppartByAdresse(String adresse);

    Appartement getAppartByIdLocataire(Integer idLoc);

    List<Appartement> getListOfApparts();

    void createAppart(Appartement a);

}
