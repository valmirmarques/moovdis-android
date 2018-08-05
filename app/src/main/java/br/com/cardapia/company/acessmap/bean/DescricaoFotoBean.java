package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tvtios-01 on 12/11/17.
 */

public class DescricaoFotoBean implements Serializable {

    @Override
    public String toString() {
        return "DescricaoFotoBean{" +
                "categories=" + categories +
                ", adult=" + adult +
                ", tags=" + tags +
                ", captions=" + captions +
                ", faces=" + faces +
                '}';
    }

    private Map<String, Double> categories = new HashMap<>();
    private AdultBean adult = new AdultBean();
    private Map<String, Double> tags = new HashMap<>();
    private Map<String, Double> captions = new HashMap<>();

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    private List<FacesBean> faces = new ArrayList<>();

    public Map<String, Double> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Double> categories) {
        this.categories = categories;
    }

    public AdultBean getAdult() {
        return adult;
    }

    public void setAdult(AdultBean adult) {
        this.adult = adult;
    }

    public Map<String, Double> getTags() {
        return tags;
    }

    public void setTags(Map<String, Double> tags) {
        this.tags = tags;
    }

    public Map<String, Double> getCaptions() {
        return captions;
    }

    public void setCaptions(Map<String, Double> captions) {
        this.captions = captions;
    }


}
