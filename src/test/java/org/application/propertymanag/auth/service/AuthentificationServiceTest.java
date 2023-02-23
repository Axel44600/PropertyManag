package org.application.propertymanag.auth.service;


import org.application.propertymanag.service.impl.MainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthentificationServiceTest {

    @InjectMocks
    private MainServiceImpl mainService;

    @Test
    public void verifInputsTest() {
        String pseudo = "Kowardz44";
        String password = "Propertymanag123?";
        int minSizePseudo = 6;
        int minSizePassword = 8;
        boolean result = false;

        if(mainService.verifSize(pseudo, minSizePseudo) &&
                mainService.verifSize(password, minSizePassword) &&
                mainService.verifMaj(pseudo) &&
                mainService.verifMaj(password) &&
                mainService.verifDigit(pseudo) &&
                mainService.verifDigit(password) &&
                mainService.verifSpecialChar(password)) {
            result = true;
        }
        assertTrue(result);
    }

}
