package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.entity.Loyer;
import org.application.propertymanag.service.impl.AppartServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
import org.application.propertymanag.service.impl.MainServiceImpl;
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
class LoyerControllerTest {

    @MockBean
    private AppartServiceImpl appartService;

    @MockBean
    private LocataireServiceImpl locataireService;

    @Autowired
    private MainServiceImpl mainService;

    @Autowired
    private MockMvc mockMvc;

    private Locataire locataire;

    private Appartement appart;

    private Loyer loyer;

    @BeforeEach
    public void init() {
        locataire = Locataire.builder()
                .idLoc(1)
                .nom("Delon")
                .prenom("Alain")
                .email("emailtst@google.com")
                .tel("0637001551")
                .solde(0)
                .build();

        appart = Appartement.builder()
                .idAppart(2)
                .idLoc(locataire)
                .adresse("adresseTest")
                .adresseComp("RDC")
                .ville("Paris")
                .codePostal(75000)
                .montantLoyer(400)
                .montantCharges(200)
                .montantDepotGarantie(1100)
                .montantFraisAgence(65)
                .dateCreation(LocalDate.now().minusDays(1))
                .build();

        loyer = Loyer.builder()
                .idLoyer(1)
                .idAppart(appart)
                .date(LocalDate.now())
                .statut(false)
                .ref(mainService.getRandomStr(25))
                .montant(appart.getMontantLoyer())
                .originePaiement("Locataire")
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        when(appartService.getAppartById(appart.getIdAppart())).thenReturn(appart);
        mockMvc.perform(get("/app/appart/loyer/{idAppart}", appart.getIdAppart())).andExpect(status().isOk());
    }

    @Test
    void testGetListOfLoyers() throws Exception {
        when(appartService.getAppartById(appart.getIdAppart())).thenReturn(appart);
        mockMvc.perform(get("/app/appart/loyer/data/listOfLoyers/{idAppart}", appart.getIdAppart())).andExpect(status().isOk());
    }

    @Test
    void testGetEditLoyer() throws Exception {
        given(appartService.getAppartById(appart.getIdAppart())).willReturn(appart);
        when(appartService.getLoyerById(loyer.getIdLoyer())).thenReturn(loyer);
        mockMvc.perform(get("/app/appart/loyer/editLoyer/{idLoyer}", loyer.getIdLoyer())).andExpect(status().isOk());
    }

    @Test
    void testFindLoyer() throws Exception {
        List<Loyer> listOfLoyers = new ArrayList<>();
        listOfLoyers.add(loyer);

        when(appartService.getListOfLoyers()).thenReturn(listOfLoyers);
        when(appartService.getLoyerByDate(loyer.getDate())).thenReturn(loyer);
        mockMvc.perform(post("/app/appart/loyer/researchLoyer")
                        .param("dateL", loyer.getDate().toString())
                        .param("idAppart", String.valueOf(loyer.getIdAppart().getIdAppart())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(7)))
                .andExpect(jsonPath("$.success").value("yes"))
                .andExpect(jsonPath("$.date").value(String.valueOf(loyer.getDate())))
                .andExpect(jsonPath("$.ref").value(loyer.getRef()))
                .andExpect(jsonPath("$.montant").value(loyer.getMontant()))
                .andExpect(jsonPath("$.statut").value("unpaid"))
                .andExpect(jsonPath("$.idLoyer").value(loyer.getIdLoyer()))
                .andExpect(jsonPath("$.urlEdit").value("./editLoyer/" + loyer.getIdLoyer()));
    }

    @Test
    void testCreateLoyer() throws Exception {
        Integer idAppart = appart.getIdAppart();
        Integer montant = appart.getMontantLoyer();
        Boolean statut = false;
        LocalDate date = LocalDate.now().plusDays(1);
        String origine = "Locataire";

        given(locataireService.getLocataireById(locataire.getIdLoc())).willReturn(locataire);
        given(appartService.getAppartById(appart.getIdAppart())).willReturn(appart);
        when(appartService.getLoyerByDate(date)).thenReturn(null);
        mockMvc.perform(post("/app/appart/loyer/createLoyer")
                        .param("idAppart", String.valueOf(idAppart))
                        .param("montant", String.valueOf(montant))
                        .param("statut", String.valueOf(statut))
                        .param("date", String.valueOf(date))
                        .param("origine", origine))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testEditLoyer() throws Exception {
        Boolean statut = true;

        given(locataireService.getLocataireById(locataire.getIdLoc())).willReturn(locataire);
        given(appartService.getAppartById(appart.getIdAppart())).willReturn(appart);
        when(appartService.getLoyerById(loyer.getIdLoyer())).thenReturn(loyer);
        mockMvc.perform(post("/app/appart/loyer/editLoyer")
                        .param("idLoyer", String.valueOf(loyer.getIdLoyer()))
                        .param("idAppart", String.valueOf(loyer.getIdAppart().getIdAppart()))
                        .param("montant", String.valueOf(loyer.getMontant()))
                        .param("statut", String.valueOf(statut))
                        .param("date", String.valueOf(loyer.getDate()))
                        .param("origine", String.valueOf(loyer.getOriginePaiement())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testDeleteLoyer() throws Exception {
        given(locataireService.getLocataireById(locataire.getIdLoc())).willReturn(locataire);
        when(appartService.getLoyerById(loyer.getIdLoyer())).thenReturn(loyer);
        mockMvc.perform(delete("/app/appart/loyer/deleteLoyer").param("idLoyer", String.valueOf(loyer.getIdLoyer())))
                .andExpect(redirectedUrl("/app/appart/loyer/"+appart.getIdAppart()));
    }

}
