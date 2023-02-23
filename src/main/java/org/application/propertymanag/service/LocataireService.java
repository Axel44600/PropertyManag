package org.application.propertymanag.service;

import org.application.propertymanag.entity.Locataire;

import java.util.List;

public interface LocataireService {

    Locataire getLocataireById(Integer idLoc);

    Locataire getLocataireByNom(String nom);

    List<Locataire> getListOfLocataires();

    void createLocataire(Locataire l);

}
