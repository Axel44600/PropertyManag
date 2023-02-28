package org.application.propertymanag.repository;

import org.application.propertymanag.entity.Loyer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface LoyerRepository extends JpaRepository<Loyer, Integer> {

    @Query(value = "SELECT l FROM Loyer l ORDER BY l.date DESC")
    @NotNull
    List<Loyer> findAll();

    Loyer findByDate(LocalDate date);
}
