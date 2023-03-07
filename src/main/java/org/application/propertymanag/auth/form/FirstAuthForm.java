package org.application.propertymanag.auth.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FirstAuthForm {

    @NotEmpty(message = "Veuillez saisir un pseudonyme.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\d).+$", message = "Le pseudonyme est incorrect, veuillez relire la notice.")
    @Size(min = 6, message = "Le pseudonyme est trop court.")
    private String pseudo;
    @NotEmpty(message = "Veuillez saisir un mot de passe.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[ -\\/:-@\\[-\\`{-~])(?=.*\\d).+$", message = "Le mot de passe est incorrect, veuillez relire la notice.")
    @Size(min = 8, message = "Le mot de passe est trop court.")
    private String password;
    @NotEmpty(message = "Veuillez confirmer votre mot de passe.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[ -\\/:-@\\[-\\`{-~])(?=.*\\d).+$", message = "Le mot de passe est incorrect, veuillez relire la notice.")
    @Size(min = 8, message = "Le mot de passe est trop court.")
    private String repassword;
    @NotEmpty(message = "Veuillez renseigner la clé d'enregistrement.")
    private String registerKey;


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getRegisterKey() {
        return registerKey;
    }

    public void setRegisterKey(String registerKey) {
        this.registerKey = registerKey;
    }
}
