package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 12/11/17.
 */

public class AdultBean implements Serializable{


    @Override
    public String toString() {
        return "AdultBean{" +
                "isAdultContent=" + isAdultContent +
                ", isRacyContent=" + isRacyContent +
                ", adultScore=" + adultScore +
                ", racyScore=" + racyScore +
                '}';
    }

    private boolean isAdultContent;
    private boolean isRacyContent;
    private double adultScore;
    private double racyScore;

    public boolean isAdultContent() {
        return isAdultContent;
    }

    public void setAdultContent(boolean adultContent) {
        isAdultContent = adultContent;
    }

    public boolean isRacyContent() {
        return isRacyContent;
    }

    public void setRacyContent(boolean racyContent) {
        isRacyContent = racyContent;
    }

    public double getAdultScore() {
        return adultScore;
    }

    public void setAdultScore(double adultScore) {
        this.adultScore = adultScore;
    }

    public double getRacyScore() {
        return racyScore;
    }

    public void setRacyScore(double racyScore) {
        this.racyScore = racyScore;
    }

}
