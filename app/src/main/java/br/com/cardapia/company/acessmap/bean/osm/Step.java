
package br.com.cardapia.company.acessmap.bean.osm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "distance",
    "duration",
    "type",
    "instruction",
    "name",
    "maneuver",
    "way_points"
})
public class Step implements Serializable
{

    @JsonProperty("distance")
    private int distance;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("type")
    private int type;
    @JsonProperty("instruction")
    private String instruction;
    @JsonProperty("name")
    private String name;
    @JsonProperty("maneuver")
    private Maneuver maneuver;
    @JsonProperty("way_points")
    private List<Integer> wayPoints = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 7605104970492485590L;

    @JsonProperty("distance")
    public int getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public int getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @JsonProperty("type")
    public int getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(int type) {
        this.type = type;
    }

    @JsonProperty("instruction")
    public String getInstruction() {
        return instruction;
    }

    @JsonProperty("instruction")
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("maneuver")
    public Maneuver getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }

    @JsonProperty("way_points")
    public List<Integer> getWayPoints() {
        return wayPoints;
    }

    @JsonProperty("way_points")
    public void setWayPoints(List<Integer> wayPoints) {
        this.wayPoints = wayPoints;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("type", type).append("instruction", instruction).append("name", name).append("maneuver", maneuver).append("wayPoints", wayPoints).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(distance).append(wayPoints).append(additionalProperties).append(name).append(instruction).append(type).append(maneuver).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Step) == false) {
            return false;
        }
        Step rhs = ((Step) other);
        return new EqualsBuilder().append(duration, rhs.duration).append(distance, rhs.distance).append(wayPoints, rhs.wayPoints).append(additionalProperties, rhs.additionalProperties).append(name, rhs.name).append(instruction, rhs.instruction).append(type, rhs.type).append(maneuver, rhs.maneuver).isEquals();
    }

}
