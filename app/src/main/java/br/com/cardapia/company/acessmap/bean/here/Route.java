
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
    "waypoint",
    "mode",
    "leg",
    "summary"
})
public class Route {

    @JsonProperty("waypoint")
    private List<Waypoint> waypoint = null;
    @JsonProperty("mode")
    private Mode mode;
    @JsonProperty("leg")
    private List<Leg> leg = null;
    @JsonProperty("summary")
    private Summary summary;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("waypoint")
    public List<Waypoint> getWaypoint() {
        return waypoint;
    }

    @JsonProperty("waypoint")
    public void setWaypoint(List<Waypoint> waypoint) {
        this.waypoint = waypoint;
    }

    @JsonProperty("mode")
    public Mode getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @JsonProperty("leg")
    public List<Leg> getLeg() {
        return leg;
    }

    @JsonProperty("leg")
    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
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
        return new ToStringBuilder(this).append("waypoint", waypoint).append("mode", mode).append("leg", leg).append("summary", summary).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summary).append(leg).append(additionalProperties).append(waypoint).append(mode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Route) == false) {
            return false;
        }
        Route rhs = ((Route) other);
        return new EqualsBuilder().append(summary, rhs.summary).append(leg, rhs.leg).append(additionalProperties, rhs.additionalProperties).append(waypoint, rhs.waypoint).append(mode, rhs.mode).isEquals();
    }

}
