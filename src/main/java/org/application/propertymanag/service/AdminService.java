package org.application.propertymanag.service;

import org.application.propertymanag.entity.Users;

import java.util.List;

public interface AdminService {

    List<Users> getListOfUsers();

    Users getUserByNom(String nom);

    Users getUserByPseudo(String pseudo);

    void createUser(Users e);

    void deleteUser(Users u);

    String getRandomStr(int n);


}
