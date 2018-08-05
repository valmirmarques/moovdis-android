
package br.com.cardapia.company.acessmap.bean.osm;

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
    "profile",
    "preference",
    "coordinates",
    "language",
    "units",
    "geometry",
    "geometry_format",
    "geometry_simplify",
    "instructions_format",
    "instructions",
    "elevation",
    "options"
})
public class Query implements Serializable
{

    @JsonProperty("profile")
    private String profile;
    @JsonProperty("preference")
    private String preference;
    @JsonProperty("coordinates")
    private List<List<Double>> coordinates = null;
    @JsonProperty("language")
    private String language;
    @JsonProperty("units")
    private String units;
    @JsonProperty("geometry")
    private boolean geometry;
    @JsonProperty("geometry_format")
    private String geometryFormat;
    @JsonProperty("geometry_simplify")
    private boolean geometrySimplify;
    @JsonProperty("instructions_format")
    private String instructionsFormat;
    @JsonProperty("instructions")
    private boolean instructions;
    @JsonProperty("elevation")
    private boolean elevation;
    @JsonProperty("options")
    private Options options;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3161151029496916849L;

    @JsonProperty("profile")
    public String getProfile() {
        return profile;
    }

    @JsonProperty("profile")
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @JsonProperty("preference")
    public String getPreference() {
        return preference;
    }

    @JsonProperty("preference")
    public void setPreference(String preference) {
        this.preference = preference;
    }

    @JsonProperty("coordinates")
    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    @JsonProperty("geometry")
    public boolean isGeometry() {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(boolean geometry) {
        this.geometry = geometry;
    }

    @JsonProperty("geometry_format")
    public String getGeometryFormat() {
        return geometryFormat;
    }

    @JsonProperty("geometry_format")
    public void setGeometryFormat(String geometryFormat) {
        this.geometryFormat = geometryFormat;
    }

    @JsonProperty("geometry_simplify")
    public boolean isGeometrySimplify() {
        return geometrySimplify;
    }

    @JsonProperty("geometry_simplify")
    public void setGeometrySimplify(boolean geometrySimplify) {
        this.geometrySimplify = geometrySimplify;
    }

    @JsonProperty("instructions_format")
    public String getInstructionsFormat() {
        return instructionsFormat;
    }

    @JsonProperty("instructions_format")
    public void setInstructionsFormat(String instructionsFormat) {
        this.instructionsFormat = instructionsFormat;
    }

    @JsonProperty("instructions")
    public boolean isInstructions() {
        return instructions;
    }

    @JsonProperty("instructions")
    public void setInstructions(boolean instructions) {
        this.instructions = instructions;
    }

    @JsonProperty("elevation")
    public boolean isElevation() {
        return elevation;
    }

    @JsonProperty("elevation")
    public void setElevation(boolean elevation) {
        this.elevation = elevation;
    }

    @JsonProperty("options")
    public Options getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(Options options) {
        this.options = options;
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
        return new ToStringBuilder(this).append("profile", profile).append("preference", preference).append("coordinates", coordinates).append("language", language).append("units", units).append("geometry", geometry).append("geometryFormat", geometryFormat).append("geometrySimplify", geometrySimplify).append("instructionsFormat", instructionsFormat).append("instructions", instructions).append("elevation", elevation).append("options", options).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(instructions).append(elevation).append(geometrySimplify).append(units).append(geometry).append(instructionsFormat).append(preference).append(additionalProperties).append(language).append(coordinates).append(options).append(geometryFormat).append(profile).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Query) == false) {
            return false;
        }
        Query rhs = ((Query) other);
        return new EqualsBuilder().append(instructions, rhs.instructions).append(elevation, rhs.elevation).append(geometrySimplify, rhs.geometrySimplify).append(units, rhs.units).append(geometry, rhs.geometry).append(instructionsFormat, rhs.instructionsFormat).append(preference, rhs.preference).append(additionalProperties, rhs.additionalProperties).append(language, rhs.language).append(coordinates, rhs.coordinates).append(options, rhs.options).append(geometryFormat, rhs.geometryFormat).append(profile, rhs.profile).isEquals();
    }

}
