package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"EMPLOYE", "ADMIN"})
class BilanControllerTest {
    @MockBean
    LocataireServiceImpl locataireService;

    @Autowired
    private MockMvc mockMvc;

    private Locataire locataire;

    @BeforeEach
    public void init() {
        locataire = Locataire.builder()
                .idLoc(3)
                .nom("Delon")
                .prenom("Alain")
                .email("emailtst@gmail.com")
                .tel("0637001551")
                .solde(0)
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        given(locataireService.getLocataireByNom(locataire.getNom())).willReturn(locataire);
        mockMvc.perform(get("/app/bilan/{lastName}", locataire.getNom())).andExpect(status().isOk());
    }

}
