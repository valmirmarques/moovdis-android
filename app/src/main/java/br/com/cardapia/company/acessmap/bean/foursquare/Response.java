
package br.com.cardapia.company.acessmap.bean.foursquare;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "venues",
    "confident"
})
public class Response implements Serializable
{

    @JsonProperty("venues")
    private List<Venue> venues = null;
    @JsonProperty("confident")
    private boolean confident;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5139211984875040602L;

    @JsonProperty("venues")
    public List<Venue> getVenues() {
        return venues;
    }

    @JsonProperty("venues")
    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    @JsonProperty("confident")
    public boolean isConfident() {
        return confident;
    }

    @JsonProperty("confident")
    public void setConfident(boolean confident) {
        this.confident = confident;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
