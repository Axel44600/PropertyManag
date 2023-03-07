package org.application.propertymanag.form.locataire;

import jakarta.validation.constraints.*;

public class CreateLocForm {

    @NotEmpty(message = "Veuillez saisir un nom de famille.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Le format du nom de famille est invalide.")
    @Size(min = 3, max = 20, message = "Le nom doit faire entre 3 et 20 caractères.")
    private String lastName;
    @NotEmpty(message = "Veuillez saisir un prénom.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Le format du prénom est invalide.")
    @Size(min = 3, max = 20, message = "Le prénom doit faire entre 3 et 20 caractères.")
    private String firstName;
    @NotEmpty(message = "Veuillez saisir une adresse email.")
    @Email(message = "L'adresse email saisie est invalide.")
    private String email;

    @NotEmpty(message = "Veuillez saisir un numéro de téléphone.")
    @Size(min = 10, max = 10, message = "Le numéro de téléphone saisie est invalide.")
    @Pattern(regexp = "^[0-9]\\d*$", message = "Le numéro de téléphone saisie est invalide.")
    private String tel;


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
