package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.Loyer;
import org.application.propertymanag.repository.LoyerRepository;
import org.application.propertymanag.service.LoyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoyerServiceImpl implements LoyerService {

    @Autowired
    LoyerRepository loyerRepository;

    @Override
    public Loyer getLoyerById(Integer idLoyer) {
        return loyerRepository.findById(idLoyer).orElseThrow();
    }

    @Override
    public Loyer getLoyerByDate(LocalDate date) {
        return loyerRepository.findByDate(date);
    }

    @Override
    public List<Loyer> getListOfLoyers() {
        return loyerRepository.findAll();
    }

    @Override
    public void createLoyer(Loyer l) {
        loyerRepository.save(l);
    }

    @Override
    public void deleteLoyer(Loyer l) {
        loyerRepository.delete(l);
    }
}
