package org.application.propertymanag.repository;

import org.application.propertymanag.entity.Locataire;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocataireRepository extends JpaRepository<Locataire, Integer> {

    @Query(value = "SELECT l FROM Locataire l ORDER BY l.id_loc")
    @NotNull
    List<Locataire> findAll();
    Locataire findByNom(String nom);
}
