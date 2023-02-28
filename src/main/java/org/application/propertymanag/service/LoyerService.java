package org.application.propertymanag.service;

import org.application.propertymanag.entity.Loyer;

import java.time.LocalDate;
import java.util.List;

public interface LoyerService {

    Loyer getLoyerById(Integer idLoyer);

    Loyer getLoyerByDate(LocalDate date);

    List<Loyer> getListOfLoyers();

    void createLoyer(Loyer l);

    void deleteLoyer(Loyer l);

}
