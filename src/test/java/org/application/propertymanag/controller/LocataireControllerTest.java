package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.impl.AdminServiceImpl;
import org.application.propertymanag.service.impl.LocataireServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"EMPLOYE", "ADMIN"}, username = "User")
class LocataireControllerTest {

    @MockBean
    private AdminServiceImpl adminService;

    @MockBean
    private LocataireServiceImpl locataireService;

    @Autowired
    private MockMvc mockMvc;

    private Locataire locataire;

    @BeforeEach
    public void init() {
        locataire = Locataire.builder()
                .idLoc(1)
                .nom("Delon")
                .prenom("Alain")
                .email("alaindelon@gmail.com")
                .tel("0637001551")
                .solde(0)
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        given(adminService.getUserByPseudo("User")).willReturn(new Users());
        mockMvc.perform(get("/app/home")).andExpect(status().isOk());
    }

    @Test
    void testGetListOfLocataires() throws Exception {
        mockMvc.perform(get("/app/data/listOfLocs")).andExpect(status().isOk());
    }

    @Test
    void testGetEditLoc() throws Exception {
        when(locataireService.getLocataireByNom(locataire.getNom())).thenReturn(locataire);
        mockMvc.perform(get("/app/editLocataire/{lastName}", locataire.getNom())).andExpect(status().isOk());
    }

    @Test
    void testFindLoc() throws Exception {
        List<Locataire> listOfLocs = new ArrayList<>();
        listOfLocs.add(locataire);

        when(locataireService.getListOfLocataires()).thenReturn(listOfLocs);
        when(locataireService.getLocataireByNom(locataire.getNom())).thenReturn(locataire);
        mockMvc.perform(post("/app/researchLoc").param("name", locataire.getNom()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(7)))
                .andExpect(jsonPath("$.success").value("yes"))
                .andExpect(jsonPath("$.nom").value(locataire.getNom()))
                .andExpect(jsonPath("$.prenom").value(locataire.getPrenom()))
                .andExpect(jsonPath("$.email").value(locataire.getEmail()))
                .andExpect(jsonPath("$.url").value("./editLocataire/"+locataire.getNom()))
                .andExpect(jsonPath("$.urlBilan").value("./bilan/"+locataire.getNom()));
    }

    @Test
    void testCreateLoc() throws Exception {
        String nom = "Delon";
        String prenom = "Alain";
        String tel = "0637001551";
        String email = "alaindelon@gmail.com";

        when(locataireService.getLocataireByNom(nom)).thenReturn(null);
        mockMvc.perform(post("/app/createLoc")
                        .param("lastName", nom)
                        .param("firstName", prenom)
                        .param("tel", tel)
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testEditLoc() throws Exception {
        String nom = locataire.getNom();
        String prenom = locataire.getPrenom();
        String tel = "0240356785";
        String email = locataire.getEmail();
        Integer solde = -230;

        when(locataireService.getLocataireByNom(nom)).thenReturn(locataire);
        mockMvc.perform(post("/app/editLocataire")
                        .param("lastName", nom)
                        .param("firstName", prenom)
                        .param("tel", tel)
                        .param("email", email)
                        .param("solde", String.valueOf(solde)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

}
