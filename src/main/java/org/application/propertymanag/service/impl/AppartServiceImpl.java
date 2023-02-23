package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.repository.AppartRepository;
import org.application.propertymanag.service.AppartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppartServiceImpl implements AppartService {

    @Autowired
    private AppartRepository appartRepository;

    @Override
    public Appartement getAppartById(Integer idAppart) {
        return appartRepository.findById(idAppart).orElseThrow();
    }

    @Override
    public Appartement getAppartByAdresse(String adresse) {
        return appartRepository.findByAdresse(adresse);
    }

    @Override
    public Appartement getAppartByIdLocataire(Integer idLoc) {
        return appartRepository.findByIdLoc(idLoc);
    }

    @Override
    public List<Appartement> getListOfApparts() {
        return appartRepository.findAll();
    }

    @Override
    public void createAppart(Appartement a) {
        appartRepository.save(a);
    }
}