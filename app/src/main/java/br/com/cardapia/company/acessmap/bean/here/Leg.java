
package br.com.cardapia.company.acessmap.bean.here;

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
    "start",
    "end",
    "length",
    "travelTime",
    "maneuver"
})
public class Leg {

    @JsonProperty("start")
    private Start start;
    @JsonProperty("end")
    private End end;
    @JsonProperty("length")
    private long length;
    @JsonProperty("travelTime")
    private long travelTime;
    @JsonProperty("maneuver")
    private List<Maneuver> maneuver = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("start")
    public Start getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(Start start) {
        this.start = start;
    }

    @JsonProperty("end")
    public End getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(End end) {
        this.end = end;
    }

    @JsonProperty("length")
    public long getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(long length) {
        this.length = length;
    }

    @JsonProperty("travelTime")
    public long getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("maneuver")
    public List<Maneuver> getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(List<Maneuver> maneuver) {
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
        return new ToStringBuilder(this).append("start", start).append("end", end).append("length", length).append("travelTime", travelTime).append("maneuver", maneuver).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(travelTime).append(start).append(additionalProperties).append(length).append(maneuver).append(end).toHashCode();
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
        return new EqualsBuilder().append(travelTime, rhs.travelTime).append(start, rhs.start).append(additionalProperties, rhs.additionalProperties).append(length, rhs.length).append(maneuver, rhs.maneuver).append(end, rhs.end).isEquals();
    }

}
