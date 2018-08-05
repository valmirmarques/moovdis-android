
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
    "address",
    "lat",
    "lng",
    "labeledLatLngs",
    "distance",
    "cc",
    "city",
    "state",
    "country",
    "formattedAddress",
    "postalCode",
    "neighborhood",
    "crossStreet"
})
public class Location implements Serializable
{

    @JsonProperty("address")
    private String address;
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lng")
    private double lng;
    @JsonProperty("labeledLatLngs")
    private List<LabeledLatLng> labeledLatLngs = null;
    @JsonProperty("distance")
    private int distance;
    @JsonProperty("cc")
    private String cc;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;
    @JsonProperty("formattedAddress")
    private List<String> formattedAddress = null;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("neighborhood")
    private String neighborhood;
    @JsonProperty("crossStreet")
    private String crossStreet;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7009556616590494750L;

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("lat")
    public double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(double lat) {
        this.lat = lat;
    }

    @JsonProperty("lng")
    public double getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(double lng) {
        this.lng = lng;
    }

    @JsonProperty("labeledLatLngs")
    public List<LabeledLatLng> getLabeledLatLngs() {
        return labeledLatLngs;
    }

    @JsonProperty("labeledLatLngs")
    public void setLabeledLatLngs(List<LabeledLatLng> labeledLatLngs) {
        this.labeledLatLngs = labeledLatLngs;
    }

    @JsonProperty("distance")
    public int getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @JsonProperty("cc")
    public String getCc() {
        return cc;
    }

    @JsonProperty("cc")
    public void setCc(String cc) {
        this.cc = cc;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("formattedAddress")
    public List<String> getFormattedAddress() {
        return formattedAddress;
    }

    @JsonProperty("formattedAddress")
    public void setFormattedAddress(List<String> formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("neighborhood")
    public String getNeighborhood() {
        return neighborhood;
    }

    @JsonProperty("neighborhood")
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    @JsonProperty("crossStreet")
    public String getCrossStreet() {
        return crossStreet;
    }

    @JsonProperty("crossStreet")
    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
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
