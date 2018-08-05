
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
    "avoid_features",
    "avoid_polygons",
    "profile_params"
})
public class Options implements Serializable
{

    @JsonProperty("avoid_features")
    private String avoidFeatures;
    @JsonProperty("avoid_polygons")
    private AvoidPolygons avoidPolygons;
    @JsonProperty("profile_params")
    private ProfileParams profileParams;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6858207059637324806L;

    @JsonProperty("avoid_features")
    public String getAvoidFeatures() {
        return avoidFeatures;
    }

    @JsonProperty("avoid_features")
    public void setAvoidFeatures(String avoidFeatures) {
        this.avoidFeatures = avoidFeatures;
    }

    @JsonProperty("avoid_polygons")
    public AvoidPolygons getAvoidPolygons() {
        return avoidPolygons;
    }

    @JsonProperty("avoid_polygons")
    public void setAvoidPolygons(AvoidPolygons avoidPolygons) {
        this.avoidPolygons = avoidPolygons;
    }

    @JsonProperty("profile_params")
    public ProfileParams getProfileParams() {
        return profileParams;
    }

    @JsonProperty("profile_params")
    public void setProfileParams(ProfileParams profileParams) {
        this.profileParams = profileParams;
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
        return new ToStringBuilder(this).append("avoidFeatures", avoidFeatures).append("avoidPolygons", avoidPolygons).append("profileParams", profileParams).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(profileParams).append(avoidPolygons).append(avoidFeatures).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Options) == false) {
            return false;
        }
        Options rhs = ((Options) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(profileParams, rhs.profileParams).append(avoidPolygons, rhs.avoidPolygons).append(avoidFeatures, rhs.avoidFeatures).isEquals();
    }

}
