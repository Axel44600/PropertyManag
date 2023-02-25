package org.application.propertymanag.repository;

import org.application.propertymanag.entity.EtatDesLieux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EtatRepository extends JpaRepository<EtatDesLieux, Integer> {
    @Query(value = "SELECT a FROM EtatDesLieux a ORDER BY a.idEtat")
    List<EtatDesLieux> findAll();
    Optional<EtatDesLieux> findById(Integer idEtat);
}
