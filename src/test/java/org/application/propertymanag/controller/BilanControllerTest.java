package org.application.propertymanag.controller;

import org.application.propertymanag.service.AppartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "Kowardz", roles = {"ADMIN", "EMPLOYE"})
public class BilanControllerTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    AppartService appartService;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getHomeTest() throws Exception {
        Integer idLoc = appartService.getListOfBilans().get(0).getIdLoc().getIdLoc();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/app/bilan/{idLoc}", idLoc))
                .andExpect(status().isOk());
    }

}
