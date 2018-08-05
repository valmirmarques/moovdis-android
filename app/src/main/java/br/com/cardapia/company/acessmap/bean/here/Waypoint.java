
package br.com.cardapia.company.acessmap.bean.here;

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
    "linkId",
    "mappedPosition",
    "originalPosition",
    "type",
    "spot",
    "sideOfStreet",
    "mappedRoadName",
    "label",
    "shapeIndex"
})
public class Waypoint {

    @JsonProperty("linkId")
    private String linkId;
    @JsonProperty("mappedPosition")
    private MappedPosition mappedPosition;
    @JsonProperty("originalPosition")
    private OriginalPosition originalPosition;
    @JsonProperty("type")
    private String type;
    @JsonProperty("spot")
    private double spot;
    @JsonProperty("sideOfStreet")
    private String sideOfStreet;
    @JsonProperty("mappedRoadName")
    private String mappedRoadName;
    @JsonProperty("label")
    private String label;
    @JsonProperty("shapeIndex")
    private long shapeIndex;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("linkId")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("linkId")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("mappedPosition")
    public MappedPosition getMappedPosition() {
        return mappedPosition;
    }

    @JsonProperty("mappedPosition")
    public void setMappedPosition(MappedPosition mappedPosition) {
        this.mappedPosition = mappedPosition;
    }

    @JsonProperty("originalPosition")
    public OriginalPosition getOriginalPosition() {
        return originalPosition;
    }

    @JsonProperty("originalPosition")
    public void setOriginalPosition(OriginalPosition originalPosition) {
        this.originalPosition = originalPosition;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("spot")
    public double getSpot() {
        return spot;
    }

    @JsonProperty("spot")
    public void setSpot(double spot) {
        this.spot = spot;
    }

    @JsonProperty("sideOfStreet")
    public String getSideOfStreet() {
        return sideOfStreet;
    }

    @JsonProperty("sideOfStreet")
    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    @JsonProperty("mappedRoadName")
    public String getMappedRoadName() {
        return mappedRoadName;
    }

    @JsonProperty("mappedRoadName")
    public void setMappedRoadName(String mappedRoadName) {
        this.mappedRoadName = mappedRoadName;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("shapeIndex")
    public long getShapeIndex() {
        return shapeIndex;
    }

    @JsonProperty("shapeIndex")
    public void setShapeIndex(long shapeIndex) {
        this.shapeIndex = shapeIndex;
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
        return new ToStringBuilder(this).append("linkId", linkId).append("mappedPosition", mappedPosition).append("originalPosition", originalPosition).append("type", type).append("spot", spot).append("sideOfStreet", sideOfStreet).append("mappedRoadName", mappedRoadName).append("label", label).append("shapeIndex", shapeIndex).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(originalPosition).append(linkId).append(mappedPosition).append(sideOfStreet).append(shapeIndex).append(additionalProperties).append(mappedRoadName).append(label).append(type).append(spot).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Waypoint) == false) {
            return false;
        }
        Waypoint rhs = ((Waypoint) other);
        return new EqualsBuilder().append(originalPosition, rhs.originalPosition).append(linkId, rhs.linkId).append(mappedPosition, rhs.mappedPosition).append(sideOfStreet, rhs.sideOfStreet).append(shapeIndex, rhs.shapeIndex).append(additionalProperties, rhs.additionalProperties).append(mappedRoadName, rhs.mappedRoadName).append(label, rhs.label).append(type, rhs.type).append(spot, rhs.spot).isEquals();
    }

}
