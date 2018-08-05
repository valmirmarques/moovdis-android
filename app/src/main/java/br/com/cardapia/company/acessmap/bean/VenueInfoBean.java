package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueInfoBean implements Serializable {


    private String _id;
    private String venueName;
    private boolean hasEntradaBoa;
    private boolean hasCirculacaoInterna;
    private boolean hasWc;
    private String comment;
    private boolean hasFraudario;
    private boolean hasEstacionamento;
    private VenueInfoLugarAcessivelEnum lugarAcessivel;
    private boolean hasMapaTatil;
    private boolean hasMesa;
    private UsuarioBean user;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isHasEntradaBoa() {
        return hasEntradaBoa;
    }

    public void setHasEntradaBoa(boolean hasEntradaBoa) {
        this.hasEntradaBoa = hasEntradaBoa;
    }

    public boolean isHasCirculacaoInterna() {
        return hasCirculacaoInterna;
    }

    public void setHasCirculacaoInterna(boolean hasCirculacaoInterna) {
        this.hasCirculacaoInterna = hasCirculacaoInterna;
    }

    public boolean isHasWc() {
        return hasWc;
    }

    public void setHasWc(boolean hasWc) {
        this.hasWc = hasWc;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isHasFraudario() {
        return hasFraudario;
    }

    public void setHasFraudario(boolean hasFraudario) {
        this.hasFraudario = hasFraudario;
    }

    public boolean isHasEstacionamento() {
        return hasEstacionamento;
    }

    public void setHasEstacionamento(boolean hasEstacionamento) {
        this.hasEstacionamento = hasEstacionamento;
    }

    public VenueInfoLugarAcessivelEnum getLugarAcessivel() {
        return lugarAcessivel;
    }

    public void setLugarAcessivel(VenueInfoLugarAcessivelEnum lugarAcessivel) {
        this.lugarAcessivel = lugarAcessivel;
    }

    public boolean isHasMapaTatil() {
        return hasMapaTatil;
    }

    public void setHasMapaTatil(boolean hasMapaTatil) {
        this.hasMapaTatil = hasMapaTatil;
    }

    public boolean isHasMesa() {
        return hasMesa;
    }

    public void setHasMesa(boolean hasMesa) {
        this.hasMesa = hasMesa;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public UsuarioBean getUser() {
        return user;
    }

    public void setUser(UsuarioBean user) {
        this.user = user;
    }

    public enum VenueInfoLugarAcessivelEnum {
        YES,
        MID,
        NO
    }

}

