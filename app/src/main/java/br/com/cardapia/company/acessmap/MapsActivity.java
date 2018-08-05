package br.com.cardapia.company.acessmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.golshadi.orientationSensor.sensors.Orientation;
import com.golshadi.orientationSensor.utils.OrientationSensorInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import br.com.cardapia.company.acessmap.bean.BotaoReportBean;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.InstrucaoRotaBean;
import br.com.cardapia.company.acessmap.bean.LocationBean;
import br.com.cardapia.company.acessmap.bean.LocationNameBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.bean.MarkerCommentsBean;
import br.com.cardapia.company.acessmap.bean.NotificationClickBean;
import br.com.cardapia.company.acessmap.bean.RouteBean;
import br.com.cardapia.company.acessmap.bean.SidewalkBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.bean.google.GoogleDirections;
import br.com.cardapia.company.acessmap.bean.here.HereDirections;
import br.com.cardapia.company.acessmap.bean.here.Maneuver;
import br.com.cardapia.company.acessmap.bean.here.Response;
import br.com.cardapia.company.acessmap.bean.mapbox.Leg;
import br.com.cardapia.company.acessmap.bean.mapbox.MapBoxDirections;
import br.com.cardapia.company.acessmap.bean.mapbox.Route;
import br.com.cardapia.company.acessmap.bean.mapbox.Step;
import br.com.cardapia.company.acessmap.bean.osm.OsmDirections;
import br.com.cardapia.company.acessmap.bean.osm.Segment;
import br.com.cardapia.company.acessmap.services.GoogleDirectionsApiService;
import br.com.cardapia.company.acessmap.services.HereApiService;
import br.com.cardapia.company.acessmap.services.MapBoxApiService;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.OSMService;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.ColorUtil;
import br.com.cardapia.company.acessmap.util.LatLongUtils;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import br.com.cardapia.company.acessmap.util.PolylineDecoder;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import io.fabric.sdk.android.Fabric;

