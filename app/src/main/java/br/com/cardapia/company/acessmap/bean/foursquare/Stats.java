
package br.com.cardapia.company.acessmap.bean.foursquare;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tipCount",
    "usersCount",
    "checkinsCount",
    "visitsCount"
})
public class Stats implements Serializable
{

    @JsonProperty("tipCount")
    private int tipCount;
    @JsonProperty("usersCount")
    private int usersCount;
    @JsonProperty("checkinsCount")
    private int checkinsCount;
    @JsonProperty("visitsCount")
    private int visitsCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6431461103581045816L;

    @JsonProperty("tipCount")
    public int getTipCount() {
        return tipCount;
    }

    @JsonProperty("tipCount")
    public void setTipCount(int tipCount) {
        this.tipCount = tipCount;
    }

    @JsonProperty("usersCount")
    public int getUsersCount() {
        return usersCount;
    }

    @JsonProperty("usersCount")
    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    @JsonProperty("checkinsCount")
    public int getCheckinsCount() {
        return checkinsCount;
    }

    @JsonProperty("checkinsCount")
    public void setCheckinsCount(int checkinsCount) {
        this.checkinsCount = checkinsCount;
    }

    @JsonProperty("visitsCount")
    public int getVisitsCount() {
        return visitsCount;
    }

    @JsonProperty("visitsCount")
    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
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
