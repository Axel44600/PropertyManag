package org.application.propertymanag.service;

import org.application.propertymanag.entity.Users;

import java.util.List;

public interface AdminService {

    Users getUserById(Integer id);

    Users getUserByNom(String nom);

    Users getUserByPseudo(String pseudo);

    List<Users> getListOfUsers();

    void createUser(Users e);

    void deleteUser(Users u);

    String getRandomStr(int n);


}
