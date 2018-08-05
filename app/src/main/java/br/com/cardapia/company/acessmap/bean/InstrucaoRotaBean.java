package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 12/11/17.
 */

public class InstrucaoRotaBean implements Serializable {


    private String instruction;
    private String distance;
    private String maneuver;

    public InstrucaoRotaBean (String instruction, String distance, String maneuver) {
        this.instruction = instruction;
        this.distance = distance;
        this.maneuver = maneuver;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrucaoRotaBean that = (InstrucaoRotaBean) o;

        if (instruction != null ? !instruction.equals(that.instruction) : that.instruction != null)
            return false;
        return distance != null ? distance.equals(that.distance) : that.distance == null;

    }

    @Override
    public int hashCode() {
        int result = instruction != null ? instruction.hashCode() : 0;
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        return result;
    }


}
