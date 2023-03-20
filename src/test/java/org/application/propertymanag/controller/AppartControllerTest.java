package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"EMPLOYE", "ADMIN"})
class AppartControllerTest {

    @MockBean
    private AppartServiceImpl appartService;

    @Autowired
    private MockMvc mockMvc;

    private Appartement appart;

    @BeforeEach
    public void init() {
        Locataire locataire = Locataire.builder()
                .idLoc(1)
                .nom("Delon")
                .prenom("Alain")
                .email("emailtst@google.com")
                .tel("0637001551")
                .solde(0)
                .build();

        appart = Appartement.builder()
                .adresse("adresseTest")
                .idLoc(locataire)
                .adresseComp("RDC")
                .ville("Paris")
                .codePostal(75000)
                .montantLoyer(400)
                .montantCharges(200)
                .montantDepotGarantie(1100)
                .montantFraisAgence(65)
                .dateCreation(LocalDate.now().minusDays(1))
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/appart")).andExpect(status().isOk());
    }

    @Test
    void testGetListOfApparts() throws Exception {
        mockMvc.perform(get("/app/data/listOfApparts")).andExpect(status().isOk());
    }

    @Test
    void testGetEditApart() throws Exception {
        appart.setIdAppart(2);
        given(appartService.getAppartById(appart.getIdAppart())).willReturn(appart);
        mockMvc.perform(get("/app/editAppart/{idAppart}", appart.getIdAppart())).andExpect(status().isOk());
    }

    @Test
    void testFindApart() throws Exception {
        List<Appartement> listOfApparts = new ArrayList<>();
        listOfApparts.add(appart);
        String adresse = appart.getAdresse();

        when(appartService.getListOfApparts()).thenReturn(listOfApparts);
        when(appartService.getAppartByAdresse(adresse)).thenReturn(appart);
        mockMvc.perform(post("/app/researchAppart").param("address", adresse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(8)))
                .andExpect(jsonPath("$.success").value("yes"))
                .andExpect(jsonPath("$.adresse").value(appart.getAdresse()))
                .andExpect(jsonPath("$.urlActions").value("action"+appart.getIdAppart()))
                .andExpect(jsonPath("$.urlEdit").value("./editAppart/"+appart.getIdAppart()))
                .andExpect(jsonPath("$.urlSeeLoyer").value("./appart/loyer/"+appart.getIdAppart()))
                .andExpect(jsonPath("$.urlSeeEtat").value("./appart/etat/"+appart.getIdAppart()))
                .andExpect(jsonPath("$.urlSeeDepot").value("./appart/depotGarantie/"+appart.getIdAppart()));
    }

    @Test
    void testCreateApart() throws Exception {
        String adressForm = appart.getAdresse();

        when(appartService.getAppartByAdresse(adressForm)).thenReturn(null);
        mockMvc.perform(post("/app/createAppart")
                        .param("adressForm", appart.getAdresse())
                        .param("addressCompForm", appart.getAdresseComp())
                        .param("villeForm", appart.getVille())
                        .param("cPostalForm", String.valueOf(appart.getCodePostal()))
                        .param("loyerForm", String.valueOf(appart.getMontantLoyer()))
                        .param("chargesForm", String.valueOf(appart.getMontantCharges()))
                        .param("depotGForm", String.valueOf(appart.getMontantCharges()))
                        .param("dateForm", String.valueOf(appart.getDateCreation())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testEditApart() throws Exception {
        String adressForm = appart.getAdresse();
        Integer idLoc = appart.getIdLoc().getIdLoc();
        Integer loyer = 230;
        Integer charges = 55;
        Integer depotGarantie = 700;

        when(appartService.getAppartByAdresse(adressForm)).thenReturn(appart);
        mockMvc.perform(post("/app/editAppart")
                        .param("idLoc", String.valueOf(idLoc))
                        .param("adressForm", appart.getAdresse())
                        .param("loyerForm", String.valueOf(loyer))
                        .param("chargesForm", String.valueOf(charges))
                        .param("depotGForm", String.valueOf(depotGarantie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testDeleteApart() throws Exception {
        appart.setIdAppart(2);

        when(appartService.getAppartById(appart.getIdAppart())).thenReturn(appart);
        mockMvc.perform(delete("/app/deleteAppart")
                        .param("idAppart", String.valueOf(appart.getIdAppart())))
                .andExpect(redirectedUrl("/app/appart"));
    }

}

