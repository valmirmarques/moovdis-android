package br.com.cardapia.company.acessmap.services;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class MoovdisServices implements Serializable {


    //protected static final String SERVER = "http://192.168.100.3:1880";
    protected static final String SERVER = "https://dev.tvtbots.digital/api/moovdis-api";

    /**
     * Obtem a URL da API
     *
     * @return
     */
    public static String getApiAddUserUrl() {
        return SERVER + "/user/add";
    }

    /**
     * Obtem a URL da API
     *
     * @return
     */
    public static String getApiUserFrmUpdateUrl() {
        return SERVER + "/user/fcmupdate";
    }

    /**
     * @return
     */
    public static String getApiGetUserUrl(Map<String, String> params) {

        String paramsStr = StringUtils.EMPTY;
        if (params != null && !params.isEmpty()) {
            paramsStr = "?";
            Set<String> keys = params.keySet();
            for (String key : keys) {
                paramsStr += key + "=" + params.get(key) + "?";
            }
            paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
        }

        return SERVER + "/user/get" + paramsStr;
    }

    /**
     * URL das Marcacoes
     *
     * @param lat
     * @param longt
     * @return
     */
    public static String getApiGetMarkersUrl(final String lat, final String longt) {
        return SERVER + "/marker/get?lat=" + lat + "&long=" + longt;
    }

    /**
     * URL das Ultimas Marcacoes
     *
     * @return
     */
    public static String getApiGetMarkersLastUrl() {
        return SERVER + "/marker/get_last";
    }

    /**
     * URL das Marcacoes
     *
     * @param id
     * @return
     */
    public static String getApiGetMarkerByIdUrl(final String id) {
        return SERVER + "/marker/get/" + id;
    }

    /**
     * @return
     */
    public static String getApiGetRankingWeekUrl() {
        return SERVER + "/ranking/week";
    }

    /**
     * @return
     */
    public static String getApiGetRankingGeneralUrl() {
        return SERVER + "/ranking/general";
    }

    /**
     * @return
     */
    public static String getApiGetRankingAddUrl() {
        return SERVER + "/ranking/add";
    }

    /**
     * @param user
     * @return
     */
    public static String getApiGetRankingUserUrl(final String user) {
        return SERVER + "/ranking/user/" + user;
    }

    /**
     * @param user
     * @return
     */
    public static String getApiGetRankingUserPhotosUrl(final String user) {
        return SERVER + "/ranking/user/" + user + "/photos";
    }

    /**
     * @return
     */
    public static String getApiPostMarkerAddUrl() {
        return SERVER + "/marker/add";
    }

    /**
     * @return
     */
    public static String getApiRouteCommentAddUrl() {
        return SERVER + "/route/comment";
    }

    /**
     * @return
     */
    public static String getApiRouteCreateAddUrl() {
        return SERVER + "/route/create";
    }

    /**
     * @param user
     * @return
     */
    public static String getApiRouteGetUrl(final String user) {
        return SERVER + "/route/user/" + user;
    }

    /**
     * @return
     */
    public static String getApiPostMarkerUpdateUrl() {
        return SERVER + "/marker/update";
    }

    /**
     * @return
     */
    public static String getApiPostMarkerLikeUrl() {
        return SERVER + "/marker/like";
    }

    /**
     * @return
     */
    public static String getApiDeleteMarkerUrl(final String id, final String deleteRanking) {
        return SERVER + "/marker/delete/" + id + "/" + deleteRanking;
    }


    /**
     * @return
     */
    public static String getApiWatsonUrl() {
        return SERVER + "/chat/sendMessage";
    }

    /**
     * @return
     */
    public static String getPushClickUrl() {
        return SERVER + "/notification/click";
    }

    /**
     * @param idRota
     * @return
     */
    public static String getApiRouteSharingUrl(final String idRota) {
        // Origin of route
        String url = SERVER + "/route/share_route_moovdis/" + idRota;
        return url;
    }

    /**
     * @return
     */
    public static String getApiPostSidewalkAddUrl() {
        return SERVER + "/sidewalk/add";
    }

    /**
     * @return
     */
    public static String getApiGetEventsUrl() {
        return SERVER + "/events/get";
    }

    /**
     * Venue Rate
     *
     * @return
     */
    public static String getApiRateVenueUrl() {
        return SERVER + "/venue/rate";
    }

    /**
     * Venue Search
     *
     * @return
     */
    public static String getApiRateSearchUrl() {
        return SERVER + "/venue/search";
    }

    /**
     * Venue Rate
     *
     * @return
     */
    public static String getApiRateByVenueIdUrl(final String id) {
        return SERVER + "/venue/rate/" + id;
    }

    /**
     * URL das Marcacoes
     *
     * @param lat
     * @param longt
     * @return
     */
    public static String getApiGetSidewalkUrl(final String lat, final String longt) {
        return SERVER + "/sidewalk/get?lat=" + lat + "&long=" + longt;
    }



}
