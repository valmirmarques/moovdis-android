package br.com.cardapia.company.acessmap.services;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class GoogleDirectionsApiService implements Serializable {


    /**
     * Obtem a URL da API
     * @param origin
     * @param dest
     * @return
     */
    public static String getApiUrl(final LatLng origin, final LatLng dest, final String googleMapsKey) {
        String language = Locale.getDefault().getLanguage();

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=true";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&mode=walking&alternatives=true&language=" + language;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?key=" + googleMapsKey + "&" + parameters;
        return url;
    }


    /**
     * Obtem a URL da API
     * @param origin
     * @param dest
     * @return
     */
    public static String getApiElevationUrl(final LatLng origin, final LatLng dest, final String googleMapsKey, final String apiUmbrellaKey) {
        // Origin of route
        String language = Locale.getDefault().getLanguage();

        String path = "path=" + origin.latitude + "," + origin.longitude + "|" + dest.latitude + "," + dest.longitude;
        String samples = "samples=20";
        String url = MoovdisServices.SERVER + "/google/elavation" + "?key=" + googleMapsKey + "&" + path + "&" + samples + "&language=" + language +  "&api_key=" + apiUmbrellaKey ;
        return url;
    }


}
