
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
    "out",
    "entry",
    "bearings",
    "location",
    "in"
})
public class Intersection implements Serializable
{

    @JsonProperty("out")
    private long out;
    @JsonProperty("entry")
    private List<Boolean> entry = null;
    @JsonProperty("bearings")
    private List<Long> bearings = null;
    @JsonProperty("location")
    private List<Double> location = null;
    @JsonProperty("in")
    private long in;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2302365418199428007L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Intersection() {
    }

    /**
     * 
     * @param location
     * @param bearings
     * @param entry
     * @param in
     * @param out
     */
    public Intersection(long out, List<Boolean> entry, List<Long> bearings, List<Double> location, long in) {
        super();
        this.out = out;
        this.entry = entry;
        this.bearings = bearings;
        this.location = location;
        this.in = in;
    }

    @JsonProperty("out")
    public long getOut() {
        return out;
    }

    @JsonProperty("out")
    public void setOut(long out) {
        this.out = out;
    }

    @JsonProperty("entry")
    public List<Boolean> getEntry() {
        return entry;
    }

    @JsonProperty("entry")
    public void setEntry(List<Boolean> entry) {
        this.entry = entry;
    }

    @JsonProperty("bearings")
    public List<Long> getBearings() {
        return bearings;
    }

    @JsonProperty("bearings")
    public void setBearings(List<Long> bearings) {
        this.bearings = bearings;
    }

    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @JsonProperty("in")
    public long getIn() {
        return in;
    }

    @JsonProperty("in")
    public void setIn(long in) {
        this.in = in;
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
        return new ToStringBuilder(this).append("out", out).append("entry", entry).append("bearings", bearings).append("location", location).append("in", in).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(location).append(additionalProperties).append(bearings).append(entry).append(in).append(out).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Intersection) == false) {
            return false;
        }
        Intersection rhs = ((Intersection) other);
        return new EqualsBuilder().append(location, rhs.location).append(additionalProperties, rhs.additionalProperties).append(bearings, rhs.bearings).append(entry, rhs.entry).append(in, rhs.in).append(out, rhs.out).isEquals();
    }

}
