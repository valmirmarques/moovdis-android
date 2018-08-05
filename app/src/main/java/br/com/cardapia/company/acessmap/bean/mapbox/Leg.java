
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
    "summary",
    "weight",
    "duration",
    "steps",
    "distance"
})
public class Leg implements Serializable
{

    @JsonProperty("summary")
    private String summary;
    @JsonProperty("weight")
    private long weight;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("steps")
    private List<Step> steps = null;
    @JsonProperty("distance")
    private double distance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1247205900235553206L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Leg() {
    }

    /**
     * 
     * @param summary
     * @param distance
     * @param duration
     * @param weight
     * @param steps
     */
    public Leg(String summary, long weight, long duration, List<Step> steps, double distance) {
        super();
        this.summary = summary;
        this.weight = weight;
        this.duration = duration;
        this.steps = steps;
        this.distance = distance;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
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

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
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
        return new ToStringBuilder(this).append("summary", summary).append("weight", weight).append("duration", duration).append("steps", steps).append("distance", distance).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summary).append(distance).append(duration).append(weight).append(additionalProperties).append(steps).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leg) == false) {
            return false;
        }
        Leg rhs = ((Leg) other);
        return new EqualsBuilder().append(summary, rhs.summary).append(distance, rhs.distance).append(duration, rhs.duration).append(weight, rhs.weight).append(additionalProperties, rhs.additionalProperties).append(steps, rhs.steps).isEquals();
    }

}
