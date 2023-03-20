package org.application.propertymanag.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainServiceImplTest {

    @InjectMocks
    private MainServiceImpl mainService;

    @Test
    void testMaj() {
        String var = "kowarDZ";
        StringBuilder varM = null;

        for (int i = 0; i < var.length(); i++) {
            if (i == 0) { varM = new StringBuilder(Character.toString(var.toUpperCase().charAt(0))); } else {
                varM.append(var.toLowerCase().charAt(i));
            }
        }
        assertEquals("Kowardz", varM.toString());
        assertEquals("Kowardz", mainService.maj(var));
    }

    @Test
    void testGetRandomStr() {
        int n = 25;
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(n);
        String s1 = mainService.getRandomStr(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }

        assertEquals(s.toString().length(), n);
        assertNotEquals(s.toString(), s1);
    }
}
