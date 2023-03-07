package org.application.propertymanag.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MainServiceImplTest {

    @Test
    public void majTest() {
        String var = "kowarDZ";
        String varM = null;

        for (int i = 0; i < var.length(); i++) {
            if (i == 0) { varM = Character.toString(var.toUpperCase().charAt(0)); } else {
                varM += var.toLowerCase().charAt(i);
            }
        }
        assertEquals(varM, "Kowardz");
    }

    @Test
    public void getRandomStrTest() {
        int n = 20;
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }

        assertEquals(s.toString().length(), n);
    }
}
