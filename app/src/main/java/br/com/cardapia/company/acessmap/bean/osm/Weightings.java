
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
    "green",
    "quiet"
})
public class Weightings implements Serializable
{

    @JsonProperty("green")
    private Green green;
    @JsonProperty("quiet")
    private Quiet quiet;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8882388256316149583L;

    @JsonProperty("green")
    public Green getGreen() {
        return green;
    }

    @JsonProperty("green")
    public void setGreen(Green green) {
        this.green = green;
    }

    @JsonProperty("quiet")
    public Quiet getQuiet() {
        return quiet;
    }

    @JsonProperty("quiet")
    public void setQuiet(Quiet quiet) {
        this.quiet = quiet;
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
        return new ToStringBuilder(this).append("green", green).append("quiet", quiet).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(green).append(quiet).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Weightings) == false) {
            return false;
        }
        Weightings rhs = ((Weightings) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(green, rhs.green).append(quiet, rhs.quiet).isEquals();
    }

}
