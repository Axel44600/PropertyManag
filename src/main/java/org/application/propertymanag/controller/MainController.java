package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
public class MainController implements PathConfig {

    @GetMapping
    public String getIndex(Authentication authentication, HttpServletResponse response, Model model) throws IOException {
        if (authentication == null) {
            model.addAttribute("appName", APP_NAME);
            return "index";
        } else {
            response.sendRedirect(APP_PATH + "/home");
            return "/app/loc/home";
        }
    }

    @GetMapping("/app/header")
    public String getHeader(Authentication authentication, Model model) {
            String role = authentication.getAuthorities().toString();
            model.addAttribute("role", role.substring(1, role.length()-1));
            model.addAttribute("appName", APP_NAME);
            return "/app/header";
    }

}
