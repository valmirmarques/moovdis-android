package br.com.cardapia.company.acessmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.bean.VenueInfoBean;
import br.com.cardapia.company.acessmap.bean.VenueRateAvgBean;
import br.com.cardapia.company.acessmap.bean.foursquare.Venue;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class PlacesDetailsActivity extends AppCompatActivity implements
        MoovDisContants, OnMapReadyCallback {


    private GoogleMap mMap;
    private Venue venueSelected;
    private RatingBar ratingBar;
    private float GOOD_RATING = 0.8f;
    private float MEDIUM_RATING = 0.5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_detail);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        // Enable Location Services
        MoovDisUtil.checkLocationPermission(this);
        MoovDisUtil.enableLocationService(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.img_place_detail);
        mapFragment.getMapAsync(this);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar = findViewById(R.id.ratingBarPlaces);

        if (this.getIntent().getExtras().containsKey(VENUE)) {
            Venue venue = (Venue) this.getIntent().getExtras().get(VENUE);
            actionBar.setTitle(venue.getName());
            this.venueSelected = venue;
            setValues();

            if (venue.getVenueInfo() != null && venue.getVenueInfo().getUser() != null) {
                new UserGetByIdRequestTask().execute(venue.getVenueInfo().getUser().get_id());
                new VenueRateByIdRequestTask().execute(venue.getVenueInfo().get_id());
            }
        } else {
            finish();
        }

        Button rateNow = findViewById(R.id.button_detail_place_rate_now);
        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });


    }


    /**
     *
     */
    private void setValues() {
        final Venue venue = this.venueSelected;

        CircleImageView imgAcessivel = findViewById(R.id.is_place_accessible_detail);
        TextView txtAcesivel = findViewById(R.id.txt_place_acessivel);
        if (venue.getVenueInfo() != null) {
            VenueInfoBean info = venue.getVenueInfo();
            switch (info.getLugarAcessivel()) {
                case YES:
                    imgAcessivel.setImageDrawable(getDrawable(R.drawable.if_accessible_true));
                    imgAcessivel.setContentDescription(getString(R.string.place_acessible));
                    txtAcesivel.setText(getString(R.string.place_acessible));
                    break;
                case MID:
                    imgAcessivel.setImageDrawable(getDrawable(R.drawable.if_accessible_mid));
                    imgAcessivel.setContentDescription(getString(R.string.place_parcially_acessible));
                    txtAcesivel.setText(getString(R.string.place_parcially_acessible));
                    break;
                case NO:
                    imgAcessivel.setImageDrawable(getDrawable(R.drawable.if_accessible_false));
                    imgAcessivel.setContentDescription(getString(R.string.place_not_acessible));
                    txtAcesivel.setText(getString(R.string.place_not_acessible));
                    break;
            }

            LinearLayout layoutParking = findViewById(R.id.layout_place_detail_parking);
            LinearLayout layoutCirculacao = findViewById(R.id.layout_place_detail_circulacao);
            LinearLayout layoutEntrada = findViewById(R.id.layout_place_detail_entrada);
            LinearLayout layoutMapaTatil = findViewById(R.id.layout_place_detail_mapa_tatil);
            LinearLayout layoutWc = findViewById(R.id.layout_place_detail_wc);
            LinearLayout layoutWcFamily = findViewById(R.id.layout_place_detail_wc_family);
            LinearLayout layoutMoveis = findViewById(R.id.layout_place_detail_mesa);

            layoutParking.setVisibility(info.isHasEstacionamento() ? View.VISIBLE : View.GONE );
            layoutCirculacao.setVisibility(info.isHasCirculacaoInterna() ? View.VISIBLE : View.GONE );
            layoutEntrada.setVisibility(info.isHasEntradaBoa() ? View.VISIBLE : View.GONE );
            layoutMapaTatil.setVisibility(info.isHasMapaTatil() ? View.VISIBLE : View.GONE );
            layoutWc.setVisibility(info.isHasWc() ? View.VISIBLE : View.GONE );
            layoutWcFamily.setVisibility(info.isHasFraudario() ? View.VISIBLE : View.GONE );
            layoutMoveis.setVisibility(info.isHasMesa() ? View.VISIBLE : View.GONE);

            if (StringUtils.isNotBlank(info.getComment())) {
                TextView txtComments = findViewById(R.id.place_detail_comment);
                txtComments.setText(info.getComment());
            }
        } else {
            ScrollView scrollView = findViewById(R.id.scroll_places_detail);
            scrollView.setVisibility(View.GONE);

            RelativeLayout linearRate = findViewById(R.id.layout_place_detail_rate_now);
            linearRate.setVisibility(View.VISIBLE);
        }

        TextView addrTxt = findViewById(R.id.detail_place_addr);
        TextView categoriaTxt = findViewById(R.id.detail_place_categoria);
        TextView distanceTxt = findViewById(R.id.detail_place_distance);

        String addr = venue.getLocation().getAddress();
        if (StringUtils.isBlank(addr) && venue.getLocation().getFormattedAddress() != null && !venue.getLocation().getFormattedAddress().isEmpty()) {
            addr = venue.getLocation().getFormattedAddress().get(0);
            if (venue.getLocation().getFormattedAddress().size() > 1) {
                addr += " " + venue.getLocation().getFormattedAddress().get(1);
            }
        }
        String categoria = StringUtils.EMPTY;
        if (venue.getCategories() != null && !venue.getCategories().isEmpty()) {
            categoria = venue.getCategories().get(0).getName();
        }
        addrTxt.setText(addr);
        categoriaTxt.setText(categoria);
        distanceTxt.setText(getString(R.string.venue_distance, venue.getLocation().getDistance()));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //
                if (venueSelected != null && venueSelected.getLocation() != null) {
                    Intent intent = new Intent(PlacesDetailsActivity.this, MapsActivity.class);
                    Location location = new Location(venueSelected.getName());
                    location.setLatitude(venueSelected.getLocation().getLat());
                    location.setLongitude(venueSelected.getLocation().getLng());
                    location.setTime(new Date().getTime()); //Set time as current Date

                    intent.putExtra(CURRENT_LOCATION, location);
                    startActivity(intent);
                }
            }
        });

        if (venueSelected != null && this.venueSelected.getLocation() != null) {
            final LatLng venueLocation = new LatLng(this.venueSelected.getLocation().getLat(),
                    this.venueSelected.getLocation().getLng());

            mMap.addMarker(new MarkerOptions()
                    .position(venueLocation)
                    .flat(true)
                    .title(this.venueSelected.getName()));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(venueLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_15));
        }

    }


    /**
     * Rest do get user
     */
    private class UserGetByIdRequestTask extends AsyncTask<String, Void, UsuarioBean> {
        @Override
        protected UsuarioBean doInBackground(String... params) {
            try {
                Log.d("UserGetByIdRequestTask", Arrays.toString(params));
                final String url = MoovdisServices.getApiGetUserUrl(null) + "/" + params[0];
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.getForObject(url, UsuarioBean.class);
            } catch (Exception e) {
                Log.e("UserGetByIdRequestTask", e.getMessage(), e);
                if (e.getCause() instanceof UnknownHostException) {
                    MoovDisUtil.showErrorMessage(PlacesDetailsActivity.this, getString(R.string.ops), getString(R.string.failed_connection));
                } else {
                    MoovDisUtil.showErrorMessage(PlacesDetailsActivity.this, getString(R.string.ops), e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(UsuarioBean retorno) {
            if (retorno == null) {
                return;
            }

            CircleImageView imgUser = findViewById(R.id.img_rate_for);
            TextView txtRateFor = findViewById(R.id.lbl_rate_for);

            // SETA A IMAGEM DO PROFILE E DO MENU
            if (retorno.getImage() != null) {
                Bitmap bMap = BitmapFactory.decodeByteArray(retorno.getImage(), 0, retorno.getImage().length);
                imgUser.setImageBitmap(bMap);
            } else {
                imgUser.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
            }
            txtRateFor.setText(getString(R.string.rate_for, retorno.getName()));

        }
    }

    /**
     * Rest do get user
     */
    private class VenueRateByIdRequestTask extends AsyncTask<String, Void, VenueRateAvgBean> {
        @Override
        protected VenueRateAvgBean doInBackground(String... params) {
            try {
                Log.d("VenueRateById", Arrays.toString(params));
                final String url = MoovdisServices.getApiRateByVenueIdUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.getForObject(url, VenueRateAvgBean.class);
            } catch (Exception e) {
                Log.e("VenueRateById", e.getMessage(), e);
                if (e.getCause() instanceof UnknownHostException) {
                    MoovDisUtil.showErrorMessage(PlacesDetailsActivity.this, getString(R.string.ops), getString(R.string.failed_connection));
                } else {
                    MoovDisUtil.showErrorMessage(PlacesDetailsActivity.this, getString(R.string.ops), e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(VenueRateAvgBean retorno) {
            if (retorno == null) {
                return;
            }
            Log.i("Rating", ToStringBuilder.reflectionToString(retorno));
            ratingBar.setRating(retorno.getRate());

            LayerDrawable layers = (LayerDrawable) ratingBar.getProgressDrawable();
            DrawableCompat.setTint(layers.getDrawable(0), 0x33000000); // The background tint
            DrawableCompat.setTint(layers.getDrawable(1), 0x00000000); // The gap tint (Transparent in this case so the gap doesnt seem darker than the background)
            if (retorno.getRate() >= GOOD_RATING) {
                DrawableCompat.setTint(layers.getDrawable(2), Color.GREEN); // The star tint
            } else if (retorno.getRate() >= MEDIUM_RATING)  {
                DrawableCompat.setTint(layers.getDrawable(2), Color.YELLOW); // The star tint
            } else {
                DrawableCompat.setTint(layers.getDrawable(2), Color.RED); // The star tint
            }
        }
    }

}
