
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
    "distance",
    "baseTime",
    "flags",
    "text",
    "travelTime",
    "_type"
})
public class Summary {

    @JsonProperty("distance")
    private long distance;
    @JsonProperty("baseTime")
    private long baseTime;
    @JsonProperty("flags")
    private List<String> flags = null;
    @JsonProperty("text")
    private String text;
    @JsonProperty("travelTime")
    private long travelTime;
    @JsonProperty("_type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("distance")
    public long getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(long distance) {
        this.distance = distance;
    }

    @JsonProperty("baseTime")
    public long getBaseTime() {
        return baseTime;
    }

    @JsonProperty("baseTime")
    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

    @JsonProperty("flags")
    public List<String> getFlags() {
        return flags;
    }

    @JsonProperty("flags")
    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("travelTime")
    public long getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
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
        return new ToStringBuilder(this).append("distance", distance).append("baseTime", baseTime).append("flags", flags).append("text", text).append("travelTime", travelTime).append("type", type).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(travelTime).append(text).append(distance).append(flags).append(additionalProperties).append(baseTime).append(type).toHashCode();
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
        return new EqualsBuilder().append(travelTime, rhs.travelTime).append(text, rhs.text).append(distance, rhs.distance).append(flags, rhs.flags).append(additionalProperties, rhs.additionalProperties).append(baseTime, rhs.baseTime).append(type, rhs.type).isEquals();
    }

}
