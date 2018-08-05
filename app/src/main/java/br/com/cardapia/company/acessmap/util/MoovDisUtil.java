package br.com.cardapia.company.acessmap.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.cardapia.company.acessmap.App;
import br.com.cardapia.company.acessmap.BuildConfig;
import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.bean.SidewalkBean;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by tvtios-01 on 06/12/17.
 */

public class MoovDisUtil {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static ProgressDialog progress;

    /**
     * Check if Internet is enabled
     *
     * @param context
     * @return
     */
    public static boolean isInternetConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Check is Location is Permited
     *
     * @param activity
     * @return
     */
    public static boolean checkLocationPermission(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Enable Location Services
     *
     * @return
     */
    public static void enableLocationService(final Context context) {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!(gps_enabled || network_enabled)) {
            Log.d("enableLocationService", "Habilitando Localizacao, estava desabilitado");
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }

    /**
     * Check if Google Play is Enabled
     *
     * @return
     */
    public static boolean checkPlayServices(final Activity activity) {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.toast_playservices_unrecoverable),
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Map to Bunble
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static Bundle mapStringToBundle(Map<String, String> data) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                bundle.putString(entry.getKey(), (String) entry.getValue());
            }
        }
        return bundle;
    }

    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }
        return containedUrls;
    }

    /**
     * Returns a list with all links contained in the input
     */
    public static String extractFirstUrl(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }
        if (containedUrls != null && !containedUrls.isEmpty()) {
            return containedUrls.get(0);
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

    /**
     * Verify if is Intenet Avaiable
     *
     * @param ctx
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Show Progress
     *
     * @param message
     */
    public static void showProgress(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        progress = ProgressDialog.show(context, context.getString(R.string.plase_wait), message, true);
                    }
                } else if (!(context instanceof App)) {
                    progress = ProgressDialog.show(context, context.getString(R.string.plase_wait), message, true);
                }
            }
        });
    }

    public static void hideProgress() {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (progress != null) {
                    progress.dismiss();
                    progress = null;
                }
            }
        });
    }

    /**
     * show message Error
     *
     * @param message
     */
    public static void showErrorMessage(final Context context, final String title, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    hideProgress();

                    new PromptDialog(context)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText(title)
                            .setContentText(message)
                            .setPositiveListener(context.getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * show message Error
     *
     * @param message
     */
    public static void showInfoMessage(final Context context, final String title, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    hideProgress();

                    new PromptDialog(context)
                            .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                            .setAnimationEnable(true)
                            .setTitleText(title)
                            .setContentText(message)
                            .setPositiveListener(context.getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * GET PHOTO FILE
     *
     * @param context
     * @return
     */
    public static Uri getPhotoFile(Context context) {
        File file = new File(context.getExternalCacheDir(), "camera.jpg");
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider.files", file);
    }


    /**
     * Make a line with principal Color
     *
     * @param sidewalkType
     * @param lstLatLngs
     * @return
     */
    public static PolylineOptions makeSidewalkLineColorPrincipal(final SidewalkBean.SidewalkTypeEnum sidewalkType, final List<LatLng> lstLatLngs) {

        int color = Integer.decode(sidewalkType.getSidewalkTypeColor()[0]);
        int[] rgb = new int[]{(color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF};

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.color(Color.rgb(rgb[0], rgb[1], rgb[2]));
        lineOptions.width(10);
        lineOptions.addAll(lstLatLngs);

        return lineOptions;

    }

    /**
     * Make a line with principal Color
     *
     * @param sidewalkType
     * @param lstLatLngs
     * @return
     */
    public static PolylineOptions makeSidewalkLineColorSecundary(final SidewalkBean.SidewalkTypeEnum sidewalkType, final List<LatLng> lstLatLngs) {

        int color2 = Integer.decode(sidewalkType.getSidewalkTypeColor()[1]);
        int[] rgb2 = new int[]{(color2 >> 16) & 0xFF, (color2 >> 8) & 0xFF, color2 & 0xFF};

        // Draw a dashed (60px spaced) blue polyline
        List<PatternItem> dashedPattern = Arrays.asList(new Dash(20), new Gap(20));
        PolylineOptions lineOptions2 = new PolylineOptions();
        lineOptions2.color(Color.rgb(rgb2[0], rgb2[1], rgb2[2]));
        lineOptions2.width(10);
        lineOptions2.pattern(dashedPattern);
        lineOptions2.addAll(lstLatLngs);

        return lineOptions2;

    }

    private static String getDatePattern(final SimpleDateFormat sdf) {
        String pattern = sdf.toPattern().replaceAll("y+", "yyyy");
        pattern = pattern.replaceAll("M+", "MM");
        pattern = pattern.replaceAll("d+", "dd");
        return pattern;
    }

    /**
     * Format Date.
     *
     * @param date
     * @return
     */
    public static String formatShortDate(final Date date) {
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        if (df instanceof SimpleDateFormat) {
            SimpleDateFormat sdf = (SimpleDateFormat) df;
            // To show Locale specific short date expression with full year
            String pattern = getDatePattern(sdf);
            sdf.applyPattern(pattern);
            return sdf.format(date);
        }
        return df.format(date);
    }

    /**
     * Format Date.
     *
     * @param date
     * @return
     */
    public static String formatLongDate(final Date date) {
        DateFormat df = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        if (df instanceof SimpleDateFormat) {
            SimpleDateFormat sdf = (SimpleDateFormat) df;
            // To show Locale specific short date expression with full year
            String pattern = getDatePattern(sdf);
            sdf.applyPattern(pattern);
            return sdf.format(date);
        }
        return df.format(date);
    }

    /**
     * Format Date.
     *
     * @param date
     * @return
     */
    public static Date parseShortDate(final String date) {
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        Date dateReturn = new Date();
        df.setLenient(false);
        if (df instanceof SimpleDateFormat) {
            SimpleDateFormat sdf = (SimpleDateFormat) df;
            // To show Locale specific short date expression with full year
            String pattern = getDatePattern(sdf);
            sdf.applyPattern(pattern);
            try {
                dateReturn = sdf.parse(date);
                return dateReturn;
            } catch (ParseException e) {
                Log.e("DateConvertion", e.getMessage(), e);
            }
        }
        try {
            dateReturn = df.parse(date);
        } catch (ParseException e) {
            Log.e("DateConvertion", e.getMessage(), e);
        }
        return dateReturn;
    }


    /**
     * Format Date.
     *
     * @param date
     * @return
     */
    public static boolean isDateValid(final String date) {
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        df.setLenient(false);
        if (df instanceof SimpleDateFormat) {
            SimpleDateFormat sdf = (SimpleDateFormat) df;
            // To show Locale specific short date expression with full year
            String pattern = getDatePattern(sdf);
            sdf.applyPattern(pattern);
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                Log.e("DateConvertion", e.getMessage(), e);
            }
        }
        try {
            df.parse(date);
            return true;
        } catch (ParseException e) {
            Log.e("DateConvertion", e.getMessage(), e);
        }
        return false;
    }

    public static void applyToolbarTitleFontTextBackgroundAndTextColor(final Activity context) {
        Toolbar toolbar = context.findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

}

