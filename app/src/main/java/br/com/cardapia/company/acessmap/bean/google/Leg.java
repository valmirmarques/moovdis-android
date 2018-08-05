
package br.com.cardapia.company.acessmap.bean.google;

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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "distance",
    "duration",
    "end_address",
    "end_location",
    "start_address",
    "start_location",
    "steps",
    "traffic_speed_entry",
    "via_waypoint"
})
public class Leg implements Serializable
{

    @JsonProperty("distance")
    private Distance distance;
    @JsonProperty("duration")
    private Duration duration;
    @JsonProperty("end_address")
    private String endAddress;
    @JsonProperty("end_location")
    private EndLocation endLocation;
    @JsonProperty("start_address")
    private String startAddress;
    @JsonProperty("start_location")
    private StartLocation startLocation;
    @JsonProperty("steps")
    private List<Step> steps = null;
    @JsonProperty("traffic_speed_entry")
    private List<Object> trafficSpeedEntry = null;
    @JsonProperty("via_waypoint")
    private List<Object> viaWaypoint = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3538826628856865111L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Leg() {
    }

    /**
     * 
     * @param startAddress
     * @param duration
     * @param distance
     * @param trafficSpeedEntry
     * @param endLocation
     * @param startLocation
     * @param steps
     * @param endAddress
     * @param viaWaypoint
     */
    public Leg(Distance distance, Duration duration, String endAddress, EndLocation endLocation, String startAddress, StartLocation startLocation, List<Step> steps, List<Object> trafficSpeedEntry, List<Object> viaWaypoint) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.steps = steps;
        this.trafficSpeedEntry = trafficSpeedEntry;
        this.viaWaypoint = viaWaypoint;
    }

    @JsonProperty("distance")
    public Distance getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public Duration getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @JsonProperty("end_address")
    public String getEndAddress() {
        return endAddress;
    }

    @JsonProperty("end_address")
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    @JsonProperty("end_location")
    public EndLocation getEndLocation() {
        return endLocation;
    }

    @JsonProperty("end_location")
    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    @JsonProperty("start_address")
    public String getStartAddress() {
        return startAddress;
    }

    @JsonProperty("start_address")
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    @JsonProperty("start_location")
    public StartLocation getStartLocation() {
        return startLocation;
    }

    @JsonProperty("start_location")
    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @JsonProperty("traffic_speed_entry")
    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    @JsonProperty("traffic_speed_entry")
    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    @JsonProperty("via_waypoint")
    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    @JsonProperty("via_waypoint")
    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
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
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endAddress", endAddress).append("endLocation", endLocation).append("startAddress", startAddress).append("startLocation", startLocation).append("steps", steps).append("trafficSpeedEntry", trafficSpeedEntry).append("viaWaypoint", viaWaypoint).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(startAddress).append(duration).append(distance).append(trafficSpeedEntry).append(additionalProperties).append(endLocation).append(startLocation).append(steps).append(endAddress).append(viaWaypoint).toHashCode();
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
        return new EqualsBuilder().append(startAddress, rhs.startAddress).append(duration, rhs.duration).append(distance, rhs.distance).append(trafficSpeedEntry, rhs.trafficSpeedEntry).append(additionalProperties, rhs.additionalProperties).append(endLocation, rhs.endLocation).append(startLocation, rhs.startLocation).append(steps, rhs.steps).append(endAddress, rhs.endAddress).append(viaWaypoint, rhs.viaWaypoint).isEquals();
    }

}
