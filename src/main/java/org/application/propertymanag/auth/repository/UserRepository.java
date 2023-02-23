package org.application.propertymanag.auth.repository;

import org.application.propertymanag.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByPseudo(String pseudo);

    Users findByNom(String nom);

    Optional<Users> findByRegisterKey(String registerKey);

    void delete(Users u);
}
