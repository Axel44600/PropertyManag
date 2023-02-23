package org.application.propertymanag.service.impl;

import org.application.propertymanag.auth.repository.UserRepository;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getListOfUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUserByNom(String nom) {
        return userRepository.findByNom(nom);
    }

    @Override
    public Users getUserByPseudo(String pseudo) {
        return userRepository.findByPseudo(pseudo).orElseThrow();
    }

    @Override
    public void createUser(Users e) {
        userRepository.save(e);
    }

    @Override
    public void deleteUser(Users u) {
        userRepository.delete(u);
    }

    public String getRandomStr(int n) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }
        return s.toString();
    }

}
