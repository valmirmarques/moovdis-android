
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
    "routes",
    "waypoints",
    "code",
    "uuid"
})
public class MapBoxDirections implements Serializable
{

    @JsonProperty("routes")
    private List<Route> routes = null;
    @JsonProperty("waypoints")
    private List<Waypoint> waypoints = null;
    @JsonProperty("code")
    private String code;
    @JsonProperty("uuid")
    private String uuid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2341801434329397516L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MapBoxDirections() {
    }

    /**
     * 
     * @param routes
     * @param uuid
     * @param code
     * @param waypoints
     */
    public MapBoxDirections(List<Route> routes, List<Waypoint> waypoints, String code, String uuid) {
        super();
        this.routes = routes;
        this.waypoints = waypoints;
        this.code = code;
        this.uuid = uuid;
    }

    @JsonProperty("routes")
    public List<Route> getRoutes() {
        return routes;
    }

    @JsonProperty("routes")
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @JsonProperty("waypoints")
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    @JsonProperty("waypoints")
    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return new ToStringBuilder(this).append("routes", routes).append("waypoints", waypoints).append("code", code).append("uuid", uuid).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(routes).append(additionalProperties).append(uuid).append(code).append(waypoints).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MapBoxDirections) == false) {
            return false;
        }
        MapBoxDirections rhs = ((MapBoxDirections) other);
        return new EqualsBuilder().append(routes, rhs.routes).append(additionalProperties, rhs.additionalProperties).append(uuid, rhs.uuid).append(code, rhs.code).append(waypoints, rhs.waypoints).isEquals();
    }

}
