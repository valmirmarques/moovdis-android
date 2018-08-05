
package br.com.cardapia.company.acessmap.bean.osm;

import java.io.Serializable;
import java.util.HashMap;
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
    "waytypes",
    "traildifficulty"
})
public class Extras implements Serializable
{

    @JsonProperty("waytypes")
    private Waytypes waytypes;
    @JsonProperty("traildifficulty")
    private Traildifficulty traildifficulty;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5456931329139837026L;

    @JsonProperty("waytypes")
    public Waytypes getWaytypes() {
        return waytypes;
    }

    @JsonProperty("waytypes")
    public void setWaytypes(Waytypes waytypes) {
        this.waytypes = waytypes;
    }

    @JsonProperty("traildifficulty")
    public Traildifficulty getTraildifficulty() {
        return traildifficulty;
    }

    @JsonProperty("traildifficulty")
    public void setTraildifficulty(Traildifficulty traildifficulty) {
        this.traildifficulty = traildifficulty;
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
        return new ToStringBuilder(this).append("waytypes", waytypes).append("traildifficulty", traildifficulty).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(waytypes).append(additionalProperties).append(traildifficulty).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Extras) == false) {
            return false;
        }
        Extras rhs = ((Extras) other);
        return new EqualsBuilder().append(waytypes, rhs.waytypes).append(additionalProperties, rhs.additionalProperties).append(traildifficulty, rhs.traildifficulty).isEquals();
    }

}
