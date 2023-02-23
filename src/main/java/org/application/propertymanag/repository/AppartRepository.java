package org.application.propertymanag.repository;

import org.application.propertymanag.entity.Appartement;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppartRepository extends JpaRepository<Appartement, Integer> {

    @Query(value = "SELECT a FROM Appartement a ORDER BY a.id_appart")
    @NotNull
    List<Appartement> findAll();
    Appartement findByAdresse(String adresse);

    @Query(value = "SELECT ap FROM Appartement ap WHERE ap.id_loc.id_loc = ?1")
    Appartement findByIdLoc(Integer idLoc);
}
