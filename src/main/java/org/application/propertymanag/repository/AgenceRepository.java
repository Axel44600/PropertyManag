package org.application.propertymanag.repository;

import org.application.propertymanag.entity.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Integer> {

}
