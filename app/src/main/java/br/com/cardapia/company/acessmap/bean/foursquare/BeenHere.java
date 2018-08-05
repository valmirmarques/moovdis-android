
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
    "count",
    "lastCheckinExpiredAt",
    "marked",
    "unconfirmedCount"
})
public class BeenHere implements Serializable
{

    @JsonProperty("count")
    private int count;
    @JsonProperty("lastCheckinExpiredAt")
    private int lastCheckinExpiredAt;
    @JsonProperty("marked")
    private boolean marked;
    @JsonProperty("unconfirmedCount")
    private int unconfirmedCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2220296229181685640L;

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }

    @JsonProperty("lastCheckinExpiredAt")
    public int getLastCheckinExpiredAt() {
        return lastCheckinExpiredAt;
    }

    @JsonProperty("lastCheckinExpiredAt")
    public void setLastCheckinExpiredAt(int lastCheckinExpiredAt) {
        this.lastCheckinExpiredAt = lastCheckinExpiredAt;
    }

    @JsonProperty("marked")
    public boolean isMarked() {
        return marked;
    }

    @JsonProperty("marked")
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @JsonProperty("unconfirmedCount")
    public int getUnconfirmedCount() {
        return unconfirmedCount;
    }

    @JsonProperty("unconfirmedCount")
    public void setUnconfirmedCount(int unconfirmedCount) {
        this.unconfirmedCount = unconfirmedCount;
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
