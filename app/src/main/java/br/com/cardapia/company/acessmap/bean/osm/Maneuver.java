
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
    "bearing_before",
    "bearing_after",
    "location"
})
public class Maneuver implements Serializable
{

    @JsonProperty("bearing_before")
    private int bearingBefore;
    @JsonProperty("bearing_after")
    private int bearingAfter;
    @JsonProperty("location")
    private List<Double> location = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6354466679280516219L;

    @JsonProperty("bearing_before")
    public int getBearingBefore() {
        return bearingBefore;
    }

    @JsonProperty("bearing_before")
    public void setBearingBefore(int bearingBefore) {
        this.bearingBefore = bearingBefore;
    }

    @JsonProperty("bearing_after")
    public int getBearingAfter() {
        return bearingAfter;
    }

    @JsonProperty("bearing_after")
    public void setBearingAfter(int bearingAfter) {
        this.bearingAfter = bearingAfter;
    }

    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
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
        return new ToStringBuilder(this).append("bearingBefore", bearingBefore).append("bearingAfter", bearingAfter).append("location", location).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bearingBefore).append(location).append(additionalProperties).append(bearingAfter).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Maneuver) == false) {
            return false;
        }
        Maneuver rhs = ((Maneuver) other);
        return new EqualsBuilder().append(bearingBefore, rhs.bearingBefore).append(location, rhs.location).append(additionalProperties, rhs.additionalProperties).append(bearingAfter, rhs.bearingAfter).isEquals();
    }

}
