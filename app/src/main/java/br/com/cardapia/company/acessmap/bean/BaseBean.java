package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;
import java.util.Locale;

public class BaseBean implements Serializable {

    private String lang;

    public String getLang() {
        if (lang == null) {
            lang = Locale.getDefault().getLanguage();
        }
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
