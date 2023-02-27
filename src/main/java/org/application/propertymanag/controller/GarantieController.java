package org.application.propertymanag.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.application.propertymanag.configuration.PathConfig;
import org.application.propertymanag.entity.DepotDeGarantie;
import org.application.propertymanag.service.GarantieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/app/appart/depotGarantie")
public class GarantieController implements PathConfig {

    @Autowired
    private GarantieService garantieService;

    @GetMapping("/{idAppart}")
    public String getHome(@PathVariable(value = "idAppart") Integer idAppart, Model model) {
        model.addAttribute("appName", APP_NAME);
        List<DepotDeGarantie> listOfDepots = garantieService.getListOfDepots().stream().filter(
                garantie -> garantie.getIdAppart().getIdAppart().equals(idAppart)).toList();

        model.addAttribute("listOfDepots", listOfDepots);
        return "/app/appart/garantie/home";
    }

    @PostMapping(value = "/validDepot")
    @Secured({"ADMIN", "EMPLOYE"})
    public void editDepot(@RequestParam(name = "idDepot") Integer idDepot, HttpServletResponse response) throws IOException {
        if(idDepot != null) {
            DepotDeGarantie d = garantieService.getDepotById(idDepot);
            d.setStatut(true);
            garantieService.createDepot(d);
            response.sendRedirect("/app/appart/depotGarantie/"+d.getIdAppart().getIdAppart());
        }
    }

}
