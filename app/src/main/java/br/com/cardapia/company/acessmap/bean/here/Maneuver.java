
package br.com.cardapia.company.acessmap.bean.here;

import java.util.ArrayList;
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
    "position",
    "instruction",
    "travelTime",
    "length",
    "shape",
    "id",
    "_type"
})
public class Maneuver {

    @JsonProperty("position")
    private Position position;
    @JsonProperty("instruction")
    private String instruction;
    @JsonProperty("travelTime")
    private long travelTime;
    @JsonProperty("length")
    private long length;
    @JsonProperty("shape")
    private List<String> shape;
    @JsonProperty("id")
    private String id;
    @JsonProperty("_type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonProperty("instruction")
    public String getInstruction() {
        return instruction;
    }

    @JsonProperty("instruction")
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @JsonProperty("travelTime")
    public long getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("length")
    public long getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(long length) {
        this.length = length;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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
        return new ToStringBuilder(this).append("position", position).append("instruction", instruction).append("travelTime", travelTime).append("length", length).append("id", id).append("type", type).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(position).append(travelTime).append(additionalProperties).append(length).append(instruction).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Maneuver) == false) {
            return false;
        }
        Maneuver rhs = ((Maneuver) other);
        return new EqualsBuilder().append(id, rhs.id).append(position, rhs.position).append(travelTime, rhs.travelTime).append(additionalProperties, rhs.additionalProperties).append(length, rhs.length).append(instruction, rhs.instruction).append(type, rhs.type).isEquals();
    }

    @JsonProperty("shape")
    public List<String> getShape() {
        if (shape == null)  {
            shape = new ArrayList<>();
        }
        return shape;
    }

    @JsonProperty("shape")
    public void setShape(List<String> shape) {
        this.shape = shape;
    }
}
