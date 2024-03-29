package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Appartement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"EMPLOYE", "ADMIN"})
class GarantieControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    }

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/appart/depotGarantie/{idAppart}", appart.getIdAppart())).andExpect(status().isOk());
    }

}
