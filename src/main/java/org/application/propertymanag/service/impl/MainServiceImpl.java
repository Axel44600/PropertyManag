package org.application.propertymanag.service.impl;

import org.application.propertymanag.service.MainService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

@Service
public class MainServiceImpl implements MainService {

    public boolean verifDigit(String var) {
        boolean number = false;
        for(int a = 0; a < var.length(); a++) {
            char l = var.charAt(a);
            if(isDigit(l)) {
                number = true;
            }
        }
        if(number) { return true; } else { return false; }
    }

    public boolean verifSpecialChar(String var) {
        Pattern pattern = Pattern.compile("\\p{Punct}");
        Matcher matcher = pattern.matcher(var);
        return matcher.find();
    }

    public String maj(String var) {
        String varM = null;

        for (int i = 0; i < var.length(); i++) {
            if (i == 0) { varM = Character.toString(var.toUpperCase().charAt(0)); } else {
                varM += var.toLowerCase().charAt(i);
            }
        }
        return varM;
    }

    public boolean verifMaj(String var) {
        boolean maj = false;
        for(int a = 0; a < var.length(); a++) {
            char pa = var.charAt(a);
            if(Character.isUpperCase(pa)) {
                maj = true;
            }
        }
        if(maj) { return true; } else { return false; }
    }

    public boolean verifSize(String var, Integer minSize) {
        if(var.length() >= minSize) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verifFormatTel(String tel) {
        if(tel.chars().allMatch(Character::isDigit)) {
            return true;
        } else {
            return false;
        }
    }

    public String getRandomStr(int n) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }
        return s.toString();
    }

}
