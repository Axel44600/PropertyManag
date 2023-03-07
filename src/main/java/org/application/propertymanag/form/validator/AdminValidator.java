package org.application.propertymanag.form.validator;

import org.application.propertymanag.entity.Role;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.form.admin.CreateUserForm;
import org.application.propertymanag.form.admin.UpdateUserForm;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;

import java.util.List;

public class AdminValidator {

    // Créer un compte utilisateur
    public String createUser(AdminServiceImpl adminService, MainServiceImpl mainService, CreateUserForm form) {

        String nom = mainService.maj(form.getLastName());
        String prenom = mainService.maj(form.getFirstName());
        Role role = form.getRole();

                if(adminService.getUserByNom(nom) == null) {
                    String key = adminService.getRandomStr(25);
                    var user = Users.builder()
                            .nom(nom)
                            .prenom(prenom)
                            .role(role)
                            .registerKey(key)
                            .build();
                    adminService.createUser(user);
                    // "La session employé a été créer avec succès."
                    // Voici la clé d'enregistrement à transmettre à l'employé concerné : key
                    return "{\"success\": \"yes\"," +
                            "\"key\": \"" + key + "\"}";
                } else {
                    // "Une session employé est déjà enregistré sous ce nom."
                    return "{\"error\": \"one\"}";
                }
    }

    // Modifier un compte utilisateur
    public String editUser(AdminServiceImpl adminService, MainServiceImpl mainService, UpdateUserForm form) {

        Integer id = form.getId();
        String nom = mainService.maj(form.getLastName());
        String prenom = mainService.maj(form.getFirstName());
        Role role = form.getRole();

        String result;
        Users user = adminService.getUserById(id);

            if(!user.getNom().equals(nom) || !user.getPrenom().equals(prenom) || !user.getRole().equals(role)) {

                List<Users> listOfUsers = adminService.getListOfUsers().stream().filter(users -> !users.getId().equals(id)).toList();
                boolean nomAlreadyUsed = listOfUsers.stream().anyMatch(users -> users.getNom().equals(nom));

                if(!nomAlreadyUsed) {
                    user.setNom(nom);
                    user.setPrenom(prenom);
                    user.setRole(role);
                    adminService.createUser(user);

                    // "Le compte utilisateur a été modifié avec succès."
                    result = "{\"success\": \"yes\"}";

                } else {
                    // "Un utilisateur est déjà enregistré sous ce nom."
                    result = "{\"error\": \"one\"}";
                }
            } else {
                result = "{\"nochange\": \"yes\"}";
            }
        return result;
    }

}
