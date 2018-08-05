
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
    "geometry",
    "legs",
    "weight_name",
    "weight",
    "duration",
    "distance"
})
public class Route implements Serializable
{

    @JsonProperty("geometry")
    private String geometry;
    @JsonProperty("legs")
    private List<Leg> legs = null;
    @JsonProperty("weight_name")
    private String weightName;
    @JsonProperty("weight")
    private long weight;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("distance")
    private double distance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 869445936216654544L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Route() {
    }

    /**
     * 
     * @param distance
     * @param duration
     * @param weightName
     * @param weight
     * @param legs
     * @param geometry
     */
    public Route(String geometry, List<Leg> legs, String weightName, long weight, long duration, double distance) {
        super();
        this.geometry = geometry;
        this.legs = legs;
        this.weightName = weightName;
        this.weight = weight;
        this.duration = duration;
        this.distance = distance;
    }

    @JsonProperty("geometry")
    public String getGeometry() {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    @JsonProperty("legs")
    public List<Leg> getLegs() {
        return legs;
    }

    @JsonProperty("legs")
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    @JsonProperty("weight_name")
    public String getWeightName() {
        return weightName;
    }

    @JsonProperty("weight_name")
    public void setWeightName(String weightName) {
        this.weightName = weightName;
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

    @JsonProperty("distance")
    public double getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(double distance) {
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
        return new ToStringBuilder(this).append("geometry", geometry).append("legs", legs).append("weightName", weightName).append("weight", weight).append("duration", duration).append("distance", distance).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(distance).append(duration).append(weightName).append(weight).append(additionalProperties).append(legs).append(geometry).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Route) == false) {
            return false;
        }
        Route rhs = ((Route) other);
        return new EqualsBuilder().append(distance, rhs.distance).append(duration, rhs.duration).append(weightName, rhs.weightName).append(weight, rhs.weight).append(additionalProperties, rhs.additionalProperties).append(legs, rhs.legs).append(geometry, rhs.geometry).isEquals();
    }

}
