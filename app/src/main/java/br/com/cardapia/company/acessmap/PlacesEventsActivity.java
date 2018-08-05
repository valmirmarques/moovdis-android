package br.com.cardapia.company.acessmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vijay.jsonwizard.activities.JsonFormActivity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import br.com.cardapia.company.acessmap.adapter.PlacesLineAdapter;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.VenueInfoBean;
import br.com.cardapia.company.acessmap.bean.foursquare.Venue;
import br.com.cardapia.company.acessmap.bean.foursquare.VenuesService;
import br.com.cardapia.company.acessmap.services.FoursquareService;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class PlacesEventsActivity extends AppCompatActivity implements
        MoovDisContants,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        PlacesLineAdapter.CallbackInterface,
        LocationListener {

    private RecyclerView mRecyclerView;
    private PlacesLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlaceAutocompleteFragment autocompleteFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private String idVenue;
    private String venueName;
    private SharedPreferences sharedPref;
    private String idUsuario;

    private ImageView buttonExpand;
    private TextView txtCategorySelected;
    private LinearLayout gridCategories;

    private String categorySelected = StringUtils.EMPTY;


    private LatLng ll = null;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {
                for (Location location : locationResult.getLocations()) {
                    Log.i("onLocationResult", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    //Place current location marker
                    ll = new LatLng(location.getLatitude(), location.getLongitude());

                    disableLocationServices();
                }
            }
        }
    };
    private LatLng defaultll = new LatLng(-25.4947401, -49.4298864);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        ButterKnife.bind(this);

        // Enable Location Services
        MoovDisUtil.checkLocationPermission(this);
        MoovDisUtil.enableLocationService(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        mRecyclerView = findViewById(R.id.recycler_view_places);
        mRecyclerView.setHasFixedSize(false);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // AUTO COMPLETE
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment_places);
        if (autocompleteFragment != null) {

            autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // example : way to access view from PlaceAutoCompleteFragment
                            // HABILITA A LOCALIZACAO
                            autocompleteFragment.setText(StringUtils.EMPTY);
                            view.setVisibility(View.GONE);
                        }
                    });

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i("onPlaceSelected", "Place: " + place.getName());
                    ll = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    new VenuesGetRequestTask().execute(categorySelected);
                }

                @Override
                public void onError(Status status) {
                    Log.i("onError", "Ocorreu um erro: " + status);
                }
            });
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mFusedLocationClient != null) {
                requestLocationUpdates();
            } else {
                buildGoogleApiClient();
            }
        }

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // SE JA EXISTE UM USUARIO CADASTRADO
        if (sharedPref.contains(ID_USUARIO)) {
            // OBTEM OS DADOS DO USUARIO NO BANCO DE DADOS
            this.idUsuario = sharedPref.getString(ID_USUARIO, StringUtils.EMPTY);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            ll = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });

        txtCategorySelected = findViewById(R.id.places_text_category_selected);
        buttonExpand = findViewById(R.id.place_category_expand);
        gridCategories = findViewById(R.id.places_filter_category);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonExpand.setVisibility(View.GONE);
                txtCategorySelected.setText(getString(R.string.category_choose));
                gridCategories.setVisibility(View.VISIBLE);
            }
        });

        // RECUPERA CALENDARIO DE EVENTOS
        new VenuesGetRequestTask().execute(categorySelected);

    }

    /**
     * Setup Recycler
     */
    private void setupRecycler(final Object listVenues, List<VenueInfoBean> listVenueInfos) {
        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.

        // AJUSTA OS DETALHES DOS ESTABELECIMENTOS
        List<Venue> venues = (List<Venue>) listVenues;
        if (!venues.isEmpty()) {
            // SORT LIST BY DISTANCE
            Collections.sort(venues);
            if (listVenueInfos != null) {
                for (Venue venue : venues) {
                    for (VenueInfoBean venueInfo : listVenueInfos) {
                        if (venueInfo.get_id().equals(venue.getId())) {
                            venue.setVenueInfo(venueInfo);
                            break;
                        }
                    }
                }
            }
        }

        mAdapter = new PlacesLineAdapter(this, new ArrayList<>(venues));
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("PlacesActivity", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ll = new LatLng(location.getLatitude(), location.getLongitude());
        disableLocationServices();
    }

    /**
     * Solicita a Atualizacao de Localizacao
     */
    private void requestLocationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * @param idVenue
     */
    @Override
    public void onHandleSelection(String idVenue, String venueName) {
        this.idVenue = idVenue;
        this.venueName = venueName;
        Intent intent = new Intent(this, JsonFormActivity.class);
        String json = readRawFile(R.raw.wizard_rate_venue);
        if (!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase(LANG_PORTUGUESE)) {
            json = readRawFile(R.raw.wizard_rate_venue_en);
        }
        intent.putExtra(JSON, json);
        startActivityForResult(intent, REQUEST_CODE_GET_JSON);
    }

    /**
     * @param venue
     */
    @Override
    public void onPlaceDetailSelection(final Venue venue) {
        this.idVenue = venue.getId();
        this.venueName = venue.getName();
        Intent intent = new Intent(this, PlacesDetailsActivity.class);
        intent.putExtra(VENUE, venue);
        startActivityForResult(intent, REQUEST_DETAIL_PLACE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String json = data.getStringExtra(JSON);
            Gson gson = new Gson();
            HashMap<String, Object> son = gson.fromJson(json, HashMap.class);

            if (son != null) {
                ratioVenue(son);
            }
        }
        if (requestCode == REQUEST_DETAIL_PLACE && resultCode == RESULT_OK) {
            onHandleSelection(this.idVenue, this.venueName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Filtro Categoria
    public void filterCategory(final View view) {
        String category = (String) view.getTag();
        txtCategorySelected.setText(view.getContentDescription());
        buttonExpand.setVisibility(View.VISIBLE);
        gridCategories.setVisibility(View.GONE);
        this.categorySelected = category;
        // RECUPERA CALENDARIO DE EVENTOS
        new VenuesGetRequestTask().execute(categorySelected);
    }

    /**
     * @param map
     */
    private void ratioVenue(final Map map) {

        Map<String, String> rate = new HashMap<>();
        rate.put("_id", idVenue);
        rate.put("userId", idUsuario);
        rate.put("venueName", venueName);

        if (map.containsKey(STEP_1)) {
            ArrayList valores = (ArrayList) (((Map) map.get("step1")).get("fields"));
            for (Object o : valores) {
                LinkedTreeMap treeMap = (LinkedTreeMap) o;
                rate.put((String) treeMap.get("key"), (String) treeMap.get("value"));
            }
        }
        if (map.containsKey(STEP_2)) {
            ArrayList valores = (ArrayList) (((Map) map.get("step2")).get("fields"));
            for (Object o : valores) {
                LinkedTreeMap treeMap = (LinkedTreeMap) o;
                rate.put((String) treeMap.get("key"), (String) treeMap.get("value"));
            }
        }
        if (map.containsKey(STEP_3)) {
            ArrayList valores = (ArrayList) (((Map) map.get("step3")).get("fields"));
            for (Object o : valores) {
                LinkedTreeMap treeMap = (LinkedTreeMap) o;
                rate.put((String) treeMap.get("key"), (String) treeMap.get("value"));
            }
        }
        if (map.containsKey(STEP_4)) {
            ArrayList valores = (ArrayList) (((Map) map.get("step4")).get("fields"));
            for (Object o : valores) {
                LinkedTreeMap treeMap = (LinkedTreeMap) o;
                rate.put((String) treeMap.get("key"), (String) treeMap.get("value"));
            }
        }

        new RateVenueRequestTask().execute(rate);

    }


    /**
     * @param rawFile
     * @return
     */
    private String readRawFile(final int rawFile) {
        StringBuilder data = new StringBuilder();
        InputStream is = getResources().openRawResource(rawFile);
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }


    /**
     * Rest do add user
     */
    private class VenuesGetRequestTask extends AsyncTask<String, Void, VenuesService> {
        @Override
        protected VenuesService doInBackground(String... params) {
            try {
                Log.i("VenuesGetRequestTask", "Init");
                MoovDisUtil.showProgress(PlacesEventsActivity.this, getString(R.string.rec_info));

                // AGUARDA A LOCALIZACAO DO USUARIO
                int countTry = 0;
                final int limitTry = 10;
                while (ll == null) {
                    Log.i("VenuesGetRequestTask", "Waiting for localization...");
                    Thread.sleep(1000);
                    countTry++;
                    if (countTry >= limitTry) {
                        break;
                    }
                }

                if (ll == null) ll = defaultll;
                Log.d("location", ll.toString());
                String category = StringUtils.EMPTY;
                if (params.length > 0) {
                    category = params[0];
                }
                final String url = FoursquareService.getSearchVenuesApiUrl(ll,
                        getString(R.string.fsq_client_id),
                        getString(R.string.fsq_client_secret),
                        category);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                VenuesService retorno = restTemplate.getForObject(url, VenuesService.class);
                return retorno;
            } catch (Exception e) {
                Log.e("VenuesGetRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(PlacesEventsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(VenuesService retorno) {
            if (retorno == null) {
                return;
            }
            if (retorno.getResponse() != null
                    && retorno.getResponse().getVenues() != null
                    && !retorno.getResponse().getVenues().isEmpty()) {

                // SEARCH FOR INFORMATIONS STORED ON LOCAL DATABASE
                List<Venue> venues = retorno.getResponse().getVenues();
                List<String> idVenues = new ArrayList<>();
                for (Venue venue : venues) {
                    idVenues.add(venue.getId());
                }
                new VenueInfoRequestTask().execute(idVenues, venues);
                //setupRecycler(retorno.getResponse().getVenues());
            } else {
                List<Venue> listaVazia = new ArrayList<>();
                setupRecycler((Object) listaVazia, null);
                MoovDisUtil.showInfoMessage(PlacesEventsActivity.this, getString(R.string.action_places), getString(R.string.info_not_found));
            }

        }
    }


    /**
     * Rest to rate venue
     */
    private class RateVenueRequestTask extends AsyncTask<Map<String, String>, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(Map<String, String>... params) {
            try {
                Log.d("RateVenueRequestTask", params.toString());
                MoovDisUtil.showProgress(PlacesEventsActivity.this, getString(R.string.salvando_ocorrencia));
                final String url = MoovdisServices.getApiRateVenueUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("RateVenueRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(PlacesEventsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }

            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            if (retorno == null) {
                return;
            }

            // RECUPERA CALENDARIO DE EVENTOS
            new VenuesGetRequestTask().execute(categorySelected);

        }

    }


    /**
     * Rest to get Venue Info
     */
    private class VenueInfoRequestTask extends AsyncTask<Object, Void, List<VenueInfoBean>> {
        @Override
        protected List<VenueInfoBean> doInBackground(final Object... params) {
            List<VenueInfoBean> result = null;
            final AtomicReference<List<VenueInfoBean>> var = new AtomicReference<>(result);
            try {
                Log.d("VenueInfoRequestTask", params.toString());
                final String url = MoovdisServices.getApiRateSearchUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                Map<String, Object> post = new HashMap<>();
                post.put("_idString", params[0]);
                VenueInfoBean[] retorno = restTemplate.postForObject(url, post, VenueInfoBean[].class);
                result = Arrays.asList(retorno);
                var.set(result);
            } catch (Exception e) {
                Log.e("VenueInfoRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(PlacesEventsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRecycler(params[1], var.get());
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<VenueInfoBean> retorno) {
            if (retorno == null) {
                return;
            }

        }

    }


}
