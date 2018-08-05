package br.com.cardapia.company.acessmap.services;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.util.LatLongUtils;
import br.com.cardapia.company.acessmap.util.MoovDisContants;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class HereApiService implements Serializable {

    private final static int API_AVOID_AREAS_LIMIT = 20;

    /**
     * Obtem a URL da API
     * @param origin
     * @param dest
     * @return
     */
    public static String[] getApiUrl(final LatLng origin, final LatLng dest, final String hereApp, final String hereKey, final List<MarkerBean> markersList) {

        String language = Locale.getDefault().getLanguage();
        // Origin and destination of route
        String stringRequest = String.format("?waypoint0=geo!%s,%s&waypoint1=geo!%s,%s", origin.latitude, origin.longitude, dest.latitude, dest.longitude);
        // Sensor enabled
        String others = "mode=fastest;bicycle;traffic:disabled&language=" + language + "&alternatives=2&maneuverattributes=di,sh&departure=now";
        // Building the url to the web service

        String avoidArea = StringUtils.EMPTY;
        int count = 0;
        if (markersList != null && !markersList.isEmpty()) {
            for (MarkerBean bean : markersList) {
                if (bean.getMarkerType().equals(MoovDisContants.MARKER_GOOD)) {
                    continue;
                }

                LatLng[] coords = LatLongUtils.bboxByPoint(new LatLng(bean.getLocation().getCoordinates()[1], bean.getLocation().getCoordinates()[0]));

                avoidArea += String.format("%s,%s;%s,%s!",
                        coords[0].latitude,
                        coords[0].longitude,
                        coords[1].latitude,
                        coords[1].longitude);

                count ++;

                if (count >= API_AVOID_AREAS_LIMIT) {
                    break;
                }
            }
        }


        if (StringUtils.isNotBlank(avoidArea)) {
            avoidArea = avoidArea.substring(0, avoidArea.length() - 1 );
            //avoidArea = "&avoidAreas=-25.488849,-49.312515;-25.489193,-49.311292";
        }

        String url = "https://route.cit.api.here.com/routing/7.2/calculateroute.json" + stringRequest + "&app_id=" + hereApp + "&app_code=" + hereKey + "&" + others;
        return new String[] { url, avoidArea};
    }

}