public class MapsActivity extends FragmentActivity implements
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NoticeDialogFragment.NoticeDialogListener,
        MoovDisContants,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OrientationSensorInterface {


    public static BotaoReportBean[] BOTAO_GOOD = {
            new BotaoReportBean(BUTTON_NAME_RAMPA, R.drawable.rampa, App.getContext().getResources().getString(R.string.rampa_access), R.drawable.rampa_back),
            new BotaoReportBean(BUTTON_NAME_BANHEIRO, R.drawable.restroom, App.getContext().getResources().getString(R.string.restroom_acessible), R.drawable.restroom_back),
            new BotaoReportBean(BUTTON_NAME_ONIBUS, R.drawable.bus, App.getContext().getResources().getString(R.string.bus), R.drawable.bus_back),
            new BotaoReportBean(BUTTON_NAME_GUIA_CALCADA, R.drawable.blind_guideline, App.getContext().getResources().getString(R.string.blind_guideline), R.drawable.blind_guideline_back),
            new BotaoReportBean(BUTTON_NAME_PARKING, R.drawable.parking, App.getContext().getResources().getString(R.string.parking), R.drawable.parking_back),
            new BotaoReportBean(BUTTON_NAME_SEMAFORO, R.drawable.traffic_light, App.getContext().getResources().getString(R.string.traffic_light_sound), R.drawable.traffic_light_back),
            new BotaoReportBean(BUTTON_NAME_ESPORTE, R.drawable.sports, App.getContext().getResources().getString(R.string.sports), R.drawable.sports_back)};

    public static BotaoReportBean[] BOTAO_BAD = {
            new BotaoReportBean(BUTTON_NAME_BURACO, R.drawable.hole, App.getContext().getResources().getString(R.string.hole), R.drawable.hole_back),
            new BotaoReportBean(BUTTON_NAME_BARREIRA, R.drawable.barrier, App.getContext().getResources().getString(R.string.barrier), R.drawable.barrier),
            new BotaoReportBean(BUTTON_NAME_OBRA, R.drawable.maquina_obra, App.getContext().getResources().getString(R.string.building), R.drawable.maquina_obra_back),
            new BotaoReportBean(BUTTON_NAME_ASSALTO, R.drawable.assalto, App.getContext().getResources().getString(R.string.crims), R.drawable.assalto_back),
            new BotaoReportBean(BUTTON_NAME_CACAMBA, R.drawable.cacamba, App.getContext().getResources().getString(R.string.trash_can), R.drawable.cacamba_back),
            new BotaoReportBean(BUTTON_NAME_NAO_CADEIRA, R.drawable.not_acessible, App.getContext().getResources().getString(R.string.not_acessibible_walk), R.drawable.not_acessible_back),
            new BotaoReportBean(BUTTON_NAME_ESCADA, R.drawable.stairs, App.getContext().getResources().getString(R.string.stairs), R.drawable.stairs_back),
            new BotaoReportBean(BUTTON_NAME_NO_BLIND, R.drawable.no_blind, App.getContext().getResources().getString(R.string.danger_obstacule), R.drawable.no_blind_back),
            new BotaoReportBean(BUTTON_NAME_LARGURA, R.drawable.width, App.getContext().getResources().getString(R.string.sidewalk_width), R.drawable.width_back)};

    private final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> MarkerPoints;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private PlaceAutocompleteFragment autocompleteFragment;
    private SharedPreferences sharedPref;
    private BottomSheetBehavior bottomResultRota;
    private String _id_marker;
    private Long timeLastCallAlerts = 0L;
    /// RESULTADOS DA ROTA
    private ImageButton btnPrev;
    private ImageButton btnNext;
    private String[] totalDistance = null;
    private int[] alertsOnPath = null;
    private boolean[] straitSidewalk = null;
    private int posRota;
    ///
    private List<Set<InstrucaoRotaBean>> instructionsList[] = null;
    private int countTryLocalization = 0;
    private List<MarkerBean> markersList = null;

    private int larguraCadeira = 80;
    private int deviceWidth = 640;

    private FloatingActionButton btnNavigate = null;
    private TextView txtLocation = null;


    ShareDialog shareDialog;


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {
                for (Location location : locationResult.getLocations()) {
                    Log.i("onLocationResult", "Location: " + location.getLatitude() + " " + location.getLongitude());

                    if (mMap == null) {
                        Log.d("onLocationResult", "Mapa ainda não foi criado:");
                        continue;
                    }

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mLastLocation == null || !LatLongUtils.isEquals(mLastLocation, location, LatLongUtils.PRECISION_FOUR)) {
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_17));

                        new AlertsGetRequestTask().execute();
                    }
                    mLastLocation = location;
                    txtLocation.setText(String.format("mLocationCallback: [%s, %s] Spped [%s]", mLastLocation.getLatitude(), mLastLocation.getLongitude(), mLastLocation.getSpeed()));

                }
            }
        }
    };
    private Orientation orientationSensor;
    private boolean pausedByLocationSearch = false;
    private boolean movedByLocationButton = false;
    // FOR ROUTE
    private LatLng origin;
    private LatLng dest;

    /**
     * Criacao da Tela.
     *
     * @param savedInstanceState param
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_main);

        ButterKnife.bind(this);

        txtLocation = (TextView) findViewById(R.id.txt_location);
        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
            txtLocation.setVisibility(View.INVISIBLE);
        }

        orientationSensor = new Orientation(this.getApplicationContext(), this);

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String idUsuario = sharedPref.getString(ID_USUARIO, StringUtils.EMPTY);
        larguraCadeira = sharedPref.getInt(WEELCHAIR_WIDTH, larguraCadeira);

        btnNavigate = (FloatingActionButton) findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNavigate.setVisibility(View.GONE);

                mMap.addMarker(new MarkerOptions()
                        .position(origin)
                        .title(getString(R.string.ponto_partida))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                traceRouteOSM(origin, dest, false);
                //traceRouteHere(origin, dest);
            }
        });

        final BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmbReportGood);
        bmb.setNormalColor(Color.GREEN);
        bmb.setBackgroundEffect(true);
        bmb.setDimColor(ColorUtil.getColorWithAlpha(Color.GRAY, 70f));
        bmb.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_7_1);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_7_1);

        final BoomMenuButton bmbBad = (BoomMenuButton) findViewById(R.id.bmbReportBad);
        bmbBad.setNormalColor(Color.RED);
        bmbBad.setBackgroundEffect(true);
        bmbBad.setDimColor(ColorUtil.getColorWithAlpha(Color.GRAY, 50f));
        bmbBad.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        bmbBad.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        bmbBad.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);

        // Enable Location Services
        MoovDisUtil.checkLocationPermission(this);
        MoovDisUtil.enableLocationService(this);

        setDisplaySize();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initializing
        MarkerPoints = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);

        // Reposiciona os botoes de compasso e de localizacao no mapa
        if (mapFragment.getView().findViewById(Integer.parseInt(ID_GOOGLE)) != null) {
            // Get the view
            View locationCompass = ((View) mapFragment.getView().findViewById(Integer.parseInt(ID_GOOGLE))
                    .getParent()).findViewById(Integer.parseInt(GOOGLE_ICON_COMPASS));
            // and next place it, on bottom right (as Google Maps app)
            if (locationCompass != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationCompass.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 250, 30, 0); // 160 la truc y , 30 la  truc x
            }
            View locationMyLocation = ((View) mapFragment.getView().findViewById(Integer.parseInt(ID_GOOGLE))
                    .getParent()).findViewById(Integer.parseInt(GOOGLE_ICON_LOCATION));
            // and next place it, on bottom right (as Google Maps app)
            if (locationMyLocation != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationMyLocation.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 250, 30, 0); // 160 la truc y , 30 la  truc x
            }

            FloatingActionButton btnMic = (FloatingActionButton) findViewById(R.id.btn_mic);
            btnMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promptSpeechInput();
                }
            });

        }

        // AUTO COMPLETE
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {

            autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // example : way to access view from PlaceAutoCompleteFragment
                            // HABILITA A LOCALIZACAO
                            autocompleteFragment.setText(StringUtils.EMPTY);
                            view.setVisibility(View.GONE);
                            pausedByLocationSearch = false;
                            requestLocationUpdates();
                        }
                    });


            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i("onPlaceSelected", "Place: " + place.getName());

                    pausedByLocationSearch = true;

                    String placeName = place.getName().toString();

                    if (mLastLocation != null) {
                        origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        btnNavigate.setVisibility(View.VISIBLE);
                    } else {
                        origin = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        btnNavigate.setVisibility(View.VISIBLE);
                    }

                    dest = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                    Location location = new Location(place.getName().toString());
                    location.setLatitude(place.getLatLng().latitude);
                    location.setLongitude(place.getLatLng().longitude);
                    location.setTime(new Date().getTime()); //Set time as current Date
                    mLastLocation = location;
                    txtLocation.setText(String.format("onPlaceSelected: [%s, %s]", mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                    MarkerPoints.add(new MarkerOptions()
                            .position(dest)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.if_finish_flag)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(dest));

                    new AlertsGetRequestTask().execute(Boolean.TRUE);
                    disableLocationServices();
                }

                @Override
                public void onError(Status status) {
                    Log.i("onError", "Ocorreu um erro: " + status);
                }
            });
        }


        // Menu Button Good
        for (int i = 0; i < BOTAO_GOOD.length; i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(BOTAO_GOOD[i].getImageButton())
                    .normalText(BOTAO_GOOD[i].getText())
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textGravity(Gravity.CENTER)
                    .shadowEffect(true)
                    .isRound(false)
                    .normalColor(Color.WHITE)
                    .shadowCornerRadius(Util.dp2px(20))
                    .buttonCornerRadius(Util.dp2px(20))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            if (mLastLocation == null) {
                                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.location_waiting));
                                return;
                            }

                            mMap.addMarker(new MarkerOptions().position(
                                    new LatLng(mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude()))
                                    .title(BOTAO_GOOD[index].getText())
                                    .flat(true)
                                    .snippet(BOTAO_GOOD[index].getText())
                                    .icon(BitmapDescriptorFactory.fromBitmap(
                                            resizeImageToMarkerSize(BOTAO_GOOD[index].getImageButtonBack() == 0 ?
                                                    BOTAO_GOOD[index].getImageButton() :
                                                    BOTAO_GOOD[index].getImageButtonBack()))));

                            MarkerBean bean = new MarkerBean();
                            bean.setDateCreated(new Date());
                            bean.setLabel(BOTAO_GOOD[index].getText());
                            LocationBean locationBean = new LocationBean();
                            locationBean.setType(LOCATION_TYPE_POINT);
                            locationBean.setCoordinates(new double[]{mLastLocation.getLongitude(), mLastLocation.getLatitude()});
                            bean.setLocation(locationBean);
                            bean.setMarkerType(MARKER_GOOD);
                            bean.setMarkerIcon(BOTAO_GOOD[index].getButtonName());
                            UsuarioBean usuarioBean = new UsuarioBean();
                            usuarioBean.set_id(idUsuario);
                            bean.setUser(usuarioBean);

                            new MarkerAddRequestTask().execute(bean);
                        }
                    });
            bmb.addBuilder(builder);
        }

        // Menu Button Good
        for (int i = 0; i < BOTAO_BAD.length; i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(BOTAO_BAD[i].getImageButton())
                    .normalText(BOTAO_BAD[i].getText())
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textGravity(Gravity.CENTER)
                    .shadowEffect(true)
                    .isRound(false)
                    .shadowCornerRadius(Util.dp2px(20))
                    .normalColor(Color.WHITE)
                    .buttonCornerRadius(Util.dp2px(20))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            if (mLastLocation == null) {
                                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.location_waiting));
                                return;
                            }

                            mMap.addMarker(new MarkerOptions().position(
                                    new LatLng(mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude()))
                                    .title(BOTAO_BAD[index].getText())
                                    .flat(true)
                                    .snippet(BOTAO_BAD[index].getText())
                                    .icon(BitmapDescriptorFactory.fromBitmap(
                                            resizeImageToMarkerSize(BOTAO_BAD[index].getImageButtonBack() == 0 ?
                                                    BOTAO_BAD[index].getImageButton() :
                                                    BOTAO_BAD[index].getImageButtonBack()))));

                            MarkerBean bean = new MarkerBean();
                            bean.setDateCreated(new Date());
                            bean.setLabel(BOTAO_BAD[index].getText());
                            LocationBean locationBean = new LocationBean();
                            locationBean.setType(LOCATION_TYPE_POINT);
                            locationBean.setCoordinates(new double[]{mLastLocation.getLongitude(), mLastLocation.getLatitude()});
                            bean.setLocation(locationBean);
                            bean.setMarkerType(MARKER_BAD);
                            bean.setMarkerIcon(BOTAO_BAD[index].getButtonName());
                            UsuarioBean usuarioBean = new UsuarioBean();
                            usuarioBean.set_id(idUsuario);
                            bean.setUser(usuarioBean);
                            new MarkerAddRequestTask().execute(bean);


                        }
                    });
            bmbBad.addBuilder(builder);
        }

        // Text Alertas
        final TextView txtAlertas = (TextView) findViewById(R.id.txt_alertas);
        txtAlertas.setText(StringUtils.EMPTY);
        new AlertsGetRequestTask().execute();

        // Area do resultado da Rota
        View bottomSheet = findViewById(R.id.bottom_sheet1);
        bottomResultRota = BottomSheetBehavior.from(bottomSheet);
        bottomResultRota.setHideable(true);
        bottomResultRota.setPeekHeight(450);
        bottomResultRota.setState(BottomSheetBehavior.STATE_HIDDEN);

        Button btnEtapas = (Button) findViewById(R.id.btnEtapas);
        btnEtapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomResultRota.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomResultRota.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (bottomResultRota.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomResultRota.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        Button btnNavegar = (Button) findViewById(R.id.btnNavegar);
        btnNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (origin != null && dest != null) {

                    // SAVE ROUTE OF USER
                    RouteBean bean = new RouteBean();
                    bean.setComment(StringUtils.EMPTY);
                    bean.setDateCreated(new Date());

                    List<InstrucaoRotaBean> instrucoesRota = new ArrayList(instructionsList[0].get(0));
                    LocationNameBean locationOrig = new LocationNameBean();
                    locationOrig.setCoordinates(new double[]{origin.longitude, origin.latitude});
                    locationOrig.setName(instrucoesRota.get(0).getInstruction());
                    locationOrig.setType(LOCATION_TYPE_POINT);
                    bean.setLocationOrig(locationOrig);

                    LocationNameBean locationDest = new LocationNameBean();
                    locationDest.setCoordinates(new double[]{dest.longitude, dest.latitude});
                    locationDest.setName(instrucoesRota.get(instrucoesRota.size() - 1).getInstruction());
                    locationDest.setType(LOCATION_TYPE_POINT);

                    bean.setLocationDest(locationDest);

                    bean.setDistance(totalDistance[0]);
                    UsuarioBean usuarioBean = new UsuarioBean();
                    usuarioBean.set_id(idUsuario);
                    bean.setUser(usuarioBean);

                    new RouteSaveRequestTask().execute(bean);

                    try {
                        String destString = String.format("%s,%s", dest.latitude, dest.longitude);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destString + "&mode=w");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                    catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.maps_app_not_instaled),
                            Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        FloatingActionButton btnSidewalk = findViewById(R.id.btn_sidewalk);
        btnSidewalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //frm.setVisibility(View.VISIBLE);
                //SidewalkTypeFragment fragment = new SidewalkTypeFragment();
                //FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                //trans.add(R.id.item_detail_container, fragment).commit();

                if (mLastLocation == null) {
                    MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.location_waiting));
                    return;
                }

                Intent intent = new Intent(MapsActivity.this, SidewalkTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CURRENT_LOCATION, new double[]{mLastLocation.getLatitude(), mLastLocation.getLongitude()});
                intent.putExtra(ID_USUARIO, idUsuario);
                startActivityForResult(intent, REQ_SIDEWALK_MARKER);

            }
        });

        // VERIFY IF USER IS ENABLED
        if (StringUtils.isBlank(idUsuario)) {
            // FACA O CADASTRO
            Toast.makeText(this, getString(R.string.first_access), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MapsActivity.this, PreferencesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            abortLocalizationTrying();
            finish();
            return;
        }

        // VERIFY IF GOOGLE PLAY IS ENABLED
        if (!MoovDisUtil.checkPlayServices(this)) {
            return;
        }

        // WHO IS THE PARENT, IS A PUSH?
        if (this.getIntent() != null &&
                ((this.getIntent().getExtras() != null
                        && this.getIntent().getExtras().get("FROM") != null
                        && "FCM".equals(this.getIntent().getExtras().get("FROM")))
                        || "PushClickIntent".equals(this.getIntent().getAction()))) {
            // SAVE THE PUSH CLICK
            NotificationClickBean bean = new NotificationClickBean();
            bean.setUserId(idUsuario);
            new NotiticationSaveClickRequestTask().execute(bean);
        }

        // VERIFICA PERMISSAO PARA ACESSAR A CAMERA
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
        }

        // FACEBOOK SHARING
        shareDialog = new ShareDialog(this);

    }

    @Override
    public void onBackPressed() {
        abortLocalizationTrying();
        super.onBackPressed();
        finish();
    }

    /**
     * Cancela a busca por ocorrecias
     */
    private void abortLocalizationTrying() {
        countTryLocalization = LIMIT_TRY_LOCALIZATION;
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.touch_to_speak));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MapsActivity.this,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String s : result) {
                        Log.d("Text Result:", s);
                    }
                    if (autocompleteFragment != null) {
                        // Pega o texto do Speech e joga na pesquisa de locais
                        autocompleteFragment.setText(result.get(0));
                        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button).callOnClick();
                    }
                }
                break;
            }
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK && null != data) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    try {
                        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                            assert photo != null;
                            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            stream.toByteArray();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            case REQ_MARKER_DETAIL: {
                if (resultCode == RESULT_OK && null != data) {
                    boolean delete = data.getBooleanExtra(DELETE_MARKER, false);
                    if (delete) {
                        // PEGA OS ALERTAS PROXIMOS
                        timeLastCallAlerts = timeLastCallAlerts - MINUTE_MILISECONDS;
                        new AlertsGetRequestTask().execute();
                    }
                    break;
                }
            }
            case REQ_SIDEWALK_MARKER: {
                if (resultCode == RESULT_OK && null != data) {
                    boolean insert = data.getBooleanExtra(INSERT_WIDEWALK, false);
                    if (insert) {
                        // PEGA OS ALERTAS PROXIMOS
                        timeLastCallAlerts = timeLastCallAlerts - MINUTE_MILISECONDS;
                        new AlertsGetRequestTask().execute();
                    }
                    break;
                }
            }
            default:
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mFusedLocationClient != null) {
                requestLocationUpdates();
            } else {
                buildGoogleApiClient();
            }

        }

        doMyLocationEnabled();

        if (mMap == null) {
            return;
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        // ADD EVENTS MAP
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveStartedListener(this);

        // Setting onclick event listener for the map
        /*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Already two locations

                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                    //
                    plotAlertsOnMap(markersList);
                }f

                // Adding new item to the ArrayList
                MarkerPoints.add(point);
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);

                //
                // For the start location, the color of marker is GREEN and
                // for the end location, the color of marker is RED.
                //
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    options.title("Ponto de Partida");
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.if_finish_flag));
                    options.title("Ponto de Chegada");
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);
                // Checks, whether start and end locations are captured
                // SE DOIS PONTOS FOREM DEFINIDOS FAZ A ROTA
                if (MarkerPoints.size() >= 2) {
                    origin = MarkerPoints.get(0);
                    dest = MarkerPoints.get(1);
                    traceRoute(origin, dest);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
            }
        });
        */


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location location = mMap.getMyLocation();

                if (location != null) {
                    mLastLocation = location;
                    txtLocation.setText(String.format("onMyLocationClick: [%s, %s]", mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                    new AlertsGetRequestTask().execute(Boolean.TRUE);
                }

                movedByLocationButton = true;
                requestLocationUpdates();
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(CURRENT_LOCATION)) {
            disableLocationServices();
            pausedByLocationSearch = true;

            Location loc = (Location) extras.get(CURRENT_LOCATION);
            mLastLocation = loc;

            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_19));
            new AlertsGetRequestTask().execute(true);
        }

    }

    /**
     * Trace a Route end plote on map
     *
     * @param origin
     * @param dest
     */
    private void traceRouteMapBox(final LatLng origin, final LatLng dest) {
        // Getting URL to the MaapBox Directions API
        String url = MapBoxApiService.getApiUrl(origin, dest, getString(R.string.map_box_key));
        Log.d("traceRouteMapBox", url.toString());
        new MapBoxDirectionsRequestTask().execute(url);
    }

    /**
     * Trace a Route end plote on map
     *
     * @param origin
     * @param dest
     */
    private void traceRouteHere(final LatLng origin, final LatLng dest) {
        // Getting URL to the MaapBox Directions API
        String[] params = HereApiService.getApiUrl(origin, dest, getString(R.string.here_app_id), getString(R.string.here_app_code), markersList);
        Log.d("traceRouteHere", params[0]);
        Object[] args = new Object[5];
        args[0] = params[0];
        args[1] = params[1];
        args[2] = null;
        args[3] = origin;
        args[4] = dest;

        new HereDirectionsRequestTask().execute(args);
    }

    /**
     * Trace a Route end plote on map
     *
     * @param origin
     * @param dest
     */
    private void traceRouteOSM(final LatLng origin, final LatLng dest, boolean isWeelchair) {
        // Getting URL to the MaapBox Directions API
        String url[] = OSMService.getApiUrl(origin, dest, getString(R.string.osm_key), markersList, isWeelchair);
        Log.d("traceRouteOSM", url[0]);
        Object[] args = new Object[5];
        args[0] = url[0];
        args[1] = url[1];
        args[2] = (isWeelchair ? YES : NO);
        args[3] = origin;
        args[4] = dest;
        new OSMDirectionsRequestTask().execute(args);
    }


    /**
     * Trace a Route end plote on map
     *
     * @param origin
     * @param dest
     */
    private void traceRoute(final LatLng origin, final LatLng dest) {
        // Getting URL to the Google Directions API
        String url = GoogleDirectionsApiService.getApiUrl(origin, dest, getString(R.string.google_directions_key));
        //Log.d("onMapClick", url.toString());
        new GoogleDirectionsRequestTask().execute(url);
    }

    public void showNoticeDialog(final String idMarker, final LatLng location) {
        // Create an instance of the dialog fragment and show it
        final String TAG = "dialog";
        this._id_marker = idMarker;
        NoticeDialogFragment dialog = new NoticeDialogFragment();
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Panel);
        dialog.setLocation(location);

        // PREPARA CARREGAMENTO
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // FIM PREPARADA

        dialog.show(ft, "dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String cometary = ((NoticeDialogFragment) dialog).getComments();
        byte[] markerImage = ((NoticeDialogFragment) dialog).getMarkerImage();
        MarkerCommentsBean bean = new MarkerCommentsBean();
        bean.setImage(markerImage);
        bean.setComment(cometary);
        LocationBean locBean = new LocationBean();
        locBean.setType(LOCATION_TYPE_POINT);
        locBean.setCoordinates(new double[]{((NoticeDialogFragment) dialog).getLocation().longitude,
                ((NoticeDialogFragment) dialog).getLocation().latitude});
        bean.setLocation(locBean);
        bean.set_id(this._id_marker);
        new MarkerUpdateRequestTask().execute(bean);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ColorDialog dialog = new ColorDialog(MapsActivity.this);
                dialog.setTitle(getString(R.string.cancel_marker));
                dialog.setContentText(getString(R.string.cancel_marker_detail));
                dialog.setPositiveListener(getString(R.string.yes), new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                        new MarkerDeleteRequestTask().execute(_id_marker);
                    }
                }).setNegativeListener(getString(R.string.no), new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        // ABRE O ALERTA PARA ADICIONAR COMENTARIOS
                        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        showNoticeDialog(_id_marker, latLng);
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    /**
     * Exibe a próxima Rota
     */
    private void showNextRoute() {
        int totalRotas = this.totalDistance.length;
        this.posRota += 1;
        this.btnPrev.setVisibility(View.VISIBLE);
        if (this.posRota == (totalRotas - 1)) {
            this.btnNext.setVisibility(View.INVISIBLE);
        }
        showRouteInstructions(instructionsList[posRota].get(0), (posRota == FIRST_INDEX && totalRotas > 1), (posRota + 1), alertsOnPath[posRota], straitSidewalk[posRota]);

    }

    // Exibe a Rota Anterior
    private void showPrevRoute() {
        int totalRotas = this.totalDistance.length;
        this.posRota -= 1;
        this.btnNext.setVisibility(View.VISIBLE);
        if (this.posRota == FIRST_INDEX) {
            this.btnPrev.setVisibility(View.INVISIBLE);
        }
        showRouteInstructions(instructionsList[posRota].get(0), (posRota == FIRST_INDEX && totalRotas > 1), (posRota + 1), alertsOnPath[posRota], straitSidewalk[posRota]);

    }

    /**
     * Exibe o resultado da Rota
     *
     * @param listInstructions
     */
    private void showRouteInstructions(Set<InstrucaoRotaBean> listInstructions, boolean isEtapaPrincipal, int routeNumber, int totalAlerts, boolean strait) {

        // POPULA OS DADOS DO RESULTADO DA ROTA
        String calcadaEstreitaString = (strait ? getString(R.string.calcada_estreita) : StringUtils.EMPTY);
        TextView totalDistanceTextView = (TextView) findViewById(R.id.totalDistance);
        if (totalAlerts == TOTAL_ZERO) {
            totalDistanceTextView.setText(getHtmlText(getString(R.string.info_distancia_no_alert, totalDistance[0], calcadaEstreitaString)));
        } else if (totalAlerts == TOTAL_UM) {
            totalDistanceTextView.setText(getHtmlText(getString(R.string.info_distancia_one_alert, totalDistance[0], calcadaEstreitaString)));
        } else {
            totalDistanceTextView.setText(getHtmlText(getString(R.string.info_distancia, totalDistance[0], totalAlerts, calcadaEstreitaString)));
        }

        TextView optionRoute = (TextView) findViewById(R.id.route_option);
        optionRoute.setText(getString(R.string.route_option, routeNumber));
        if (totalDistance.length <= 1) {
            optionRoute.setVisibility(View.INVISIBLE);
        } else {
            optionRoute.setVisibility(View.VISIBLE);
        }

        // POPULA AS ROTAS
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_etapas);
        // REMOVE TODOS OS COMPONENTES ANTES DE CRIAR NOVOS
        linearLayout.removeAllViewsInLayout();

        // Creating a List of HashSet elements
        List<InstrucaoRotaBean> listSteps = new ArrayList<>(listInstructions);
        for (int i = 0; i < listSteps.size() - 1; i++) {
            InstrucaoRotaBean instuction = listSteps.get(i);
            TextView tv = new TextView(MapsActivity.this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setPadding(32, 16, 32, 0);
            linearLayout.addView(tv);
            if (i == FIRST_INDEX) {
                Spanned html = getHtmlText(getString(R.string.etapa_rota, instuction.getInstruction(), listSteps.get(listInstructions.size() - 1).getInstruction()));
                if (isEtapaPrincipal) {
                    html = getHtmlText(getString(R.string.etapa_rota, instuction.getInstruction(), listSteps.get(listInstructions.size() - 1).getInstruction()) + getString(R.string.etapa_principal));
                }
                tv.setText(html);
            } else {
                if (StringUtils.isBlank(instuction.getDistance())) {
                    tv.setText(getHtmlText(String.format("%s. ", i) + instuction.getInstruction()));
                } else {
                    tv.setText(getHtmlText(String.format("%s. ", i) + getString(R.string.etapa_instruct, instuction.getDistance(), instuction.getInstruction())));
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private Spanned getHtmlText(String texto) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(texto, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(texto);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Desabilita a Localizacao
     */
    private void disableLocationServices() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        Log.i("disableLocationServices", "Localização Desabilitada");
    }

    @Override
    public void onPause() {
        super.onPause();
        pausedByLocationSearch = false;
        Log.i(TAG, "onPause: " + pausedByLocationSearch);
        disableLocationServices();
    }

    private void turnOnSensor() {


        //------Turn Orientation sensor ON-------
        // set tolerance for any directions
        orientationSensor.init(1.0, 1.0, 1.0);


        // return true or false
        if (orientationSensor.isSupport()) {
            // set output speed and turn initialized sensor on
            // 0 Normal
            // 1 UI
            // 2 GAME
            // 3 FASTEST
            orientationSensor.on(0);
            //---------------------------------------
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: " + pausedByLocationSearch);
        String placeText = ((EditText) findViewById(R.id.place_autocomplete_search_input)).getText().toString();
        if (!pausedByLocationSearch && StringUtils.isBlank(placeText)) {
            requestLocationUpdates();
        }
        //turnOnSensor();
    }

    @Override
    public void orientation(Double AZIMUTH, Double PITCH, Double ROLL) {

        String text = String.format("Azimuth: [%s] PITCH: [%s] ROLL: [%s]", AZIMUTH, PITCH, ROLL);
        txtLocation.setText(text);
        if (mLastLocation != null) {
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraPosition position = CameraPosition.builder().bearing(AZIMUTH.floatValue()).target(latLng).zoom(17f).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //orientationSensor.off();
        disableLocationServices();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Solicita a Atualizacao de Localizacao
     */
    private void requestLocationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        Log.i("requestLocationUpdates", "Localização Habilitada");


    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!pausedByLocationSearch) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", location.getLatitude() + "," + location.getLongitude());
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mLastLocation == null || !LatLongUtils.isEquals(mLastLocation, location, LatLongUtils.PRECISION_FOUR)) {
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_17));

            new AlertsGetRequestTask().execute();
        }

        mLastLocation = location;
        txtLocation.setText(String.format("onLocationChanged: [%s, %s]", mLastLocation.getLatitude(), mLastLocation.getLongitude()));


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MoovDisUtil.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient != null && mFusedLocationClient != null) {
                            requestLocationUpdates();
                        } else {
                            buildGoogleApiClient();
                        }
                        doMyLocationEnabled();

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void doMyLocationEnabled() {
        if (mMap != null) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException sex) {
                Toast.makeText(this, getString(R.string.location_activate), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.map_not_ready), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param imageResourceId
     * @return
     */
    public Bitmap resizeImageToMarkerSize(int imageResourceId) {
        float ratioWidth = 18.0f;
        float ratioHeight = 11.25f;

        int height = (int) (deviceWidth / ratioHeight);
        int width = (int) (deviceWidth / ratioWidth);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(imageResourceId);
        Bitmap b = bitmapdraw.getBitmap();
        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    /**
     * @param buttonType
     * @param buttonIcon
     * @return
     */
    private BotaoReportBean getBotaoReport(String buttonType, String buttonIcon) {
        BotaoReportBean[] botoes = null;
        if (MARKER_GOOD.equalsIgnoreCase(buttonType)) {
            botoes = BOTAO_GOOD;
        } else {
            botoes = BOTAO_BAD;
        }
        for (BotaoReportBean botaoBean : botoes) {
            if (botaoBean.getButtonName().equals(buttonIcon)) {
                return botaoBean;
            }
        }
        return null;
    }

    /**
     * @param v
     */
    public void onClickBackButton(View v) {
        Log.d("onClickBackButton", "Back...");
        this.onBackPressed();
    }

    /**
     * @param markers
     */
    private void plotAlertsOnMap(List<MarkerBean> markers) {

        if (markers == null || markers.isEmpty()) {
            markers = new ArrayList<>();
        }

        if (mMap != null) {
            mMap.clear();
        }

        for (MarkerBean bean : markers) {

            BotaoReportBean botao = getBotaoReport(bean.getMarkerType(), bean.getMarkerIcon());

            BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(
                    resizeImageToMarkerSize(botao.getImageButtonBack() == 0 ?
                            botao.getImageButton() : botao.getImageButtonBack()));


            if (botao != null) {
                LatLng position = new LatLng(bean.getLocation().getCoordinates()[1],
                        bean.getLocation().getCoordinates()[0]);
                String addr = StringUtils.EMPTY;
                if (StringUtils.isNotBlank(bean.getAddressDescription())) {
                    addr = bean.getAddressDescription().split(",")[0];
                }
                mMap.addMarker(new MarkerOptions().position(position)
                        .title(getString(R.string.ocorrencia_na, bean.getLabel(), addr))
                        .snippet(bean.getComment())
                        .icon(bm))
                        .setTag(bean.get_id());

            }
        }

        for (MarkerOptions marker : MarkerPoints) {
            mMap.addMarker(marker);
        }
        MarkerPoints = new ArrayList<>();
    }

    /**
     * @param sidewalks
     */
    private void plotSidewalksOnMap(List<SidewalkBean> sidewalks) {

        if (sidewalks == null || sidewalks.isEmpty()) {
            sidewalks = new ArrayList<>();
        }

        for (SidewalkBean bean : sidewalks) {

            List<LatLng> coordinates = Arrays.asList(
                    new LatLng(bean.getLocationStart().getCoordinates()[1], bean.getLocationStart().getCoordinates()[0]),
                    new LatLng(bean.getLocationEnd().getCoordinates()[1], bean.getLocationEnd().getCoordinates()[0]));

            mMap.addPolyline(MoovDisUtil.makeSidewalkLineColorPrincipal(bean.getSidewalkType(), coordinates));
            mMap.addPolyline(MoovDisUtil.makeSidewalkLineColorSecundary(bean.getSidewalkType(), coordinates));
        }

    }

    /**
     * ao clicar num marcador
     * @param marker
     * @return
     */
    /**
     * Chamado quando o usuário clica em um marcador.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        /*
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
        */


        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);

        if (marker.getTag() != null) {
            Intent intent = new Intent(MapsActivity.this, MarkerDetailActivity.class);
            intent.putExtra(MARKER_ID, (String) marker.getTag());
            startActivityForResult(intent, REQ_MARKER_DETAIL);
        }

        return false;
    }

    /**
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    /**
     * Verifica se tem calcada estreita
     *
     * @param markersList
     * @return
     */
    private List<MarkerBean> extractStraitSidewalk(final List<MarkerBean> markersList) {
        List<MarkerBean> newList = new ArrayList<>();
        // VERIFICA SE A LARGURA DA CADEIRA EH MAIS LARGA QUE O PADRAO
        final int larguraCadeira = sharedPref.getInt(WEELCHAIR_WIDTH, DEFAULT_WEELCHAIR_WIDTH);
        if (larguraCadeira <= DEFAULT_WEELCHAIR_WIDTH) {
            return newList;
        }

        // SE A LARGURA FOR MAIOR, ALERTA
        if (markersList != null && !markersList.isEmpty()) {
            for (MarkerBean bean : markersList) {
                if (bean.getMarkerIcon().equals(BUTTON_NAME_LARGURA)) {
                    newList.add(bean);
                }
            }
        }

        return newList;
    }

    /**
     * Verifica se Usa cadeira de Rodas
     *
     * @return
     */
    private boolean useWeelchair() {
        return sharedPref.getBoolean(USE_WEELCHAIR, false);
    }

    /**
     *
     */
    private void setShareDontAskAgain() {
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARE_DONT_ASK_AGAIN, true);
            editor.commit();
        }
    }

    private boolean getShareDontAskAgain() {
        if (sharedPref != null) {
            if (sharedPref.contains(SHARE_DONT_ASK_AGAIN)) {
                return sharedPref.getBoolean(SHARE_DONT_ASK_AGAIN, false);
            }
        }
        return false;
    }

    @Override
    public void onCameraIdle() {
        Log.i("onCameraIdle", "“A câmera parou de se mover.");
    }

    @Override
    public void onCameraMoveCanceled() {
        Log.i("onCameraIdle", "Movimento da câmera cancelado.");
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.i("onCameraMoveStarted", "O usuário usou gesto no mapa.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Log.i("onCameraMoveStarted", "O usuário tocou em algum ponto do mapa.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Log.i("onCameraMoveStarted", "O aplicativo movimentou a câmera.");
        }

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE ||
                reason == GoogleMap.OnCameraMoveStartedListener
                        .REASON_API_ANIMATION && !movedByLocationButton) {
            disableLocationServices();
        }
        movedByLocationButton = false;
    }

    /**
     * Inicializa a Lista de Rotas
     *
     * @param size
     */
    private void initListsRoute(final int size) {
        posRota = 0;
        totalDistance = new String[size];
        alertsOnPath = new int[size];
        straitSidewalk = new boolean[size];
        instructionsList = new List[totalDistance.length];

        // DADOS PARA EXIBIR NO RESULTADO DAS ROTAS.
        for (int i = 0; i < instructionsList.length; i++) {
            instructionsList[i] = new ArrayList<>();
        }

        // Se nao encontrou a rota
        if (size == TOTAL_ZERO) {
            new PromptDialog(MapsActivity.this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                    .setAnimationEnable(true)
                    .setTitleText(getString(R.string.rota))
                    .setContentText(getString(R.string.rota_nao_encontrada))
                    .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
            return;
        }
    }

    /**
     * Mostra a elevação da Rota
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void showRouteElevation() {
        // Busca Elevação:
        // Getting URL to the Google Directions API
        String url = GoogleDirectionsApiService.getApiElevationUrl(origin,
                dest,
                getString(R.string.google_directions_key),
                getString(R.string.api_umbrella_key));

        WebView webview = (WebView) findViewById(R.id.web_elevation);
        //Log.d("loadElevation", url);
        webview.getSettings().setJavaScriptEnabled(true);
        final Activity activity = MapsActivity.this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.determinateBar);
                Log.d("proress", "" + progress);
                progressBar.setProgress(progress);

                if (progress >= CEM) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        webview.loadUrl(url);
    }

    /**
     * Inicializa os Botons de Navegacao
     *
     * @param size
     */
    private void initRouteOptionsButtons(final int size) {
        // ADICIONA BOTOES DAS ALTERNATIVAS DE ROTAS
        btnPrev = (ImageButton) findViewById(R.id.btn_route_prev);
        btnNext = (ImageButton) findViewById(R.id.btn_route_next);
        btnPrev.setVisibility(View.GONE);
        if (size == TOTAL_UM) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextRoute();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevRoute();
            }
        });
        // FIM BOTAO ALTERNATIVAS DE ROTAS

    }

    /**
     * @param points
     * @param alertsBadOnPathCount
     * @param bestRoute
     * @return
     */
    private PolylineOptions configureLineRoute(final List<LatLng> points, final int alertsBadOnPathCount, MutableBoolean bestRoute) {

        PolylineOptions lineOptions = new PolylineOptions();
        // Adding all the points in the route to LineOptions
        final int PATTERN_DASH_LENGTH_PX = 15;
        final int PATTERN_GAP_LENGTH_PX = 15;
        final List<PatternItem> PATTERN_POLYGON_ALPHA
                = Arrays.asList(new Dash(PATTERN_DASH_LENGTH_PX), new Gap(PATTERN_GAP_LENGTH_PX));
        lineOptions.addAll(points);


        if (alertsBadOnPathCount == Integer.MIN_VALUE) {
            lineOptions.color(Color.RED);
            lineOptions.width(22);
            lineOptions.zIndex(1000f);
        } else if (alertsBadOnPathCount > 0) {
            lineOptions.color(Color.GRAY);
            lineOptions.pattern(PATTERN_POLYGON_ALPHA);
            lineOptions.width(20);
            lineOptions.zIndex(888f);
        } else if (bestRoute.isFalse()) {
            lineOptions.color(Color.rgb(34, 139, 34));
            bestRoute.setValue(true);
            lineOptions.zIndex(999f);
            lineOptions.width(20);
        } else {
            lineOptions.color(Color.GRAY);
            lineOptions.pattern(PATTERN_POLYGON_ALPHA);
            lineOptions.width(20);
            lineOptions.zIndex(888f);
        }

        lineOptions.geodesic(true);
        return lineOptions;
    }

    /**
     * Desenha linha nos mapas
     *
     * @param list
     */
    private void drawLineRoutes(final List<PolylineOptions>[] list) {
        // FIRST ADD THE RED
        for (List<PolylineOptions> listPolygons : list) {

            for (int i = 0; i < listPolygons.size(); i++) {
                mMap.addPolyline(listPolygons.get(i));
            }

        }
    }

    private void alertaRotaExtensa() {
        // ALERTA DE ROTA EXTENSA
        boolean isLongWay = false;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);

        try {
            if (totalDistance[0].toLowerCase().contains("km")) {
                double km = nf.parse(totalDistance[0].toLowerCase().replace("km", "")).doubleValue();
                // BLOQUEIA SE ROTA FOR MAIOR QUE 1,5KM
                if (km > 1.5) isLongWay = true;
            } else if (totalDistance[0].toLowerCase().contains("m")) {
                double m = nf.parse(totalDistance[0].toLowerCase().replace("m", "")).doubleValue();
                // BLOQUEIA SE ROTA FOR MAIOR QUE 1,5KM
                if (m > 1500) isLongWay = true;
            }
        } catch (ParseException e) {
            isLongWay = false;
            Log.e(TAG, e.getMessage(), e);
        }


        if (isLongWay) {
            new PromptDialog(MapsActivity.this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                    .setAnimationEnable(true)
                    .setTitleText(getString(R.string.rota_longa))
                    .setContentText(getString(R.string.rota_experimental))
                    .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private void renderRectangules() {
        for (MarkerBean bean : markersList) {
            if (bean.getMarkerType().equals(MoovDisContants.MARKER_GOOD)) {
                continue;
            }
            LatLng[] coords = LatLongUtils.bboxByPoint(new LatLng(bean.getLocation().getCoordinates()[1], bean.getLocation().getCoordinates()[0]));
            mMap.addPolyline(new PolylineOptions()
                    .add(coords[0], coords[1])
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true));
        }

    }

    private void setDisplaySize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        if (windowmanager != null && windowmanager.getDefaultDisplay() != null) {
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            deviceWidth = displayMetrics.widthPixels;
        }
    }

    /**
     * Rest do add user
     */
    private class AlertsGetRequestTask extends AsyncTask<Boolean, Void, List<MarkerBean>> {
        @Override
        protected List<MarkerBean> doInBackground(Boolean... params) {
            try {
                Log.i("AlertsGetRequestTask", "Init");
                boolean forceUpdate = params.length > 0 && params[0];

                long now = Calendar.getInstance().getTimeInMillis();

                if (!forceUpdate) {
                    // POR QUESTOES DE PERFORMANCE DO SERVICO, EXECUTA SOMENTE SE O TEMPO ANTERIOR FOR MAIOR QUE 1 MINUTO
                    long diff = now - timeLastCallAlerts;
                    if (diff < MINUTE_MILISECONDS) {
                        Log.d("AlertsGetRequestTask", String.format("Jah executou recentemente %dms atrás", diff));
                        return null;
                    }

                }
                timeLastCallAlerts = now;
                //

                countTryLocalization = 0;
                // Aguarda a localizacao
                while (mLastLocation == null && countTryLocalization <= LIMIT_TRY_LOCALIZATION) {
                    Log.d("AlertsGetRequestTask", "Waiting for localization...");
                    countTryLocalization++;
                    Thread.sleep(3000);
                }

                if (countTryLocalization >= LIMIT_TRY_LOCALIZATION) {
                    return null;
                }

                final String url = MoovdisServices.getApiGetMarkersUrl(String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()));
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                MarkerBean[] retorno = restTemplate.getForObject(url, MarkerBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(List<MarkerBean> retorno) {
            if (retorno == null) {
                return;
            }

            // OBTEM TAMBEM A LISTA DE CALDADAS
            new SidewalkGetRequestTask().execute();
            // FIM

            // DESENHA OS MARCADORES
            markersList = retorno;
            final TextView txtAlertas = (TextView) findViewById(R.id.txt_alertas);
            if (retorno.size() == 1) {
                txtAlertas.setText(getString(R.string.alerta_next));
            } else {
                txtAlertas.setText(getString(R.string.alertas_next, retorno.size()));
            }
            plotAlertsOnMap(retorno);
            // FIM
        }
    }

    /**
     * Rest to Mark add General
     */
    private class MarkerAddRequestTask extends AsyncTask<MarkerBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(MarkerBean... params) {
            try {
                Log.d("MarkerAddRequestTask", params.toString());
                MoovDisUtil.showProgress(MapsActivity.this, getString(R.string.salvando_ocorrencia));
                final String url = MoovdisServices.getApiPostMarkerAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
            } catch (Exception e) {
                Log.e("MarkerAddRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            Toast.makeText(MapsActivity.this, getString(R.string.ocorrencia_sucesso), Toast.LENGTH_SHORT).show();

            // PEGA OS ALERTAS PROXIMOS
            timeLastCallAlerts = timeLastCallAlerts - MINUTE_MILISECONDS;
            new AlertsGetRequestTask().execute();
            if (retorno != null) {
                // ABRE O ALERTA PARA ADICIONAR COMENTARIOS
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                showNoticeDialog(retorno.get_id(), latLng);
            }
        }
    }

    /**
     * Rest to Mark add General
     */
    private class MarkerUpdateRequestTask extends AsyncTask<MarkerCommentsBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(MarkerCommentsBean... params) {
            try {
                Log.d("MarkerUpdateRequestTask", params[0].get_id());
                MoovDisUtil.showProgress(MapsActivity.this, getString(R.string.salvando_ocorrencia));
                final String url = MoovdisServices.getApiPostMarkerUpdateUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                retorno.setImage(params[0].getImage());
                return retorno;
            } catch (Exception e) {
                Log.e("MarkerUpdateRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            Toast.makeText(MapsActivity.this, getString(R.string.ocorrencia_update_sucesso), Toast.LENGTH_SHORT).show();

            // PEGA OS ALERTAS PROXIMOS
            timeLastCallAlerts = timeLastCallAlerts - MINUTE_MILISECONDS;
            new AlertsGetRequestTask().execute();

            // QUESTIONA SE DESEJA PUBLICAR NO FACEBOOK
            if (false) {
            //if (false && retorno.getImage() != null) {
                final Bitmap bitmap =  BitmapFactory.decodeByteArray(retorno.getImage(), 0, retorno.getImage().length);
                if (ShareDialog.canShow(ShareLinkContent.class) && !getShareDontAskAgain()) {

                    final CharSequence[] items = {getString(R.string.dont_ask_again)};
                    final boolean[] checkedItems = {false};

                    AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(getString(R.string.pub_marker_face))
                            .setCancelable(true)
                            .setMultiChoiceItems(items, checkedItems , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                                    checkedItems[index] = isChecked;
                                }
                            }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //just check checkedItems indexes value

                                    /*
                                    SharePhoto photo = new SharePhoto.Builder()
                                            .setBitmap(bitmap)
                                            .setUserGenerated(true)
                                            .setCaption(getString(R.string.i_did_route) + " #moovdis #acessibilidade")
                                            .build();
                                    SharePhotoContent content = new SharePhotoContent.Builder()
                                            .addPhoto(photo)
                                            .build();
                                    */

                                    ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                                            .putString("og:type", "fitness.course")
                                            .putString("og:title", "Sample Course")
                                            .putString("og:description", "This is a sample course.")
                                            .putInt("fitness:duration:value", 100)
                                            .putString("fitness:duration:units", "s")
                                            .putInt("fitness:distance:value", 12)
                                            .putString("fitness:distance:units", "km")
                                            .putInt("fitness:speed:value", 5)
                                            .putString("fitness:speed:units", "m/s")
                                            .build();
                                    ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                                            .setActionType("fitness.runs")
                                            .putObject("fitness:course", object)
                                            .build();
                                    ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                                            .setPreviewPropertyName("fitness:course")
                                            .setAction(action)
                                            .build();

                                    shareDialog.show(content);
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (checkedItems[0]) {
                                        setShareDontAskAgain();
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            }
        }
    }

    /**
     * Rest to Mark add General
     */
    private class RouteSaveRequestTask extends AsyncTask<RouteBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(RouteBean... params) {
            try {
                Log.d("RouteSaveRequestTask", params.toString());
                MoovDisUtil.showProgress(MapsActivity.this, getString(R.string.saving_route));
                final String url = MoovdisServices.getApiRouteCreateAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("RouteSaveRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            // Dont do Nothing, cause next action is google Maps
        }
    }

    /**
     * Rest to Mark add General
     */
    private class NotiticationSaveClickRequestTask extends AsyncTask<NotificationClickBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(NotificationClickBean... params) {
            try {
                Log.d("NotiticationSaveClick", params.toString());
                final String url = MoovdisServices.getPushClickUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("NotiticationSaveClick", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            // Dont do Nothing, cause next action is google Maps
        }
    }

    /**
     * Rest do add user
     */
    private class MapBoxDirectionsRequestTask extends AsyncTask<String, Void, MapBoxDirections> {
        @Override
        protected MapBoxDirections doInBackground(String... params) {
            try {
                Log.i("MapBoxDirections", "Init");
                final String url = params[0];
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                MapBoxDirections retorno = restTemplate.getForObject(url, MapBoxDirections.class);
                return retorno;
            } catch (Exception e) {
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.falha_route));
                Log.e("MapBoxDirections", e.getMessage(), e);
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(MapBoxDirections retorno) {
            if (retorno == null) {
                return;
            }
            //Log.i("MapBoxDirections", retorno.toString());

            int size = retorno.getRoutes().size();
            List<PolylineOptions>[] listPolygons = new ArrayList[size];
            for (int i = 0; i < listPolygons.length; i++) {
                listPolygons[i] = new ArrayList<>();
            }

            // Init lists
            initListsRoute(size);
            // show route elevation
            showRouteElevation();
            // init buttons navigations
            initRouteOptionsButtons(size);

            // Traversing through all the routes
            Log.d("onPostExecute", "Total de Rotas: " + size);
            MutableBoolean bestRoute = new MutableBoolean(false);
            List<List<LatLng>> listBadPaths = new ArrayList<>();

            for (int iRotas = 0; iRotas < size; iRotas++) {
                ArrayList<LatLng> points = new ArrayList<>();
                Set<InstrucaoRotaBean> instructions = new LinkedHashSet<>();

                // Fetching i-th route
                Route path = retorno.getRoutes().get(iRotas);
                Leg leg = path.getLegs().get(0);

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(0);
                totalDistance[iRotas] = String.format("%s m", nf.format(path.getDistance()));

                // Fetching all the points in i-th route
                final int stepSize = leg.getSteps().size();
                for (int jPath = 0; jPath < stepSize; jPath++) {
                    Step point = leg.getSteps().get(jPath);

                    List<LatLng> listPoints = LatLongUtils.decodePoly(point.getGeometry());
                    points.addAll(listPoints);

                    // PARA A PRIMEIRA ROTA, OBTEM DADOS PARA EXIBIR NO RESULTADO DA ROTA
                    if (jPath == FIRST_INDEX) {
                        instructions.add(new InstrucaoRotaBean(retorno.getWaypoints().get(0).getName(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                    instructions.add(new InstrucaoRotaBean(point.getManeuver().getInstruction(), String.format("%s m", nf.format(point.getDistance())), StringUtils.EMPTY));
                    if (jPath == (stepSize - 1)) {
                        instructions.add(new InstrucaoRotaBean(retorno.getWaypoints().get(retorno.getWaypoints().size() - 1).getName(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                }
                instructionsList[iRotas].add(instructions);

                // CHECK ALERTS IN ROUTE
                // CHECK ALERTS IN ROUTE
                //final int alertsBadOnPathCount = LatLongUtils.checkIntersectPoint(points, markersList);
                int alertsBadOnPathCount = 0;
                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        // CHECK ALERTS IN ROUTE
                        int finalIndex = ((i + 2) < points.size()) ? (i + 2) : points.size();
                        List<LatLng> subList = points.subList(i, finalIndex);
                        final int alertsOnPoint = LatLongUtils.checkIntersectPoint(subList, markersList);

                        if (alertsOnPoint > 0) {
                            alertsBadOnPathCount += alertsOnPoint;
                            listBadPaths.add(new ArrayList<>(subList));
                            Log.i("alertsOnPoint", "Rota:" + iRotas + " -> " + alertsOnPoint + " -> " + subList.toString());
                        }
                    }
                }
                final int hasStraint = LatLongUtils.checkIntersectPoint(points, extractStraitSidewalk(markersList));

                alertsOnPath[iRotas] = alertsBadOnPathCount;
                straitSidewalk[iRotas] = hasStraint > 0 && useWeelchair();

                listPolygons[iRotas].add(configureLineRoute(points, alertsBadOnPathCount, bestRoute));
            }

            drawLineRoutes(listPolygons);


            if (!listBadPaths.isEmpty()) {
                for (List<LatLng> subList : listBadPaths) {
                    // CHECK ALERTS IN ROUTE
                    ArrayList<PolylineOptions> lst = new ArrayList<>();
                    lst.add(configureLineRoute(subList, Integer.MIN_VALUE, bestRoute));
                    List<PolylineOptions>[] lstArray = new ArrayList[]{lst};
                    drawLineRoutes(lstArray);
                }
            }

            showRouteInstructions(instructionsList[0].get(0), instructionsList.length > TOTAL_UM, PRIMEIRA, alertsOnPath[FIRST_INDEX], straitSidewalk[FIRST_INDEX]);
            bottomResultRota.setState(BottomSheetBehavior.STATE_COLLAPSED);

            alertaRotaExtensa();

        }
    }

    /**
     * Rest do add user
     */
    private class GoogleDirectionsRequestTask extends AsyncTask<String, Void, GoogleDirections> {
        @Override
        protected GoogleDirections doInBackground(String... params) {
            try {
                Log.i("GoogleDirections", "Init");
                final String url = params[0];
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                GoogleDirections retorno = restTemplate.getForObject(url, GoogleDirections.class);
                return retorno;
            } catch (Exception e) {
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.falha_route));
                Log.e("GoogleDirections", e.getMessage(), e);
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(GoogleDirections retorno) {
            if (retorno == null) {
                return;
            }

            //Log.i("GoogleDirections", retorno.toString());

            final int size = retorno.getRoutes().size();
            List<PolylineOptions>[] listPolygons = new ArrayList[size];
            for (int i = 0; i < listPolygons.length; i++) {
                listPolygons[i] = new ArrayList<>();
            }
            // Init lists
            initListsRoute(size);
            // show route elevation
            showRouteElevation();
            // init buttons navigations
            initRouteOptionsButtons(size);


            // Traversing through all the routes
            Log.d("onPostExecute", "Total de Rotas: " + size);
            MutableBoolean bestRoute = new MutableBoolean(false);

            List<List<LatLng>> listBadPaths = new ArrayList<>();


            for (int iRotas = 0; iRotas < size; iRotas++) {
                ArrayList<LatLng> points = new ArrayList<>();
                points.add(origin);
                Set<InstrucaoRotaBean> instructions = new LinkedHashSet<>();

                // Fetching i-th route
                br.com.cardapia.company.acessmap.bean.google.Route path = retorno.getRoutes().get(iRotas);
                br.com.cardapia.company.acessmap.bean.google.Leg leg = path.getLegs().get(0);
                totalDistance[iRotas] = leg.getDistance().getText();

                // Fetching all the points in i-th route
                for (int jPath = 0; jPath < leg.getSteps().size(); jPath++) {
                    br.com.cardapia.company.acessmap.bean.google.Step point = leg.getSteps().get(jPath);
                    List<LatLng> listPoints = LatLongUtils.decodePoly(point.getPolyline().getPoints());
                    points.addAll(listPoints);

                    // PARA A PRIMEIRA ROTA, OBTEM DADOS PARA EXIBIR NO RESULTADO DA ROTA
                    if (jPath == FIRST_INDEX) {
                        instructions.add(new InstrucaoRotaBean(leg.getStartAddress(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                    instructions.add(new InstrucaoRotaBean(point.getHtmlInstructions(), point.getDistance().getText(), point.getManeuver()));

                    if (jPath == (leg.getSteps().size() - 1)) {
                        instructions.add(new InstrucaoRotaBean(leg.getEndAddress(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                }
                instructionsList[iRotas].add(instructions);

                // CHECK ALERTS IN ROUTE
                //final int alertsBadOnPathCount = LatLongUtils.checkIntersectPoint(points, markersList);
                int alertsBadOnPathCount = 0;
                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        // CHECK ALERTS IN ROUTE
                        int finalIndex = ((i + 2) < points.size()) ? (i + 2) : points.size();
                        List<LatLng> subList = points.subList(i, finalIndex);
                        final int alertsOnPoint = LatLongUtils.checkIntersectPoint(subList, markersList);

                        if (alertsOnPoint > 0) {
                            alertsBadOnPathCount += alertsOnPoint;
                            listBadPaths.add(new ArrayList<>(subList));
                            Log.i("alertsOnPoint", "Rota:" + iRotas + " -> " + alertsOnPoint + " -> " + subList.toString());
                        }
                    }
                }


                final int hasStraint = LatLongUtils.checkIntersectPoint(points, extractStraitSidewalk(markersList));

                alertsOnPath[iRotas] = alertsBadOnPathCount;
                straitSidewalk[iRotas] = hasStraint > 0 && useWeelchair();

                listPolygons[iRotas].add(configureLineRoute(points, alertsBadOnPathCount, bestRoute));
            }

            drawLineRoutes(listPolygons);


            if (!listBadPaths.isEmpty()) {
                for (List<LatLng> subList : listBadPaths) {
                    // CHECK ALERTS IN ROUTE
                    ArrayList<PolylineOptions> lst = new ArrayList<>();
                    lst.add(configureLineRoute(subList, Integer.MIN_VALUE, bestRoute));
                    List<PolylineOptions>[] lstArray = new ArrayList[]{lst};
                    drawLineRoutes(lstArray);
                }
            }


            if (instructionsList.length > 0 && instructionsList[0] != null && !instructionsList[0].isEmpty() && instructionsList[0].size() > 0)  {
                showRouteInstructions(instructionsList[0].get(0), instructionsList.length > TOTAL_UM, PRIMEIRA, alertsOnPath[FIRST_INDEX], straitSidewalk[FIRST_INDEX]);
            }
            bottomResultRota.setState(BottomSheetBehavior.STATE_COLLAPSED);

            alertaRotaExtensa();
        }
    }

    /**
     * Rest do add user
     */
    private class HereDirectionsRequestTask extends AsyncTask<Object, Void, HereDirections> {
        @Override
        protected HereDirections doInBackground(final Object... params) {
            try {
                Log.i("HereDirections", "Init");
                final String url = (String) params[0];

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
                bodyMap.add("avoidareas", (String) params[1]);

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyMap, headers);

                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

                //ResponseEntity<HereDirections> response =
                //        restTemplate.exchange(url, HttpMethod.POST,
                //                requestEntity, HereDirections.class);
                //return response.getBody();

                HereDirections retorno = restTemplate.postForObject(url, requestEntity, HereDirections.class);
                return retorno;
            } catch (Exception e) {
                //MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.falha_route));
                Log.e("HereDirections", e.getMessage(), e);
                if (e instanceof HttpClientErrorException) {
                    String error = ((HttpClientErrorException) e).getResponseBodyAsString();
                    Log.d("error_body_resp", error);
                }

                MoovDisUtil.hideProgress();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapsActivity.this, getString(R.string.try_again), Toast.LENGTH_LONG).show();
                        traceRoute((LatLng) params[3], (LatLng) params[4]);
                    }
                });
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(HereDirections response) {
            if (response == null) {
                return;
            }

            //Log.i("GoogleDirections", retorno.toString());
            Response retorno = response.getResponse();
            final int size = retorno.getRoute().size();
            List<PolylineOptions>[] listPolygons = new ArrayList[size];
            for (int i = 0; i < listPolygons.length; i++) {
                listPolygons[i] = new ArrayList<>();
            }
            // Init lists
            initListsRoute(size);
            // show route elevation
            showRouteElevation();
            // init buttons navigations
            initRouteOptionsButtons(size);


            // Traversing through all the routes
            Log.d("onPostExecute", "Total de Rotas: " + size);
            MutableBoolean bestRoute = new MutableBoolean(false);

            List<List<LatLng>> listBadPaths = new ArrayList<>();


            for (int iRotas = 0; iRotas < size; iRotas++) {
                ArrayList<LatLng> points = new ArrayList<>();
                points.add(origin);
                Set<InstrucaoRotaBean> instructions = new LinkedHashSet<>();

                // Fetching i-th route
                br.com.cardapia.company.acessmap.bean.here.Route path = retorno.getRoute().get(iRotas);
                br.com.cardapia.company.acessmap.bean.here.Leg leg = path.getLeg().get(0);

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(0);
                totalDistance[iRotas] = String.format("%s m", nf.format(leg.getLength()));

                // Fetching all the points in i-th route
                for (int jPath = 0; jPath < leg.getManeuver().size(); jPath++) {
                    Maneuver point = leg.getManeuver().get(jPath);

                    for (String s : point.getShape()) {
                        //LatLng poly = new LatLng(point.getPosition().getLatitude(), point.getPosition().getLongitude());
                        String[] coords = s.split(",");
                        LatLng poly = new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                        points.add(poly);
                    }


                    // PARA A PRIMEIRA ROTA, OBTEM DADOS PARA EXIBIR NO RESULTADO DA ROTA
                    if (jPath == FIRST_INDEX) {
                        instructions.add(new InstrucaoRotaBean(leg.getStart().getMappedRoadName(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                    instructions.add(new InstrucaoRotaBean(point.getInstruction(), StringUtils.EMPTY, StringUtils.EMPTY));

                    if (jPath == (leg.getManeuver().size() - 1)) {
                        instructions.add(new InstrucaoRotaBean(leg.getEnd().getMappedRoadName(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                }
                instructionsList[iRotas].add(instructions);

                // CHECK ALERTS IN ROUTE
                //final int alertsBadOnPathCount = LatLongUtils.checkIntersectPoint(points, markersList);
                int alertsBadOnPathCount = 0;
                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        // CHECK ALERTS IN ROUTE
                        int finalIndex = ((i + 2) < points.size()) ? (i + 2) : points.size();
                        List<LatLng> subList = points.subList(i, finalIndex);
                        final int alertsOnPoint = LatLongUtils.checkIntersectPoint(subList, markersList);

                        if (alertsOnPoint > 0) {
                            alertsBadOnPathCount += alertsOnPoint;
                            listBadPaths.add(new ArrayList<>(subList));
                            Log.i("alertsOnPoint", "Rota:" + iRotas + " -> " + alertsOnPoint + " -> " + subList.toString());
                        }
                    }
                }


                final int hasStraint = LatLongUtils.checkIntersectPoint(points, extractStraitSidewalk(markersList));

                alertsOnPath[iRotas] = alertsBadOnPathCount;
                straitSidewalk[iRotas] = hasStraint > 0 && useWeelchair();

                listPolygons[iRotas].add(configureLineRoute(points, alertsBadOnPathCount, bestRoute));
            }

            drawLineRoutes(listPolygons);


            if (!listBadPaths.isEmpty()) {
                for (List<LatLng> subList : listBadPaths) {
                    // CHECK ALERTS IN ROUTE
                    ArrayList<PolylineOptions> lst = new ArrayList<>();
                    lst.add(configureLineRoute(subList, Integer.MIN_VALUE, bestRoute));
                    List<PolylineOptions>[] lstArray = new ArrayList[]{lst};
                    drawLineRoutes(lstArray);
                }
            }


            showRouteInstructions(instructionsList[0].get(0), instructionsList.length > TOTAL_UM, PRIMEIRA, alertsOnPath[FIRST_INDEX], straitSidewalk[FIRST_INDEX]);
            bottomResultRota.setState(BottomSheetBehavior.STATE_COLLAPSED);

            //renderRectangules();

            alertaRotaExtensa();
        }
    }

    /**
     * Rest to Delete Marker
     */
    private class MarkerDeleteRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.d("MarkerDeleteRequestTask", params[0]);
                final String url = MoovdisServices.getApiDeleteMarkerUrl(params[0], YES);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                restTemplate.delete(url);
            } catch (Exception e) {
                Log.e("MarkerDeleteRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void retorno) {
            new AlertsGetRequestTask().execute(true);
        }
    }

    /**
     * Rest do add user
     */
    private class SidewalkGetRequestTask extends AsyncTask<Boolean, Void, List<SidewalkBean>> {
        @Override
        protected List<SidewalkBean> doInBackground(Boolean... params) {
            try {
                Log.i("SidewalkGetRequestTask", "Init");
                final String url = MoovdisServices.getApiGetSidewalkUrl(String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()));
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                SidewalkBean[] retorno = restTemplate.getForObject(url, SidewalkBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("SidewalkGetRequestTask", e.getMessage(), e);
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(List<SidewalkBean> retorno) {
            if (retorno == null) {
                return;
            }

            //markersList = retorno;
            plotSidewalksOnMap(retorno);
        }
    }



    /**
     * Rest do add user
     */
    private class OSMDirectionsRequestTask extends AsyncTask<Object, Void, OsmDirections> {
        @Override
        protected OsmDirections doInBackground(final Object... params) {
            try {
                Log.i("OsmDirections", "Init");
                final String url = (String) params[0];

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
                bodyMap.add("options", (String) params[1]);

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyMap, headers);

                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

                MoovDisUtil.showProgress(MapsActivity.this, getString(R.string.calc_route));

                OsmDirections retorno = restTemplate.postForObject(url, requestEntity, OsmDirections.class);

                return retorno;
            } catch (Exception e) {
                Log.e("OsmDirections", e.getMessage(), e);
                if (e instanceof HttpClientErrorException) {
                    String error = ((HttpClientErrorException) e).getResponseBodyAsString();
                    Log.d("error_body_resp", error);
                }

                // se for rota por cadeira de Rotas, tenta recalcular
                MoovDisUtil.hideProgress();
                if (YES.equalsIgnoreCase((String) params[2])) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, getString(R.string.no_weelchair_route), Toast.LENGTH_LONG).show();
                            traceRouteOSM((LatLng) params[3], (LatLng) params[4], false);
                        }
                    });
                } else {
                    //MoovDisUtil.showErrorMessage(MapsActivity.this, getString(R.string.ops), getString(R.string.falha_route));
                    // Try to Route with another provider
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, getString(R.string.try_again), Toast.LENGTH_LONG).show();
                            traceRouteHere((LatLng) params[3], (LatLng) params[4]);
                        }
                    });
                }

            } finally {
                MoovDisUtil.hideProgress();
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(OsmDirections retorno) {
            if (retorno == null) {
                return;
            }

            final int size = retorno.getRoutes().size();
            List<PolylineOptions>[] listPolygons = new ArrayList[size];
            for (int i = 0; i < listPolygons.length; i++) {
                listPolygons[i] = new ArrayList<>();
            }
            // Init lists
            initListsRoute(size);
            // show route elevation
            showRouteElevation();
            // init buttons navigations
            initRouteOptionsButtons(size);


            // Traversing through all the routes
            Log.d("onPostExecute", "Total de Rotas: " + size);
            MutableBoolean bestRoute = new MutableBoolean(false);

            List<List<LatLng>> listBadPaths = new ArrayList<>();


            for (int iRotas = 0; iRotas < size; iRotas++) {
                ArrayList<LatLng> points = new ArrayList<>();
                points.add(origin);
                Set<InstrucaoRotaBean> instructions = new LinkedHashSet<>();

                // Fetching i-th route
                br.com.cardapia.company.acessmap.bean.osm.Route path = retorno.getRoutes().get(iRotas);
                Segment leg = path.getSegments().get(0);
                totalDistance[iRotas] = String.format(Locale.getDefault(), "%.1fm", leg.getDistance());


                if (path.getGeometry() != null
                        && path.getGeometry().getCoordinates() != null
                        && !path.getGeometry().getCoordinates().isEmpty()) {
                    for (List<Double> l : path.getGeometry().getCoordinates()) {
                        points.add(new LatLng(l.get(1), l.get(0)));
                    }
                }



                // Fetching all the points in i-th route
                String finalName = StringUtils.EMPTY;
                for (int jPath = 0; jPath < leg.getSteps().size(); jPath++) {
                    br.com.cardapia.company.acessmap.bean.osm.Step point = leg.getSteps().get(jPath);

                    // PARA A PRIMEIRA ROTA, OBTEM DADOS PARA EXIBIR NO RESULTADO DA ROTA
                    if (jPath == FIRST_INDEX) {
                        instructions.add(new InstrucaoRotaBean(point.getName(), StringUtils.EMPTY, StringUtils.EMPTY));
                    }

                    instructions.add(new InstrucaoRotaBean(point.getInstruction(), String.format("%sm", point.getDistance()), StringUtils.EMPTY));

                    if (jPath == (leg.getSteps().size() - 2)) {
                        finalName = point.getName();
                    }
                    if (jPath == (leg.getSteps().size() - 1)) {
                        instructions.add(new InstrucaoRotaBean(finalName, StringUtils.EMPTY, StringUtils.EMPTY));
                    }
                }
                instructionsList[iRotas].add(instructions);

                // CHECK ALERTS IN ROUTE
                //final int alertsBadOnPathCount = LatLongUtils.checkIntersectPoint(points, markersList);
                int alertsBadOnPathCount = 0;
                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        // CHECK ALERTS IN ROUTE
                        int finalIndex = ((i + 2) < points.size()) ? (i + 2) : points.size();
                        List<LatLng> subList = points.subList(i, finalIndex);
                        final int alertsOnPoint = LatLongUtils.checkIntersectPoint(subList, markersList);

                        if (alertsOnPoint > 0) {
                            alertsBadOnPathCount += alertsOnPoint;
                            listBadPaths.add(new ArrayList<>(subList));
                            Log.i("alertsOnPoint", "Rota:" + iRotas + " -> " + alertsOnPoint + " -> " + subList.toString());
                        }
                    }
                }


                final int hasStraint = LatLongUtils.checkIntersectPoint(points, extractStraitSidewalk(markersList));

                alertsOnPath[iRotas] = alertsBadOnPathCount;
                straitSidewalk[iRotas] = hasStraint > 0 && useWeelchair();

                listPolygons[iRotas].add(configureLineRoute(points, alertsBadOnPathCount, bestRoute));
            }

            drawLineRoutes(listPolygons);


            if (!listBadPaths.isEmpty()) {
                for (List<LatLng> subList : listBadPaths) {
                    // CHECK ALERTS IN ROUTE
                    ArrayList<PolylineOptions> lst = new ArrayList<>();
                    lst.add(configureLineRoute(subList, Integer.MIN_VALUE, bestRoute));
                    List<PolylineOptions>[] lstArray = new ArrayList[]{lst};
                    drawLineRoutes(lstArray);
                }
            }


            showRouteInstructions(instructionsList[0].get(0), instructionsList.length > TOTAL_UM, PRIMEIRA, alertsOnPath[FIRST_INDEX], straitSidewalk[FIRST_INDEX]);
            bottomResultRota.setState(BottomSheetBehavior.STATE_COLLAPSED);

            alertaRotaExtensa();
        }
    }

}


