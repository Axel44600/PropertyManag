package org.application.propertymanag.service.impl;

import org.application.propertymanag.service.MainService;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    @Override
    public String maj(String value) {
        String varM = null;

        for (int i = 0; i < value.length(); i++) {
            if (i == 0) { varM = Character.toString(value.toUpperCase().charAt(0)); } else {
                varM += value.toLowerCase().charAt(i);
            }
        }
        return varM;
    }

    @Override
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
