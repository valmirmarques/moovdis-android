
package br.com.cardapia.company.acessmap.bean.google;

import java.io.Serializable;
import java.util.HashMap;
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
    "end_location",
    "html_instructions",
    "polyline",
    "start_location",
    "travel_mode",
    "maneuver"
})
public class Step implements Serializable
{

    @JsonProperty("distance")
    private Distance_ distance;
    @JsonProperty("duration")
    private Duration_ duration;
    @JsonProperty("end_location")
    private EndLocation_ endLocation;
    @JsonProperty("html_instructions")
    private String htmlInstructions;
    @JsonProperty("polyline")
    private Polyline polyline;
    @JsonProperty("start_location")
    private StartLocation_ startLocation;
    @JsonProperty("travel_mode")
    private String travelMode;
    @JsonProperty("maneuver")
    private String maneuver;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6577105186027402495L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step() {
    }

    /**
     * 
     * @param duration
     * @param distance
     * @param polyline
     * @param endLocation
     * @param htmlInstructions
     * @param startLocation
     * @param maneuver
     * @param travelMode
     */
    public Step(Distance_ distance, Duration_ duration, EndLocation_ endLocation, String htmlInstructions, Polyline polyline, StartLocation_ startLocation, String travelMode, String maneuver) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.endLocation = endLocation;
        this.htmlInstructions = htmlInstructions;
        this.polyline = polyline;
        this.startLocation = startLocation;
        this.travelMode = travelMode;
        this.maneuver = maneuver;
    }

    @JsonProperty("distance")
    public Distance_ getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public Duration_ getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Duration_ duration) {
        this.duration = duration;
    }

    @JsonProperty("end_location")
    public EndLocation_ getEndLocation() {
        return endLocation;
    }

    @JsonProperty("end_location")
    public void setEndLocation(EndLocation_ endLocation) {
        this.endLocation = endLocation;
    }

    @JsonProperty("html_instructions")
    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    @JsonProperty("html_instructions")
    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    @JsonProperty("polyline")
    public Polyline getPolyline() {
        return polyline;
    }

    @JsonProperty("polyline")
    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    @JsonProperty("start_location")
    public StartLocation_ getStartLocation() {
        return startLocation;
    }

    @JsonProperty("start_location")
    public void setStartLocation(StartLocation_ startLocation) {
        this.startLocation = startLocation;
    }

    @JsonProperty("travel_mode")
    public String getTravelMode() {
        return travelMode;
    }

    @JsonProperty("travel_mode")
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    @JsonProperty("maneuver")
    public String getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
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
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endLocation", endLocation).append("htmlInstructions", htmlInstructions).append("polyline", polyline).append("startLocation", startLocation).append("travelMode", travelMode).append("maneuver", maneuver).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(distance).append(polyline).append(additionalProperties).append(endLocation).append(htmlInstructions).append(startLocation).append(maneuver).append(travelMode).toHashCode();
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
        return new EqualsBuilder().append(duration, rhs.duration).append(distance, rhs.distance).append(polyline, rhs.polyline).append(additionalProperties, rhs.additionalProperties).append(endLocation, rhs.endLocation).append(htmlInstructions, rhs.htmlInstructions).append(startLocation, rhs.startLocation).append(maneuver, rhs.maneuver).append(travelMode, rhs.travelMode).isEquals();
    }

}
