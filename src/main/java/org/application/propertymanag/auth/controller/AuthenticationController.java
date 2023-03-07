package org.application.propertymanag.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.application.propertymanag.auth.form.FirstAuthForm;
import org.application.propertymanag.auth.form.FirstAuthValidator;
import org.application.propertymanag.auth.service.AuthenticationService;
import org.application.propertymanag.configuration.PathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthenticationController implements PathConfig {

    private AuthenticationService service;
    private final FirstAuthValidator validator = new FirstAuthValidator();

    @Autowired
    public void setInjectedBean(AuthenticationService service) {
        this.service = service;
    }

    @GetMapping("/auth")
    public String getAuth(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "auth";
        } else {
            response.sendRedirect("app/home");
            return "/app/loc/home";
        }
    }

    @GetMapping("/first_auth")
    public String getFirstAuth(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if(authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "first_auth";
        } else {
            response.sendRedirect("app/home");
            return "/app/loc/home";
        }
    }

    @PostMapping(value = "/first_auth", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String submitFirstAuth(@ModelAttribute @Valid FirstAuthForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "{\"error\": \"four\"," +
                    "\"msgError\": \"" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "\"}";
        } else {
            return validator.firstLogin(service, form);
        }
    }

}
