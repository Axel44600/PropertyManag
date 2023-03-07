package org.application.propertymanag.repository;

import org.application.propertymanag.entity.Bilan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BilanRepository extends JpaRepository<Bilan, Integer> {
    @Query(value = "SELECT b FROM Bilan b ORDER BY b.idBilan ASC")
    @NotNull
    List<Bilan> findAll();
    @NotNull Optional<Bilan> findById(@NotNull Integer idBilan);
}
