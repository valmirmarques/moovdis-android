
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
    "type",
    "transportModes",
    "trafficMode",
    "feature"
})
public class Mode {

    @JsonProperty("type")
    private String type;
    @JsonProperty("transportModes")
    private List<String> transportModes = null;
    @JsonProperty("trafficMode")
    private String trafficMode;
    @JsonProperty("feature")
    private List<Object> feature = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("transportModes")
    public List<String> getTransportModes() {
        return transportModes;
    }

    @JsonProperty("transportModes")
    public void setTransportModes(List<String> transportModes) {
        this.transportModes = transportModes;
    }

    @JsonProperty("trafficMode")
    public String getTrafficMode() {
        return trafficMode;
    }

    @JsonProperty("trafficMode")
    public void setTrafficMode(String trafficMode) {
        this.trafficMode = trafficMode;
    }

    @JsonProperty("feature")
    public List<Object> getFeature() {
        return feature;
    }

    @JsonProperty("feature")
    public void setFeature(List<Object> feature) {
        this.feature = feature;
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
        return new ToStringBuilder(this).append("type", type).append("transportModes", transportModes).append("trafficMode", trafficMode).append("feature", feature).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(trafficMode).append(additionalProperties).append(feature).append(transportModes).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Mode) == false) {
            return false;
        }
        Mode rhs = ((Mode) other);
        return new EqualsBuilder().append(trafficMode, rhs.trafficMode).append(additionalProperties, rhs.additionalProperties).append(feature, rhs.feature).append(transportModes, rhs.transportModes).append(type, rhs.type).isEquals();
    }

}
