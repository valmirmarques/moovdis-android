package br.com.cardapia.company.acessmap.services;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.util.LatLongUtils;
import br.com.cardapia.company.acessmap.util.MoovDisContants;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class OSMService implements Serializable {

    private final static int API_AVOID_AREAS_LIMIT = 50;

    /**
     * call service from OSM
     * @param origin
     * @param dest
     * @param osmKey
     * @param markersList
     * @param isWeelchair
     * @return
     */
    public static String[] getApiUrl(final LatLng origin, final LatLng dest, final String osmKey, final List<MarkerBean> markersList, final boolean isWeelchair) {


        StringBuilder avoidArea = new StringBuilder();
        StringBuilder avoidQuery = new StringBuilder();

        if (markersList != null && !markersList.isEmpty()) {
            final int limit = markersList.size() > API_AVOID_AREAS_LIMIT ? API_AVOID_AREAS_LIMIT : markersList.size();
            for (int i = 0; i < limit; i++) {
                MarkerBean bean = markersList.get(i);
                if (bean.getMarkerType().equals(MoovDisContants.MARKER_GOOD)) {
                    continue;
                }

                LatLng[] polygon = LatLongUtils.polygonByPoint(new LatLng(bean.getLocation().getCoordinates()[1], bean.getLocation().getCoordinates()[0]));

                avoidQuery.append(String.format("[[[%s,%s],[%s,%s],[%s,%s],[%s,%s],[%s,%s]]],",
                        polygon[0].longitude, polygon[0].latitude,
                        polygon[1].longitude, polygon[1].latitude,
                        polygon[2].longitude, polygon[2].latitude,
                        polygon[3].longitude, polygon[3].latitude,
                        polygon[4].longitude, polygon[4].latitude
                ));
            }
        }
        String avoidQueryStr = avoidQuery.toString();
        if (StringUtils.isNotBlank(avoidQueryStr)) {
            avoidArea.append(avoidQueryStr.substring(0, avoidQueryStr.length() -1));
        }

        String language = Locale.getDefault().getLanguage();
        StringBuilder query = new StringBuilder("https://api.openrouteservice.org/directions?");
        query.append("api_key=").append(osmKey);
        query.append("&coordinates=").append(origin.longitude).append(",").append(origin.latitude).append("|").append(dest.longitude).append(",").append(dest.latitude);
        query.append("&profile=").append((isWeelchair ? "wheelchair" : "foot-walking"));
        query.append("&preference=").append("recommended");
        query.append("&format=").append("json");
        query.append("&units=").append("m");
        query.append("&language=").append(language);
        query.append("&geometry=").append("true");
        query.append("&geometry_format=").append("geojson");
        query.append("&instructions=").append("true");
        query.append("&instructions_format=").append("html");
        query.append("&roundabout_exits=").append("true");
        query.append("&attributes=").append("detourfactor|percentage");
        query.append("&maneuvers=").append("true");
        query.append("&elevation=").append("true");
        query.append("&extra_info=").append("traildifficulty|waytype");

        StringBuilder options = new StringBuilder();
        if (isWeelchair) {
            options.append("{")
                    .append("\"avoid_features\": \"hills|ferries|steps\",")
                    .append("\"profile_params\": {")
                    .append("\"restrictions\": {")
                    .append("\"surface_type\": \"cobblestone:flattened\",")
                    .append("\"track_type\": \"grade1\",")
                    .append("\"smoothness_type\": \"good\",")
                    .append("\"maximum_sloped_curb\": 0.06,")
                    .append("\"maximum_incline\": 6").append("}");
            if (StringUtils.isNotBlank(avoidQueryStr)) {
                options.append("},");
                options.append("\"avoid_polygons\": {  ")
                        .append("\"type\": \"MultiPolygon\",")
                        .append("\"coordinates\": [")
                        .append(avoidArea.toString())
                        .append("]}");
            } else {
                options.append("}");
            }
            options.append("}");
        } else {
            options.append("{")
                    .append("\"avoid_features\": \"fords|ferries\",")
                    .append("\"profile_params\": {")
                    .append("\"weightings\": {")
                    .append("\"green\": {")
                    .append("\"factor\": 0.8")
                    .append("},")
                    .append("\"quiet\": {")
                    .append("\"factor\": 1.0")
                    .append("}")
                    .append("}");
            if (StringUtils.isNotBlank(avoidQueryStr)) {
                options.append("},");
                options.append("\"avoid_polygons\": {  ")
                        .append("\"type\": \"MultiPolygon\",")
                        .append("\"coordinates\": [")
                        .append(avoidArea.toString())
                        .append("]}");
            } else {
                options.append("}");
            }
            options.append("}");
        }

        return new String[]{query.toString(), options.toString()};
    }

}
