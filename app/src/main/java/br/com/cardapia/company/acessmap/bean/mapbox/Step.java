
package br.com.cardapia.company.acessmap.bean.mapbox;

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
    "intersections",
    "driving_side",
    "geometry",
    "mode",
    "maneuver",
    "weight",
    "duration",
    "name",
    "distance"
})
public class Step implements Serializable
{

    @JsonProperty("intersections")
    private List<Intersection> intersections = null;
    @JsonProperty("driving_side")
    private String drivingSide;
    @JsonProperty("geometry")
    private String geometry;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("maneuver")
    private Maneuver maneuver;
    @JsonProperty("weight")
    private long weight;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("name")
    private String name;
    @JsonProperty("distance")
    private long distance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 4542255554768343132L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step() {
    }

    /**
     * 
     * @param drivingSide
     * @param intersections
     * @param distance
     * @param duration
     * @param weight
     * @param name
     * @param maneuver
     * @param mode
     * @param geometry
     */
    public Step(List<Intersection> intersections, String drivingSide, String geometry, String mode, Maneuver maneuver, long weight, long duration, String name, long distance) {
        super();
        this.intersections = intersections;
        this.drivingSide = drivingSide;
        this.geometry = geometry;
        this.mode = mode;
        this.maneuver = maneuver;
        this.weight = weight;
        this.duration = duration;
        this.name = name;
        this.distance = distance;
    }

    @JsonProperty("intersections")
    public List<Intersection> getIntersections() {
        return intersections;
    }

    @JsonProperty("intersections")
    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    @JsonProperty("driving_side")
    public String getDrivingSide() {
        return drivingSide;
    }

    @JsonProperty("driving_side")
    public void setDrivingSide(String drivingSide) {
        this.drivingSide = drivingSide;
    }

    @JsonProperty("geometry")
    public String getGeometry() {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("maneuver")
    public Maneuver getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }

    @JsonProperty("weight")
    public long getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(long weight) {
        this.weight = weight;
    }

    @JsonProperty("duration")
    public long getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("distance")
    public long getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(long distance) {
        this.distance = distance;
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
        return new ToStringBuilder(this).append("intersections", intersections).append("drivingSide", drivingSide).append("geometry", geometry).append("mode", mode).append("maneuver", maneuver).append("weight", weight).append("duration", duration).append("name", name).append("distance", distance).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(drivingSide).append(intersections).append(distance).append(duration).append(weight).append(additionalProperties).append(name).append(maneuver).append(mode).append(geometry).toHashCode();
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
        return new EqualsBuilder().append(drivingSide, rhs.drivingSide).append(intersections, rhs.intersections).append(distance, rhs.distance).append(duration, rhs.duration).append(weight, rhs.weight).append(additionalProperties, rhs.additionalProperties).append(name, rhs.name).append(maneuver, rhs.maneuver).append(mode, rhs.mode).append(geometry, rhs.geometry).isEquals();
    }

}
