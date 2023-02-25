package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.EtatDesLieux;
import org.application.propertymanag.repository.EtatRepository;
import org.application.propertymanag.service.EtatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtatServiceImpl implements EtatService {

    @Autowired
    private EtatRepository etatRepository;

    @Override
    public EtatDesLieux getEtatById(Integer idEtat) {
        return etatRepository.findById(idEtat).orElseThrow();
    }

    @Override
    public List<EtatDesLieux> getListOfEtats() {
        return etatRepository.findAll();
    }

    @Override
    public void createEtat(EtatDesLieux e) {
        etatRepository.save(e);
    }

    @Override
    public EtatDesLieux getEtatByRef(String ref) {
        return etatRepository.findByRef(ref);
    }
}
