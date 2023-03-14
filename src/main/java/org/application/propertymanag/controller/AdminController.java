package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.*;
import org.application.propertymanag.form.admin.CreateUserForm;
import org.application.propertymanag.form.admin.UpdateUserForm;
import org.application.propertymanag.form.validator.AdminValidator;
import org.application.propertymanag.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@RequestMapping("/app/admin")
public class AdminController implements PathConfig {

    private AdminServiceImpl adminService;
    private MainServiceImpl mainService;
    private final AdminValidator validator = new AdminValidator();

    @Autowired
    public void setInjectedBean(AdminServiceImpl adminService, MainServiceImpl mainService) {
        this.adminService = adminService;
        this.mainService = mainService;
    }

    @GetMapping("/home")
    public String getHome(Model model, Authentication auth) {
        model.addAttribute("appName", APP_NAME);
        model.addAttribute("user", adminService.getUserByPseudo(auth.getName()));
        model.addAttribute("listOfUsers", adminService.getListOfUsers());
        return "/app/admin/home";
    }

    @GetMapping("/data/listOfUsers")
    public String getListOfUsers(Model model, Authentication auth) {
        model.addAttribute("listOfUsers", adminService.getListOfUsers());
        model.addAttribute("user", adminService.getUserByPseudo(auth.getName()));
        return "/app/admin/data/list_users";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUser(@PathVariable(name = "id") Integer id, HttpServletResponse response, Model model) throws IOException {
        try {
            Users u = adminService.getUserById(id);
            model.addAttribute("user", u);
            List<Users> listOfUsers = adminService.getListOfUsers();

            model.addAttribute("listOfUsers", listOfUsers);
            model.addAttribute("appName", APP_NAME);
            return "/app/admin/edit_user";
        } catch (NoSuchElementException nSE) {
            response.sendRedirect("/app/home");
            return "/app/admin/home";
        }
    }

    @PostMapping(value = "/researchUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    public String findUser(@RequestParam(name = "name") String nom, Authentication auth) {
        String nomM = mainService.maj(nom);
        boolean userFound = adminService.getListOfUsers().stream().anyMatch(users -> users.getNom().equals(nomM));

        if(userFound) {
            Users u = adminService.getUserByNom(nomM);
            String itsMe = "no";
            if(u.getPseudo().equals(auth.getName())) {
                itsMe = "yes";
            }
            return "{" +
                    "\"success\": \"yes\"," +
                    "\"id\": \"" + u.getId() + "\"," +
                    "\"nom\": \"" + u.getNom() + "\"," +
                    "\"prenom\": \"" + u.getPrenom() + "\"," +
                    "\"pseudo\": \"" + u.getPseudo() + "\"," +
                    "\"role\": \"" + u.getRole().name() + "\"," +
                    "\"itsMe\": \"" + itsMe + "\"," +
                    "\"urlEdit\": \"./editUser/" + u.getId() + "\"}";
        } else {
            return "{" + "\"success\": \"no\"}";
        }
    }

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    public String createUser (@ModelAttribute @Valid CreateUserForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.createUser(adminService, mainService, form);
        }
    }

    @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ADMIN")
    public String editUser(@ModelAttribute @Valid UpdateUserForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"two\"," +
                        "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.editUser(adminService, mainService, form);
        }
    }

    @DeleteMapping(value = "/deleteUser")
    public void deleteUser(@RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        if(id != null && adminService.getUserById(id) != null) {
            Users user = adminService.getUserById(id);
            adminService.deleteUser(user);
            response.sendRedirect("/app/admin/home");
        }
    }
}
