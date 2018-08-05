package br.com.cardapia.company.acessmap.util;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.cardapia.company.acessmap.bean.MarkerBean;

/**
 * Created by tvtios-01 on 11/02/18.
 */
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  This routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). It is being used to calculate     :*/
/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
/*::                                                                         :*/
/*::  Definitions:                                                           :*/
/*::    South latitudes are negative, east longitudes are positive           :*/
/*::                                                                         :*/
/*::  Passed to function:                                                    :*/
/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'M' is statute miles (default)                         :*/
/*::                  'K' is kilometers                                      :*/
/*::                  'N' is nautical miles                                  :*/
/*::                  'm' meters                                             :*/
/*::  Worldwide cities and other features databases with latitude longitude  :*/
/*::  are available at https://www.geodatasource.com                         :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@geodatasource.com                  :*/
/*::                                                                         :*/
/*::  Official Web site: https://www.geodatasource.com                       :*/
/*::                                                                         :*/
/*::           GeoDataSource.com (C) All Rights Reserved 2017                :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
public class LatLongUtils {

    public static final int PRECISION_FOUR = 4;

    public static final int DEFAULT_PRECISION = 10;


    public static final char METERS = 'm';
    public static final char KILOMETERS = 'K';
    public static final char MILES = 'M';
    public static final char NAUTICAL_MILES = 'N';

    /**
     * Calculate Distance between two points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param unit
     * @return
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == KILOMETERS) {
            dist = dist * 1.609344;
        } else if (unit == NAUTICAL_MILES) {
            dist = dist * 0.8684;
        } else if (unit == METERS) {
            dist = dist * 1.609344 * 1000;
        }
        return dist;
        //return SphericalUtil.computeDistanceBetween(new LatLng(lat1, lon1), new LatLng(lat2, lon2));
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians			:*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    @Contract(pure = true)
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees			:*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    @Contract(pure = true)
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static LatLng calcEndPoint(final LatLng center, final int distance, final double heading) {
        return SphericalUtil.computeOffset(center, distance, heading);
    }

    /**
     * Generate bbox by a Point
     *
     * @param center
     * @return
     */
    public static LatLng[] bboxByPoint(final LatLng center) {
        LatLng result = LatLongUtils.calcEndPoint(center, 15, 270);
        LatLng result2 = LatLongUtils.calcEndPoint(result, 15, 90);
        LatLng result3 = LatLongUtils.calcEndPoint(result2, 15, 180);
        return new LatLng[]{result, result3};
    }

    /**
     * Generate bbox by a Point
     *
     * @param center
     * @return
     */
    public static LatLng[] polygonByPoint(final LatLng center) {
        LatLng result = LatLongUtils.calcEndPoint(center, 1, 270);
        LatLng result2 = LatLongUtils.calcEndPoint(center, 1, 90);
        LatLng result3 = LatLongUtils.calcEndPoint(result2, 1, 0);
        LatLng result4 = LatLongUtils.calcEndPoint(result3, 2, 270);
        return new LatLng[]{result2, result, result4, result3, result2};
    }
    /**
     * checkIntersectPoint
     *
     * @param path
     * @param listIntersect
     * @return
     */
    public static int checkIntersectPoint(List<LatLng> path, List<MarkerBean> listIntersect) {
        int count = 0;

        if (listIntersect == null || listIntersect.isEmpty()) {
            return count;
        }

        for (MarkerBean marker : listIntersect) {
            if (marker.getMarkerType().equals(MoovDisContants.MARKER_GOOD)) {
                continue;
            }
            LatLng intersect = new LatLng(marker.getLocation().getCoordinates()[1], marker.getLocation().getCoordinates()[0]);
            boolean isLocationOnPath = PolyUtil.isLocationOnPath(intersect, path, true, 10D);
            boolean isLocationOnEdge = PolyUtil.isLocationOnEdge(intersect, path, true, 10D);
            if (isLocationOnPath || isLocationOnEdge) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param A
     * @param B
     * @param C
     * @return
     */
    public static boolean onLine(LatLng A, LatLng B, LatLng C) {
        double m1 = (C.latitude - A.latitude) / (C.longitude - A.longitude);
        double m2 = (C.latitude - B.latitude) / (C.longitude - B.longitude);
        return m1 == m2;
    }

    public static boolean isEquals(final Location A, final Location B, final int decimalPrecision) {

        BigDecimal lat1 = new BigDecimal(A.getLatitude());
        BigDecimal lng1 = new BigDecimal(B.getLongitude());

        BigDecimal lat2 = new BigDecimal(B.getLatitude());
        BigDecimal lng2 = new BigDecimal(B.getLongitude());

        lat1 = lat1.setScale(decimalPrecision, BigDecimal.ROUND_FLOOR);
        lng1 = lng1.setScale(decimalPrecision, BigDecimal.ROUND_FLOOR);
        lat2 = lat2.setScale(decimalPrecision, BigDecimal.ROUND_FLOOR);
        lng2 = lng2.setScale(decimalPrecision, BigDecimal.ROUND_FLOOR);

        return lat1.compareTo(lat2) == 0 && lng1.compareTo(lng2) == 0;

    }


    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    public static List<LatLng> decodePoly(final String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    /**
     * Decode a "Google-encoded" polyline
     *
     * @param encodedString
     * @param precision     1 for a 6 digits encoding of lat and lon, 10 for a 5 digits encoding.
     * @param hasAltitude   if the polyline also contains altitude (GraphHopper routes, with altitude in cm).
     * @return the polyline.
     */
    public static ArrayList<LatLng> decode(String encodedString, int precision, boolean hasAltitude) {
        int index = 0;
        int len = encodedString.length();
        int lat = 0, lng = 0, alt = 0;
        ArrayList<LatLng> polyline = new ArrayList<LatLng>(len / 3);
        //capacity estimate: polyline size is roughly 1/3 of string length for a 5digits encoding, 1/5 for 10digits.

        while (index < len) {
            int b, shift, result;
            shift = result = 0;
            do {
                b = encodedString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = result = 0;
            do {
                b = encodedString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            if (hasAltitude) {
                shift = result = 0;
                do {
                    b = encodedString.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dalt = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                alt += dalt;
            }

            LatLng p = new LatLng(lat * precision, lng * precision);
            polyline.add(p);
        }

        //Log.d("BONUSPACK", "decode:string="+len+" points="+polyline.size());

        return polyline;
    }


}