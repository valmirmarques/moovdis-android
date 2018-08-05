package br.com.cardapia.company.acessmap.parser;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.cardapia.company.acessmap.bean.DescricaoFotoBean;
import br.com.cardapia.company.acessmap.bean.FacesBean;

/**
 * Created by tvtios-01 on 23/10/17.
 */
public class MicrooftJsonParser {

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public DescricaoFotoBean parse(JSONObject jObject){

        DescricaoFotoBean descricaoFoto = new DescricaoFotoBean();
        JSONArray jCategories;
        JSONArray jTags;
        JSONArray jCaption;
        JSONObject jAdult;
        JSONArray jFaces;

        try {

            try {
                jCategories = jObject.getJSONArray("categories");

                /** Traversing all Categories */
                for(int i = 0; i < jCategories.length(); i++) {
                    String nomeCategoria = (((JSONObject) jCategories.get(i)).getString("name"));
                    Double scoreCategoria = (((JSONObject) jCategories.get(i)).getDouble("score"));
                    descricaoFoto.getCategories().put(nomeCategoria, scoreCategoria);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jAdult = jObject.getJSONObject("adult");
                descricaoFoto.getAdult().setAdultContent(jAdult.getBoolean("isAdultContent"));
                descricaoFoto.getAdult().setAdultScore(jAdult.getDouble("adultScore"));
                descricaoFoto.getAdult().setRacyContent(jAdult.getBoolean("isRacyContent"));
                descricaoFoto.getAdult().setRacyScore(jAdult.getDouble("racyScore"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jTags = jObject.getJSONObject("description").getJSONArray("tags");
                /** Traversing all Tags */
                for(int i = 0; i < jTags.length(); i++) {
                    String nomeTag = (((String) jTags.get(i)));
                    Double scoreCaption = 1d;
                    descricaoFoto.getTags().put(nomeTag, scoreCaption);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jFaces = jObject.getJSONArray("faces");

                /** Traversing all Categories */
                for(int i = 0; i < jFaces.length(); i++) {
                    int age = (((JSONObject) jFaces.get(i)).getInt("age"));
                    String gender = (((JSONObject) jFaces.get(i)).getString("gender"));
                    FacesBean faceBean = new FacesBean();
                    faceBean.setAge(age);
                    faceBean.setGender(gender);
                    descricaoFoto.getFaces().add(faceBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jCaption = jObject.getJSONObject("description").getJSONArray("captions");
            /** Traversing all Captions */
            for(int i = 0; i < jCaption.length(); i++) {
                String nomeCaption = (((JSONObject) jCaption.get(i)).getString("text"));
                Double scoreCaption = (((JSONObject) jCaption.get(i)).getDouble("confidence"));
                descricaoFoto.getCaptions().put(nomeCaption, scoreCaption);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
        }


        return descricaoFoto;
    }


}


