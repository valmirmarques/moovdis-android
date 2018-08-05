package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class RouteBean implements Serializable {


    private String _id;
    private String comment;
    private Date dateCreated;
    private LocationNameBean locationOrig;
    private LocationNameBean locationDest;
    private String distance;
    private UsuarioBean user;
    private boolean like;
    private float rating;
    private boolean feedbackPublic;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocationNameBean getLocationOrig() {
        return locationOrig;
    }

    public void setLocationOrig(LocationNameBean locationOrig) {
        this.locationOrig = locationOrig;
    }

    public LocationNameBean getLocationDest() {
        return locationDest;
    }

    public void setLocationDest(LocationNameBean locationDest) {
        this.locationDest = locationDest;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public UsuarioBean getUser() {
        return user;
    }

    public void setUser(UsuarioBean user) {
        this.user = user;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isFeedbackPublic() {
        return feedbackPublic;
    }

    public void setFeedbackPublic(boolean feedbackPublic) {
        this.feedbackPublic = feedbackPublic;
    }
}
