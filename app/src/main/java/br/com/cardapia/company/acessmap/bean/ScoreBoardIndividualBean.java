package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 12/11/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ScoreBoardIndividualBean implements Serializable {


    private long totalPoints;
    private int count;
    private long countImages;


    public long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getCountImages() {
        return countImages;
    }

    public void setCountImages(long countImages) {
        this.countImages = countImages;
    }
}
