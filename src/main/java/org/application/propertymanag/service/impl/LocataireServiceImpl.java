package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.repository.LocataireRepository;
import org.application.propertymanag.service.LocataireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocataireServiceImpl implements LocataireService {

    @Autowired
    private LocataireRepository locataireRepository;

    @Override
    public Locataire getLocataireById(Integer idLoc) {
        return locataireRepository.findById(idLoc).orElseThrow();
    }

    @Override
    public Locataire getLocataireByNom(String nom) {
        return locataireRepository.findByNom(nom);
    }

    @Override
    public List<Locataire> getListOfLocataires() {
        return locataireRepository.findAll();
    }

    @Override
    public void createLocataire(Locataire l) {
        locataireRepository.save(l);
    }
}
