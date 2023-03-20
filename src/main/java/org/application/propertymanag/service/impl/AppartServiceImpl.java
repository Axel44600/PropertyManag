package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.*;
import org.application.propertymanag.repository.*;
import org.application.propertymanag.service.AppartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AppartServiceImpl implements AppartService {

    private AppartRepository appartRepository;
    private EtatRepository etatRepository;
    private LoyerRepository loyerRepository;
    private GarantieRepository garantieRepository;
    private BilanRepository bilanRepository;

    @Autowired
    public void setInjectedBean(AppartRepository appartRepository, EtatRepository etatRepository, LoyerRepository loyerRepository,
                                GarantieRepository garantieRepository, BilanRepository bilanRepository) {
        this.appartRepository = appartRepository;
        this.etatRepository = etatRepository;
        this.loyerRepository = loyerRepository;
        this.garantieRepository = garantieRepository;
        this.bilanRepository = bilanRepository;
    }

    // APPARTEMENT
    @Override
    public Appartement getAppartById(Integer idApart) {
        return appartRepository.findById(idApart).orElseThrow();
    }

    @Override
    public Appartement getAppartByAdresse(String address) {
        return appartRepository.findByAdresse(address);
    }

    @Override
    public List<Appartement> getListOfApparts() {
        return appartRepository.findAll();
    }

    @Override
    public void createAppart(Appartement a) {
        appartRepository.save(a);
    }

    @Override
    public void deleteAppart(Appartement a) {
        List<EtatDesLieux> listOfEtats = etatRepository.findAll().stream().filter(etatDesLieux -> etatDesLieux.getIdAppart().getIdAppart().equals(a.getIdAppart())).toList();
            etatRepository.deleteAll(listOfEtats);
        List<Loyer> listOfLoyers = loyerRepository.findAll().stream().filter(loyer -> loyer.getIdAppart().getIdAppart().equals(a.getIdAppart())).toList();
            loyerRepository.deleteAll(listOfLoyers);
        List<DepotDeGarantie> listOfDepots = garantieRepository.findAll().stream().filter(garantie -> garantie.getIdAppart().getIdAppart().equals(a.getIdAppart())).toList();
            garantieRepository.deleteAll(listOfDepots);
        List<Bilan> listOfBilans = bilanRepository.findAll().stream().filter(bilan -> bilan.getIdAppart().getIdAppart().equals(a.getIdAppart())).toList();
            bilanRepository.deleteAll(listOfBilans);

        appartRepository.delete(a);
    }


    // ETAT DES LIEUX
    @Override
    public EtatDesLieux getEtatById(Integer idEtat) {
        return etatRepository.findById(idEtat).orElseThrow();
    }

    @Override
    public EtatDesLieux getEtatByRef(String ref) {
        return etatRepository.findByRef(ref);
    }

    @Override
    public List<EtatDesLieux> getListOfEtats() {
        return etatRepository.findAll();
    }

    @Override
    public void createEtat(EtatDesLieux e) {
        etatRepository.save(e);
    }


    // LOYER
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

    // DEPOT DE GARANTIE
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


    // BILAN DES COMPTES DES LOYERS

    @Override
    public List<Bilan> getListOfBilans() {
        return bilanRepository.findAll();
    }

    @Override
    public void createBilan(Bilan b) {
        bilanRepository.save(b);
    }

}
