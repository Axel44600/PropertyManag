package org.application.propertymanag.form.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.application.propertymanag.entity.Role;

public class UpdateUserForm {
    @NotNull
    private Integer id;
    @NotEmpty(message = "Veuillez saisir un nom de famille.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Le format du nom de famille est invalide.")
    @Size(min = 3, max = 20, message = "Le nom doit faire entre 3 et 20 caractères.")
    private String lastName;
    @NotEmpty(message = "Veuillez saisir un prénom.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Le format du prénom est invalide.")
    @Size(min = 3, max = 20, message = "Le prénom doit faire entre 3 et 20 caractères.")
    private String firstName;
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
