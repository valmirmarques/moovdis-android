package br.com.cardapia.company.acessmap.services;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class MapBoxApiService implements Serializable {


    /**
     * Obtem a URL da API
     * @param origin
     * @param dest
     * @return
     */
    public static String getApiUrl(final LatLng origin, final LatLng dest, final String mapBoxApiKey) {
        String language = Locale.getDefault().getLanguage();

        // Origin and destination of route
        String stringRequest = String.format("%s,%s;%s,%s", origin.longitude, origin.latitude, dest.longitude, dest.latitude);
        // Sensor enabled
        String others = "steps=true&alternatives=true&language=" + language;

        // Building the url to the web service
        String url = "https://api.mapbox.com/directions/v5/mapbox/walking/" + stringRequest + "?access_token=" + mapBoxApiKey + "&" + others;
        return url;
    }

}
