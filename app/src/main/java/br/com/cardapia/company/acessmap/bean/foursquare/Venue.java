
package br.com.cardapia.company.acessmap.bean.foursquare;

import android.support.annotation.NonNull;

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

import br.com.cardapia.company.acessmap.bean.VenueInfoBean;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "contact",
    "location",
    "categories",
    "verified",
    "stats",
    "beenHere",
    "hereNow",
    "referralId",
    "venueChains",
    "hasPerk",
    "venuePage"
})
public class Venue implements Serializable, Comparable<Venue>
{

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("contact")
    private Contact contact;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("categories")
    private List<Category> categories = null;
    @JsonProperty("verified")
    private boolean verified;
    @JsonProperty("stats")
    private Stats stats;
    @JsonProperty("beenHere")
    private BeenHere beenHere;
    @JsonProperty("hereNow")
    private HereNow hereNow;
    @JsonProperty("referralId")
    private String referralId;
    @JsonProperty("venueChains")
    private List<Object> venueChains = null;
    @JsonProperty("hasPerk")
    private boolean hasPerk;
    @JsonProperty("venuePage")
    private VenuePage venuePage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    private VenueInfoBean venueInfo;
    private final static long serialVersionUID = 3044429426674018909L;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("contact")
    public Contact getContact() {
        return contact;
    }

    @JsonProperty("contact")
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @JsonProperty("verified")
    public boolean isVerified() {
        return verified;
    }

    @JsonProperty("verified")
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @JsonProperty("stats")
    public Stats getStats() {
        return stats;
    }

    @JsonProperty("stats")
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @JsonProperty("beenHere")
    public BeenHere getBeenHere() {
        return beenHere;
    }

    @JsonProperty("beenHere")
    public void setBeenHere(BeenHere beenHere) {
        this.beenHere = beenHere;
    }

    @JsonProperty("hereNow")
    public HereNow getHereNow() {
        return hereNow;
    }

    @JsonProperty("hereNow")
    public void setHereNow(HereNow hereNow) {
        this.hereNow = hereNow;
    }

    @JsonProperty("referralId")
    public String getReferralId() {
        return referralId;
    }

    @JsonProperty("referralId")
    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    @JsonProperty("venueChains")
    public List<Object> getVenueChains() {
        return venueChains;
    }

    @JsonProperty("venueChains")
    public void setVenueChains(List<Object> venueChains) {
        this.venueChains = venueChains;
    }

    @JsonProperty("hasPerk")
    public boolean isHasPerk() {
        return hasPerk;
    }

    @JsonProperty("hasPerk")
    public void setHasPerk(boolean hasPerk) {
        this.hasPerk = hasPerk;
    }

    @JsonProperty("venuePage")
    public VenuePage getVenuePage() {
        return venuePage;
    }

    @JsonProperty("venuePage")
    public void setVenuePage(VenuePage venuePage) {
        this.venuePage = venuePage;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public VenueInfoBean getVenueInfo() {
        return venueInfo;
    }

    public void setVenueInfo(VenueInfoBean venueInfo) {
        this.venueInfo = venueInfo;
    }


    @Override
    public int compareTo(@NonNull Venue venue) {
        if (this.getLocation() != null && venue.getLocation() != null) {
            return this.getLocation().getDistance() - venue.getLocation().getDistance();
        }
        return 0;
    }
}
