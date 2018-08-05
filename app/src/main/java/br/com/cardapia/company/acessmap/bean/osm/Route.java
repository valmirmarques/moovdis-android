
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
    "elevation",
    "summary",
    "geometry_format",
    "geometry",
    "segments",
    "way_points",
    "extras",
    "bbox"
})
public class Route implements Serializable
{

    @JsonProperty("elevation")
    private boolean elevation;
    @JsonProperty("summary")
    private Summary summary;
    @JsonProperty("geometry_format")
    private String geometryFormat;
    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("segments")
    private List<Segment> segments = null;
    @JsonProperty("way_points")
    private List<Integer> wayPoints = null;
    @JsonProperty("extras")
    private Extras extras;
    @JsonProperty("bbox")
    private List<Double> bbox = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -929136592102833620L;

    @JsonProperty("elevation")
    public boolean isElevation() {
        return elevation;
    }

    @JsonProperty("elevation")
    public void setElevation(boolean elevation) {
        this.elevation = elevation;
    }

    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @JsonProperty("geometry_format")
    public String getGeometryFormat() {
        return geometryFormat;
    }

    @JsonProperty("geometry_format")
    public void setGeometryFormat(String geometryFormat) {
        this.geometryFormat = geometryFormat;
    }

    @JsonProperty("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonProperty("segments")
    public List<Segment> getSegments() {
        return segments;
    }

    @JsonProperty("segments")
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @JsonProperty("way_points")
    public List<Integer> getWayPoints() {
        return wayPoints;
    }

    @JsonProperty("way_points")
    public void setWayPoints(List<Integer> wayPoints) {
        this.wayPoints = wayPoints;
    }

    @JsonProperty("extras")
    public Extras getExtras() {
        return extras;
    }

    @JsonProperty("extras")
    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    @JsonProperty("bbox")
    public List<Double> getBbox() {
        return bbox;
    }

    @JsonProperty("bbox")
    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
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
        return new ToStringBuilder(this).append("elevation", elevation).append("summary", summary).append("geometryFormat", geometryFormat).append("geometry", geometry).append("segments", segments).append("wayPoints", wayPoints).append("extras", extras).append("bbox", bbox).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summary).append(wayPoints).append(segments).append(additionalProperties).append(bbox).append(elevation).append(extras).append(geometry).append(geometryFormat).toHashCode();
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
        return new EqualsBuilder().append(summary, rhs.summary).append(wayPoints, rhs.wayPoints).append(segments, rhs.segments).append(additionalProperties, rhs.additionalProperties).append(bbox, rhs.bbox).append(elevation, rhs.elevation).append(extras, rhs.extras).append(geometry, rhs.geometry).append(geometryFormat, rhs.geometryFormat).isEquals();
    }

}
