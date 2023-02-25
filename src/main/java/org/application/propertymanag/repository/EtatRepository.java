package org.application.propertymanag.repository;

import org.application.propertymanag.entity.EtatDesLieux;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EtatRepository extends JpaRepository<EtatDesLieux, Integer> {
    @Query(value = "SELECT e FROM EtatDesLieux e ORDER BY e.idEtat")
    @NotNull
    List<EtatDesLieux> findAll();

    @NotNull Optional<EtatDesLieux> findById(@NotNull Integer idEtat);

    @Query(value = "SELECT e FROM EtatDesLieux e WHERE e.ref = ?1")
    EtatDesLieux findByRef(String ref);

}
