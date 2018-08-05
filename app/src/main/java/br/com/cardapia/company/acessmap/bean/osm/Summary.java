
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
    "distance",
    "duration",
    "ascent",
    "descent"
})
public class Summary implements Serializable
{

    @JsonProperty("distance")
    private double distance;
    @JsonProperty("duration")
    private double duration;
    @JsonProperty("ascent")
    private double ascent;
    @JsonProperty("descent")
    private double descent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4032638859276378980L;

    @JsonProperty("distance")
    public double getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public double getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(double duration) {
        this.duration = duration;
    }

    @JsonProperty("ascent")
    public double getAscent() {
        return ascent;
    }

    @JsonProperty("ascent")
    public void setAscent(double ascent) {
        this.ascent = ascent;
    }

    @JsonProperty("descent")
    public double getDescent() {
        return descent;
    }

    @JsonProperty("descent")
    public void setDescent(double descent) {
        this.descent = descent;
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
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("ascent", ascent).append("descent", descent).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(distance).append(additionalProperties).append(descent).append(ascent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Summary) == false) {
            return false;
        }
        Summary rhs = ((Summary) other);
        return new EqualsBuilder().append(duration, rhs.duration).append(distance, rhs.distance).append(additionalProperties, rhs.additionalProperties).append(descent, rhs.descent).append(ascent, rhs.ascent).isEquals();
    }

}
