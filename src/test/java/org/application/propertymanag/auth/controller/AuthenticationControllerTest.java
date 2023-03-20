package org.application.propertymanag.auth.controller;

import org.application.propertymanag.auth.service.AuthenticationService;
import org.application.propertymanag.entity.Role;
import org.application.propertymanag.entity.Users;
import org.application.propertymanag.service.impl.MainServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainServiceImpl mainService;

    private Users user;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .id(1)
                .nom("Delon")
                .prenom("Alain")
                .role(Role.EMPLOYE)
                .registerKey(mainService.getRandomStr(15))
                .build();
    }


    @Test
    void testGetAuth() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk());
    }

    @Test
    void testGetFirstAuth() throws Exception {
        mockMvc.perform(get("/first_auth")).andExpect(status().isOk());
    }

    @Test
    void testFirstAuth() throws Exception {
        String pseudo = "User123456";
        String password = "PropertyManag123.";
        String key = user.getRegisterKey();

        List<Users> listOfUsers = new ArrayList<>();
        listOfUsers.add(user);

        when(authenticationService.getListOfUsers()).thenReturn(listOfUsers);
        mockMvc.perform(post("/first_auth")
                        .param("pseudo", pseudo)
                        .param("password", password)
                        .param("repassword", password)
                        .param("registerKey", key))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.success").value("yes"));
    }
}
