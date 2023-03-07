package org.application.propertymanag.form.admin;

import jakarta.validation.constraints.*;
import org.application.propertymanag.entity.Role;

public class CreateUserForm {

    @NotEmpty(message = "Veuillez saisir un nom de famille.")
    @Pattern(regexp="^[A-Za-z]*$", message = "Le format du nom de famille est invalide.")
    @Size(min = 3, max = 20, message = "Le nom de famille ne doit pas dépasser les 20 caractères.")
    private String lastName;
    @NotEmpty(message = "Veuillez saisir un prénom.")
    @Pattern(regexp="^[A-Za-z]*$", message = "Le format du prénom est invalide.")
    @Size(min = 3, max = 20, message = "Le prénom ne doit pas dépasser les 20 caractères.")
    private String firstName;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
