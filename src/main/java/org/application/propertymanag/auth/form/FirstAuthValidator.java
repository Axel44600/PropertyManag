package org.application.propertymanag.auth.form;

import org.application.propertymanag.auth.service.AuthenticationService;

public class FirstAuthValidator {

    // Première connexion
    public String firstLogin(AuthenticationService service, FirstAuthForm form) {
        String pseudo = form.getPseudo();
        String password = form.getPassword();
        String repassword = form.getRepassword();
        String key = form.getRegisterKey();

        boolean pseudoAlreadyUsed = service.getListOfUsers().stream().anyMatch(users -> users.getUsername() != null && users.getUsername().equals(pseudo));
        boolean validKey = service.getListOfUsers().stream().anyMatch(users -> users.getRegisterKey() != null && users.getRegisterKey().equals(key));

        if(password.equals(repassword)) {
            if(!pseudoAlreadyUsed) {
                if(validKey) {
                    service.firstLogin(key, pseudo, password);
                    // "Votre compte est désormais activé, vous pouvez vous connecter !"
                    return "{\"success\": \"yes\"}";
                } else {
                    // "Votre clé d'enregistrement n'est pas valide."
                    return "{\"error\": \"one\"}";
                }
            } else {
                // "Ce pseudonyme est déjà utilisé."
                return "{\"error\": \"two\"}";
            }
        } else {
            // "Les mots de passe ne correspondent pas."
            return "{\"error\": \"three\"}";
        }
    }
}
