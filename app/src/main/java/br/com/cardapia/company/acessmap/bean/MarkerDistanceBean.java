package br.com.cardapia.company.acessmap.bean;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class MarkerDistanceBean extends MarkerBean implements Serializable, Comparable<MarkerDistanceBean> {

    private double distance;
    private String addressDescription;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(@NonNull MarkerDistanceBean markerDistanceBean) {
        return Double.compare(this.distance, markerDistanceBean.distance);
    }

    public String getAddressDescription() {
        return addressDescription;
    }

    public void setAddressDescription(String addressDescription) {
        this.addressDescription = addressDescription;
    }
}
