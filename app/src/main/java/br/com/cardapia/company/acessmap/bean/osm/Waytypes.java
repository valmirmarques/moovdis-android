
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
    "values",
    "summary"
})
public class Waytypes implements Serializable
{

    @JsonProperty("values")
    private List<List<Integer>> values = null;
    @JsonProperty("summary")
    private List<Summary_> summary = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7147046853028505522L;

    @JsonProperty("values")
    public List<List<Integer>> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<List<Integer>> values) {
        this.values = values;
    }

    @JsonProperty("summary")
    public List<Summary_> getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(List<Summary_> summary) {
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
        return new ToStringBuilder(this).append("values", values).append("summary", summary).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summary).append(values).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Waytypes) == false) {
            return false;
        }
        Waytypes rhs = ((Waytypes) other);
        return new EqualsBuilder().append(summary, rhs.summary).append(values, rhs.values).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
