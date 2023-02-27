package org.application.propertymanag.repository;

import org.application.propertymanag.entity.DepotDeGarantie;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarantieRepository extends JpaRepository<DepotDeGarantie, Integer> {
    @Query(value = "SELECT d FROM DepotDeGarantie d ORDER BY d.idDepot DESC")
    @NotNull
    List<DepotDeGarantie> findAll();

    @NotNull Optional<DepotDeGarantie> findById(@NotNull Integer idDepot);

    @Query(value = "SELECT d FROM DepotDeGarantie d WHERE d.ref = ?1")
    DepotDeGarantie findByRef(String ref);

}
