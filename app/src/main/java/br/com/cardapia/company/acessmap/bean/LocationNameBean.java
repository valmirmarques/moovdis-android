package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class LocationNameBean extends LocationBean {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
