package br.com.cardapia.company.acessmap.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by tvtios-01 on 06/12/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class UsuarioBean implements Serializable {

    private String _id;
    private boolean blind;
    private boolean weelchair;
    private boolean motorized;
    private boolean othersDisabilities;
    private int weelchairWidht;
    private String othersDisabilitiesName;
    private String name;
    private String email;
    private Date birthDate;
    private byte[] image;
    private Timestamp dateCreated;
    private String pushId;
    private String lang;

    public UsuarioBean() {
    }

    public boolean isBlind() {
        return blind;
    }

    public void setBlind(boolean blind) {
        this.blind = blind;
    }

    public boolean isWeelchair() {
        return weelchair;
    }

    public void setWeelchair(boolean weelchair) {
        this.weelchair = weelchair;
    }

    public int getWeelchairWidht() {
        return weelchairWidht;
    }

    public void setWeelchairWidht(int weelchairWidht) {
        this.weelchairWidht = weelchairWidht;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isMotorized() {
        return motorized;
    }

    public void setMotorized(boolean motorized) {
        this.motorized = motorized;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "UsuarioBean{" +
                "_id='" + _id + '\'' +
                ", blind=" + blind +
                ", weelchair=" + weelchair +
                ", motorized=" + motorized +
                ", weelchairWidht=" + weelchairWidht +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", image=" + Arrays.toString(image) +
                ", dateCreated=" + dateCreated +
                '}';
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isOthersDisabilities() {
        return othersDisabilities;
    }

    public void setOthersDisabilities(boolean othersDisabilities) {
        this.othersDisabilities = othersDisabilities;
    }

    public String getOthersDisabilitiesName() {
        if (othersDisabilitiesName == null) {
            return StringUtils.EMPTY;
        }
        return othersDisabilitiesName;
    }

    public void setOthersDisabilitiesName(String othersDisabilitiesName) {
        this.othersDisabilitiesName = othersDisabilitiesName;
    }
}
