
package br.com.cardapia.company.acessmap.bean.google;

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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bounds",
    "copyrights",
    "legs",
    "overview_polyline",
    "summary",
    "warnings",
    "waypoint_order"
})
public class Route implements Serializable
{

    @JsonProperty("bounds")
    private Bounds bounds;
    @JsonProperty("copyrights")
    private String copyrights;
    @JsonProperty("legs")
    private List<Leg> legs = null;
    @JsonProperty("overview_polyline")
    private OverviewPolyline overviewPolyline;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("warnings")
    private List<String> warnings = null;
    @JsonProperty("waypoint_order")
    private List<Object> waypointOrder = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7119291250627786396L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Route() {
    }

    /**
     * 
     * @param waypointOrder
     * @param summary
     * @param bounds
     * @param copyrights
     * @param legs
     * @param warnings
     * @param overviewPolyline
     */
    public Route(Bounds bounds, String copyrights, List<Leg> legs, OverviewPolyline overviewPolyline, String summary, List<String> warnings, List<Object> waypointOrder) {
        super();
        this.bounds = bounds;
        this.copyrights = copyrights;
        this.legs = legs;
        this.overviewPolyline = overviewPolyline;
        this.summary = summary;
        this.warnings = warnings;
        this.waypointOrder = waypointOrder;
    }

    @JsonProperty("bounds")
    public Bounds getBounds() {
        return bounds;
    }

    @JsonProperty("bounds")
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @JsonProperty("copyrights")
    public String getCopyrights() {
        return copyrights;
    }

    @JsonProperty("copyrights")
    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    @JsonProperty("legs")
    public List<Leg> getLegs() {
        return legs;
    }

    @JsonProperty("legs")
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    @JsonProperty("overview_polyline")
    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    @JsonProperty("overview_polyline")
    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("warnings")
    public List<String> getWarnings() {
        return warnings;
    }

    @JsonProperty("warnings")
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    @JsonProperty("waypoint_order")
    public List<Object> getWaypointOrder() {
        return waypointOrder;
    }

    @JsonProperty("waypoint_order")
    public void setWaypointOrder(List<Object> waypointOrder) {
        this.waypointOrder = waypointOrder;
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
        return new ToStringBuilder(this).append("bounds", bounds).append("copyrights", copyrights).append("legs", legs).append("overviewPolyline", overviewPolyline).append("summary", summary).append("warnings", warnings).append("waypointOrder", waypointOrder).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(waypointOrder).append(summary).append(bounds).append(additionalProperties).append(copyrights).append(legs).append(warnings).append(overviewPolyline).toHashCode();
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
        return new EqualsBuilder().append(waypointOrder, rhs.waypointOrder).append(summary, rhs.summary).append(bounds, rhs.bounds).append(additionalProperties, rhs.additionalProperties).append(copyrights, rhs.copyrights).append(legs, rhs.legs).append(warnings, rhs.warnings).append(overviewPolyline, rhs.overviewPolyline).isEquals();
    }

}
