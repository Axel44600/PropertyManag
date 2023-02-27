package org.application.propertymanag.service;

import org.application.propertymanag.entity.DepotDeGarantie;

import java.util.List;

public interface GarantieService {

    DepotDeGarantie getDepotById(Integer idDepot);

    List<DepotDeGarantie> getListOfDepots();

    void createDepot(DepotDeGarantie d);

}
