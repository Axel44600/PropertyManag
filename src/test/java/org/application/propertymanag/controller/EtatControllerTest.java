package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.EtatDesLieux;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"EMPLOYE", "ADMIN"})
class EtatControllerTest {

    @MockBean
    private AppartServiceImpl appartService;
    @Autowired
    private MockMvc mockMvc;

    private EtatDesLieux etatDesLieux;
    private Appartement appart;

    @BeforeEach
    public void init() {
        appart = Appartement.builder()
                .idAppart(2)
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

        etatDesLieux = EtatDesLieux.builder()
                .idEtat(4)
                .idAppart(appart)
                .type("Emménagement")
                .ref("REFERENCE")
                .date(LocalDate.now().atStartOfDay())
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        given(appartService.getAppartById(appart.getIdAppart())).willReturn(appart);
        mockMvc.perform(get("/app/appart/etat/{idAppart}", appart.getIdAppart())).andExpect(status().isOk());
    }

    @Test
    void testGetEditEtat() throws Exception {
        given(appartService.getEtatById(etatDesLieux.getIdEtat())).willReturn(etatDesLieux);
        mockMvc.perform(get("/app/appart/etat/editEtat/{idEtat}", etatDesLieux.getIdEtat())).andExpect(status().isOk());
    }

    @Test
    void testEditEtat() throws Exception {
        Integer idEtat = etatDesLieux.getIdEtat();
        LocalDateTime date = LocalDate.now().plusDays(1).atStartOfDay();
        String remarques = "L'appartement est en bon état.";

        when(appartService.getEtatById(idEtat)).thenReturn(etatDesLieux);
        mockMvc.perform(post("/app/appart/etat/editEtat", idEtat)
                        .param("idEtat", String.valueOf(idEtat))
                        .param("date", String.valueOf(date))
                        .param("remarques", remarques))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }
}
