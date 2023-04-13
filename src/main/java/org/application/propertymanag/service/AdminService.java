package org.application.propertymanag.service;

import org.application.propertymanag.entity.Agence;
import org.application.propertymanag.entity.Users;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Users getUserById(Integer id);

    Users getUserByNom(String nom);

    Users getUserByPseudo(String pseudo);

    List<Users> getListOfUsers();

    void createUser(Users e);

    void deleteUser(Users u);

    Optional<Agence> getAgencyById(Integer idAgency);

    void updateAgency(Agence agence);

    String getRandomStr(int n);


}
