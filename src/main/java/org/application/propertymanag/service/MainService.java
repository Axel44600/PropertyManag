package org.application.propertymanag.service;

public interface MainService {
    public boolean verifDigit(String var);

    public boolean verifSpecialChar(String var);

    public String maj(String var);

    public boolean verifMaj(String var);

    public boolean verifSize(String var, Integer minSize);

    public boolean verifFormatTel(String tel);

    public String getRandomStr(int n);

}
