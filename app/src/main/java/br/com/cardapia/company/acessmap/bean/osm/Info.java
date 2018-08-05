
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
    "attribution",
    "osm_file_md5_hash",
    "engine",
    "service",
    "timestamp",
    "query"
})
public class Info implements Serializable
{

    @JsonProperty("attribution")
    private String attribution;
    @JsonProperty("osm_file_md5_hash")
    private String osmFileMd5Hash;
    @JsonProperty("engine")
    private Engine engine;
    @JsonProperty("service")
    private String service;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("query")
    private Query query;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8790595357731879522L;

    @JsonProperty("attribution")
    public String getAttribution() {
        return attribution;
    }

    @JsonProperty("attribution")
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    @JsonProperty("osm_file_md5_hash")
    public String getOsmFileMd5Hash() {
        return osmFileMd5Hash;
    }

    @JsonProperty("osm_file_md5_hash")
    public void setOsmFileMd5Hash(String osmFileMd5Hash) {
        this.osmFileMd5Hash = osmFileMd5Hash;
    }

    @JsonProperty("engine")
    public Engine getEngine() {
        return engine;
    }

    @JsonProperty("engine")
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(Query query) {
        this.query = query;
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
        return new ToStringBuilder(this).append("attribution", attribution).append("osmFileMd5Hash", osmFileMd5Hash).append("engine", engine).append("service", service).append("timestamp", timestamp).append("query", query).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timestamp).append(engine).append(additionalProperties).append(query).append(service).append(osmFileMd5Hash).append(attribution).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Info) == false) {
            return false;
        }
        Info rhs = ((Info) other);
        return new EqualsBuilder().append(timestamp, rhs.timestamp).append(engine, rhs.engine).append(additionalProperties, rhs.additionalProperties).append(query, rhs.query).append(service, rhs.service).append(osmFileMd5Hash, rhs.osmFileMd5Hash).append(attribution, rhs.attribution).isEquals();
    }

}
