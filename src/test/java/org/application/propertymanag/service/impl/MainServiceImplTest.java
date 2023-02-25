package org.application.propertymanag.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MainServiceImplTest {
    @Test
    public void verifDigitTest() {
        Boolean number = false;
        String var = "Kowardz88";

        for(int a = 0; a < var.length(); a++) {
            char l = var.charAt(a);
            if(isDigit(l)) {
                number = true;
            }
        }
       assertTrue(number);
    }

    @Test
    public void verifSpecialCharTest() {
        String var = "Kowardz";
        Pattern pattern = Pattern.compile("\\p{Punct}");
        Matcher matcher = pattern.matcher(var);

        assertFalse(matcher.find());
    }

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
    public void verifMajTest() {
        String var = "azErty";
        boolean maj = false;
        for(int a = 0; a < var.length(); a++) {
            char pa = var.charAt(a);
            if(Character.isUpperCase(pa)) {
                maj = true;
            }
        }

        assertTrue(maj);
    }

    @Test
    public void verifSizeTest() {
        String var = "Kowardz";
        int minSizePs = 6;

        assertTrue(var.length() >= minSizePs);
    }

    @Test
    public void verifFormatTel() {
        String var = "123";
        assertTrue(var.chars().allMatch(Character::isDigit));
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
