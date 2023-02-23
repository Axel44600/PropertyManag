package org.application.propertymanag.auth.service;

import lombok.RequiredArgsConstructor;
import org.application.propertymanag.auth.AuthenticationResponse;
import org.application.propertymanag.auth.repository.UserRepository;
import org.application.propertymanag.configuration.JwtService;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MainService mainService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private ArrayList<Boolean> error = new ArrayList<>();

    public List<Users> getListOfUsers() {
        return repository.findAll();
    }

    public ArrayList<Boolean> getError() {
        return error;
    }

    public boolean verifInputs(String pseudo, String password) {
        int minSizePseudo = 6;
        int minSizePassword = 8;

        if(mainService.verifSize(pseudo, minSizePseudo) &&
                mainService.verifSize(password, minSizePassword) &&
                mainService.verifMaj(pseudo) &&
                mainService.verifMaj(password) &&
                mainService.verifDigit(pseudo) &&
                mainService.verifDigit(password) &&
                mainService.verifSpecialChar(password)) {
            return true;
        } else {
            return false;
        }
    }

    public AuthenticationResponse firstLogin(String key, String pseudo, String password) {
        Optional<Users> u = repository.findByRegisterKey(key);
        String nom = u.get().getNom();

        Users user = repository.findByNom(nom);
        user.setPseudo(pseudo);
        user.setPassword(passwordEncoder.encode((password)));
        user.setRegisterKey(null);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
