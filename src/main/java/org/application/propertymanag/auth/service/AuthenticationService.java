package org.application.propertymanag.auth.service;

import lombok.RequiredArgsConstructor;
import org.application.propertymanag.auth.AuthenticationResponse;
import org.application.propertymanag.auth.repository.UserRepository;
import org.application.propertymanag.configuration.JwtService;
import org.application.propertymanag.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public void setInjectedBean(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> getListOfUsers() {
        return repository.findAll();
    }

    public void firstLogin(String key, String pseudo, String password) {
        Users u = repository.findByRegisterKey(key).orElseThrow();
        String nom = u.getNom();

        Users user = repository.findByNom(nom);
        user.setPseudo(pseudo);
        user.setPassword(passwordEncoder.encode((password)));
        user.setRegisterKey(null);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse.builder().token(jwtToken).build();
    }
}
