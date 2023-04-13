package org.application.propertymanag.controller;

import org.application.propertymanag.entity.Agence;
import org.application.propertymanag.entity.Role;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.impl.AdminServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = "ADMIN")
class AdminControllerTest {

    @MockBean
    private AdminServiceImpl adminService;

    @Autowired
    private MockMvc mockMvc;

    private Users user;

    private Agence agence;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .id(1)
                .pseudo("User")
                .password("PropertyManag123.")
                .nom("Delon")
                .prenom("Alain")
                .registerKey(null)
                .role(Role.EMPLOYE)
                .build();

        agence = Agence.builder()
                .idAgence(1)
                .nomAgence("Infeco")
                .fraisAgence(8)
                .build();
    }

    @Test
    void testGetHome() throws Exception {
        given(adminService.getAgencyById(1)).willReturn(Optional.ofNullable(agence));
        mockMvc.perform(MockMvcRequestBuilders.get("/app/admin/home")).andExpect(status().isOk());
    }

    @Test
    void testGetListOfUsers() throws Exception {
        mockMvc.perform(get("/app/admin/data/listOfUsers")).andExpect(status().isOk());
    }

    @Test
    void testGetEditUser() throws Exception {
        when(adminService.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(get("/app/admin/editUser/{id}", user.getId())).andExpect(status().isOk());
    }

    @Test
    void testFindUser() throws Exception {
        List<Users> listOfUsers = new ArrayList<>();
        listOfUsers.add(user);

        when(adminService.getListOfUsers()).thenReturn(listOfUsers);
        when(adminService.getUserByNom(user.getNom())).thenReturn(user);
        mockMvc.perform(post("/app/admin/researchUser").param("name", user.getNom()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(8)))
                .andExpect(jsonPath("$.success").value("yes"))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.nom").value(user.getNom()))
                .andExpect(jsonPath("$.prenom").value(user.getPrenom()))
                .andExpect(jsonPath("$.pseudo").value(user.getPseudo()))
                .andExpect(jsonPath("$.role").value(String.valueOf(user.getRole())))
                .andExpect(jsonPath("$.itsMe").value("no"))
                .andExpect(jsonPath("$.urlEdit").value("./editUser/"+user.getId()));
    }

    @Test
    void testCreateUser() throws Exception {
        String nom = "Delon";
        String prenom = "Alain";
        Role role = Role.EMPLOYE;
        String registerKey = adminService.getRandomStr(25);

        when(adminService.getUserByNom(nom)).thenReturn(null);
        mockMvc.perform(post("/app/admin/createUser")
                        .param("lastName", nom)
                        .param("firstName", prenom)
                        .param("role", String.valueOf(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.success").value("yes"))
                .andExpect(jsonPath("$.key").value(String.valueOf(registerKey)));
    }

    @Test
    void testEditUser() throws Exception {
        String nom = user.getNom();
        String prenom = user.getPrenom();
        Role role = Role.ADMIN;

        when(adminService.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(post("/app/admin/editUser")
                        .param("id", String.valueOf(user.getId()))
                        .param("lastName", nom)
                        .param("firstName", prenom)
                        .param("role", String.valueOf(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(adminService.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(delete("/app/admin/deleteUser").param("id", String.valueOf(user.getId())))
                .andExpect(redirectedUrl("/app/admin/home"));
    }

    @Test
    void testEditAgency() throws Exception {
        String nomAgence = "newAgency";
        Integer fraisAgence = agence.getFraisAgence();

        when(adminService.getAgencyById(agence.getIdAgence())).thenReturn(Optional.ofNullable(agence));
        mockMvc.perform(post("/app/admin/editAgency")
                        .param("nameAgency", nomAgence)
                        .param("expensesAgency", String.valueOf(fraisAgence)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }

}

