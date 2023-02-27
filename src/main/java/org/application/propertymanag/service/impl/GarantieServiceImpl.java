package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.DepotDeGarantie;
import org.application.propertymanag.repository.GarantieRepository;
import org.application.propertymanag.service.GarantieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarantieServiceImpl implements GarantieService {

    @Autowired
    GarantieRepository garantieRepository;


    @Override
    public DepotDeGarantie getDepotById(Integer idDepot) {
        return garantieRepository.findById(idDepot).orElseThrow();
    }

    @Override
    public List<DepotDeGarantie> getListOfDepots() {
        return garantieRepository.findAll();
    }

    @Override
    public void createDepot(DepotDeGarantie d) {
        garantieRepository.save(d);
    }
}
