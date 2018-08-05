package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SidewalkBean implements Serializable {

    private String _id;
    private LocationBean locationStart;
    private LocationBean locationEnd;
    private SidewalkTypeEnum sidewalkType;
    private Date dateCreated;
    private UsuarioBean user;
    private int likeCount;
    private int dificultLevel;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public SidewalkTypeEnum getSidewalkType() {
        return sidewalkType;
    }

    public void setSidewalkType(SidewalkTypeEnum sidewalkType) {
        this.sidewalkType = sidewalkType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UsuarioBean getUser() {
        return user;
    }

    public void setUser(UsuarioBean user) {
        this.user = user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDificultLevel() {
        return dificultLevel;
    }

    public void setDificultLevel(int dificultLevel) {
        this.dificultLevel = dificultLevel;
    }

    public LocationBean getLocationStart() {
        return locationStart;
    }

    public void setLocationStart(LocationBean locationStart) {
        this.locationStart = locationStart;
    }

    public LocationBean getLocationEnd() {
        return locationEnd;
    }

    public void setLocationEnd(LocationBean locationEnd) {
        this.locationEnd = locationEnd;
    }

    public enum SidewalkTypeEnum {

        PAVER(0, new String[]{"#BFBFBF", "#BFBFBF"}),
        GRAMA(1, new String[]{"#759854", "#F4F0EC"}),
        CALCADAO(2, new String[]{"#000000", "#FEFEFE"}),
        PEDRA_SABAO(3, new String[]{"#807466", "#807466"}),
        BLOQUETE(4, new String[]{"#A9939C", "#A9939C"}),
        CONCRETO(5, new String[]{"#989898", "#989898"}),
        TERRA(6, new String[]{"#9F6C55", "#9F6C55"}),
        ASFALTO(7, new String[]{"#121212", "#121212"}),
        PEDRA(8, new String[]{"#C6B692", "#D2DEDE"}),
        NAO_IDENTIFICADO(9, new String[]{"#FFFFFF", "#FFFFFF"});

        int id;
        String[] polyColors;

        SidewalkTypeEnum(int id, String[] polyColors) {
            this.id = id;
            this.polyColors = polyColors;
        }

        /**
         * @param id
         * @return
         */
        public static SidewalkTypeEnum fromId(int id) {
            for (SidewalkTypeEnum e : values()) {
                if (e.id == id) {
                    return e;
                }
            }
            return NAO_IDENTIFICADO;
        }

        public String[] getSidewalkTypeColor() {
            return this.polyColors;
        }

    }
}
