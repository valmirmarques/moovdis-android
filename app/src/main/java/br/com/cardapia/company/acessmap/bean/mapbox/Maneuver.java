
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
    "bearing_after",
    "bearing_before",
    "location",
    "modifier",
    "type",
    "instruction"
})
public class Maneuver implements Serializable
{

    @JsonProperty("bearing_after")
    private long bearingAfter;
    @JsonProperty("bearing_before")
    private long bearingBefore;
    @JsonProperty("location")
    private List<Double> location = null;
    @JsonProperty("modifier")
    private String modifier;
    @JsonProperty("type")
    private String type;
    @JsonProperty("instruction")
    private String instruction;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8509396886937072035L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Maneuver() {
    }

    /**
     * 
     * @param modifier
     * @param bearingBefore
     * @param location
     * @param bearingAfter
     * @param instruction
     * @param type
     */
    public Maneuver(long bearingAfter, long bearingBefore, List<Double> location, String modifier, String type, String instruction) {
        super();
        this.bearingAfter = bearingAfter;
        this.bearingBefore = bearingBefore;
        this.location = location;
        this.modifier = modifier;
        this.type = type;
        this.instruction = instruction;
    }

    @JsonProperty("bearing_after")
    public long getBearingAfter() {
        return bearingAfter;
    }

    @JsonProperty("bearing_after")
    public void setBearingAfter(long bearingAfter) {
        this.bearingAfter = bearingAfter;
    }

    @JsonProperty("bearing_before")
    public long getBearingBefore() {
        return bearingBefore;
    }

    @JsonProperty("bearing_before")
    public void setBearingBefore(long bearingBefore) {
        this.bearingBefore = bearingBefore;
    }

    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @JsonProperty("modifier")
    public String getModifier() {
        return modifier;
    }

    @JsonProperty("modifier")
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
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
        return new ToStringBuilder(this).append("bearingAfter", bearingAfter).append("bearingBefore", bearingBefore).append("location", location).append("modifier", modifier).append("type", type).append("instruction", instruction).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(modifier).append(bearingBefore).append(location).append(additionalProperties).append(bearingAfter).append(instruction).append(type).toHashCode();
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
        return new EqualsBuilder().append(modifier, rhs.modifier).append(bearingBefore, rhs.bearingBefore).append(location, rhs.location).append(additionalProperties, rhs.additionalProperties).append(bearingAfter, rhs.bearingAfter).append(instruction, rhs.instruction).append(type, rhs.type).isEquals();
    }

}
