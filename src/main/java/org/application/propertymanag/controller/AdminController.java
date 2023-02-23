package org.application.propertymanag.controller;

import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.Role;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/admin")
public class AdminController implements PathConfig {

    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private MainServiceImpl mainService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/index")
    public String getIndexA(Model model) {
        model.addAttribute("listOfUsers", adminService.getListOfUsers());
        return "/app/admin/index";
    }

    @GetMapping("/about-employe")
    public String getAboutEmployeA() {
        return "/app/admin/about-employe";
    }

    @GetMapping("/employe/{lastName}")
    public String employe(@PathVariable(name = "lastName") String nom, Model m) {
        m.addAttribute("e", adminService.getUserByNom(nom));
        return "/app/admin/employe";
    }

    @GetMapping("/deleteEmploye/{lastName}")
    public String getEmploye(@PathVariable(name = "lastName") String nom, Model m) {
        Users u = adminService.getUserByNom(nom);
        adminService.deleteUser(u);
        m.addAttribute("msgSuccess", "Le compte employé à été supprimé avec succès !");
        m.addAttribute("listOfEmployes", adminService.getListOfUsers());
        return "/app/admin/index";
    }

    @PostMapping("/index")
    public String searchEmploye(@RequestParam(name = "lastName") String nom, Model m) {
        String errorMsg = null;

        if(!nom.isEmpty()) {
            String realNom = mainService.maj(nom);
            if(adminService.getUserByNom(realNom) != null) {
                m.addAttribute("employe", adminService.getUserByNom(realNom));
                return "/app/admin/about-employe";
            } else {
                errorMsg = "Cet employé n'a pas été trouvé.";
            }
        } else {
            errorMsg = "Veuillez saisir un nom";
        }
        m.addAttribute("error", errorMsg);
        m.addAttribute("listOfEmployes", adminService.getListOfUsers());
        return "/app/admin/index";
    }

    @PostMapping("/createEmploye")
    public String createEmploye(
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "firstName") String prenom,
            @RequestParam(name = "poste") Role role,
            Model m) {
        String errorMsg = null;
        String key = adminService.getRandomStr(25);

        if(!nom.isEmpty() && !prenom.isEmpty()) {
            String nomM = mainService.maj(nom);
            String prenomM = mainService.maj(prenom);

            if(adminService.getUserByNom(nomM) == null) {
                if(!mainService.verifDigit(nomM) && !mainService.verifDigit(prenomM)
                        && !mainService.verifSpecialChar(nomM) && !mainService.verifSpecialChar(prenomM)) {
                    var user = Users.builder()
                            .nom(nomM)
                            .prenom(prenomM)
                            .role(role)
                            .registerKey(key)
                            .build();
                    adminService.createUser(user);
                    m.addAttribute("key",
                            "Le compte employé a été créer avec succès, voici la clé d'enregistrement : "+key);
                    return "/app/admin/index";
                } else {
                    errorMsg = "Le nom ou le prénom ne peut pas comporter de chiffres ni de caractères spéciaux.";
                }
            } else {
                errorMsg = "Cet employé existe déjà.";
            }
        } else {
            errorMsg = "Veuillez remplir tous les champs du formulaire.";
        }
        m.addAttribute("error", errorMsg);
        m.addAttribute("listOfEmployes", adminService.getListOfUsers());
        return "/app/admin/index";
    }

    @PostMapping("/employe/editEmploye")
    public String editEmploye(
            @RequestParam(name = "lastName") String nom,
            @RequestParam(name = "pseudo") String pseudo,
            @RequestParam(name = "poste") Role role,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "repassword") String repass,
            Model m) {
        String errorMsg = null;

        if(!nom.isEmpty() && !pseudo.isEmpty()) {
            String nomM = mainService.maj(nom);

                if(!mainService.verifDigit(nomM) && !mainService.verifSpecialChar(nomM)) {
                    if(password.isEmpty() && repass.isEmpty()) {

                        if(mainService.verifSize(pseudo, 6) && mainService.verifMaj(pseudo) && mainService.verifDigit(pseudo)) {
                            Users u = adminService.getUserByNom(nomM);
                            u.setPseudo(pseudo);
                            u.setRole(role);
                            adminService.createUser(u);
                            m.addAttribute("msgSuccess", "Le compte employé a été modifier avec succès !");
                            return "/app/admin/index";
                        } else {
                            errorMsg = "Format du pseudonyme ou du mot de passe incorrect, veuillez relire la notice.";
                        }
                    } else {
                        if(mainService.verifSize(pseudo, 6) && mainService.verifSize(password, 8) &&
                                mainService.verifMaj(pseudo) && mainService.verifMaj(password) &&
                                mainService.verifDigit(pseudo) && mainService.verifDigit(password) &&
                                mainService.verifSpecialChar(password)) {

                            if(password.equals(repass)) {
                                Users u = adminService.getUserByNom(nomM);
                                u.setPseudo(pseudo);
                                u.setPassword(passwordEncoder.encode((password)));
                                u.setRole(role);
                                adminService.createUser(u);
                                m.addAttribute("msgSuccess", "Le compte employé a été modifier avec succès !");
                                return "/app/admin/index";
                            } else {
                                errorMsg = " Les mots de passe ne correspondent pas.";
                            }
                        } else {
                            errorMsg = "Format du pseudonyme ou du mot de passe incorrect, veuillez relire la notice.";
                        }
                    }

                } else {
                    errorMsg = "Le nom ou le prénom ne peut pas comporter de chiffres ni de caractères spéciaux.";
                }
        } else {
            errorMsg = "Veuillez remplir tous les champs du formulaire.";
        }
        m.addAttribute("error", errorMsg);
        m.addAttribute("listOfEmployes", adminService.getListOfUsers());
        return "/app/admin/index";
    }

}
