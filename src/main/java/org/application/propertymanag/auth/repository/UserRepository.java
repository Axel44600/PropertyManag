package org.application.propertymanag.auth.repository;

import org.application.propertymanag.entity.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query(value = "SELECT u FROM Users u ORDER BY u.id")
    @NotNull
    List<Users> findAll();

    Optional<Users> findByPseudo(String pseudo);

    Users findByNom(String nom);

    Optional<Users> findByRegisterKey(String registerKey);

    void delete(@NotNull Users u);
}
