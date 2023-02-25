package org.application.propertymanag.service;

import org.application.propertymanag.entity.EtatDesLieux;

import java.util.List;

public interface EtatService {

    EtatDesLieux getEtatById(Integer idEtat);

    List<EtatDesLieux> getListOfEtats();

    void createEtat(EtatDesLieux e);
}
