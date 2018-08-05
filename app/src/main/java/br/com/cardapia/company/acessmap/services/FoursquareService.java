package br.com.cardapia.company.acessmap.services;

import com.google.android.gms.maps.model.LatLng;

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
public class FoursquareService implements Serializable {


    /**
     *
     * @param ll
     * @param clientId
     * @param clientSecret
     * @return
     */
    public static String getSearchVenuesApiUrl(final LatLng ll, final String clientId, final String clientSecret, final String categoryId) {

        String language = Locale.getDefault().getLanguage();
        StringBuilder query = new StringBuilder("https://api.foursquare.com/v2/venues/search?");
        query.append("client_id=").append(clientId);
        query.append("&client_secret=").append(clientSecret);
        query.append("&v=20170724");
        query.append("&ll=").append(ll.latitude).append(",").append(ll.longitude);
        query.append("&radius=").append("3000");
        query.append("&limit=").append("50");
        query.append("&intent=").append("browser");
        if (StringUtils.isNotBlank(categoryId)) {
            query.append("&categoryId=").append(categoryId);
        }
        query.append("&lang=").append(language);

        return query.toString();
    }

    /**
     * s
     * @param id
     * @param clientId
     * @param clientSecret
     * @return
     */
    public static String getPhotoVenuesApiUrl(final String id, final String clientId, final String clientSecret) {

        StringBuilder query = new StringBuilder("https://api.foursquare.com/v2/venues/");
        query.append(id);
        query.append("/photos?");
        query.append("client_id=").append(clientId);
        query.append("&client_secret=").append(clientSecret);
        query.append("&v=20170724");
        query.append("&limit=1");

        return query.toString();
    }


}
