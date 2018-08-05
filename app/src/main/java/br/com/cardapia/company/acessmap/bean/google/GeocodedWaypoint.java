
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
    "geocoder_status",
    "place_id",
    "types"
})
public class GeocodedWaypoint implements Serializable
{

    @JsonProperty("geocoder_status")
    private String geocoderStatus;
    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("types")
    private List<String> types = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6116653067967959910L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GeocodedWaypoint() {
    }

    /**
     * 
     * @param geocoderStatus
     * @param placeId
     * @param types
     */
    public GeocodedWaypoint(String geocoderStatus, String placeId, List<String> types) {
        super();
        this.geocoderStatus = geocoderStatus;
        this.placeId = placeId;
        this.types = types;
    }

    @JsonProperty("geocoder_status")
    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    @JsonProperty("geocoder_status")
    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    @JsonProperty("place_id")
    public String getPlaceId() {
        return placeId;
    }

    @JsonProperty("place_id")
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @JsonProperty("types")
    public List<String> getTypes() {
        return types;
    }

    @JsonProperty("types")
    public void setTypes(List<String> types) {
        this.types = types;
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
        return new ToStringBuilder(this).append("geocoderStatus", geocoderStatus).append("placeId", placeId).append("types", types).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(geocoderStatus).append(placeId).append(additionalProperties).append(types).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GeocodedWaypoint) == false) {
            return false;
        }
        GeocodedWaypoint rhs = ((GeocodedWaypoint) other);
        return new EqualsBuilder().append(geocoderStatus, rhs.geocoderStatus).append(placeId, rhs.placeId).append(additionalProperties, rhs.additionalProperties).append(types, rhs.types).isEquals();
    }

}
