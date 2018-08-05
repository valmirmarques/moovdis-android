package br.com.cardapia.company.acessmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import br.com.cardapia.company.acessmap.adapter.LastMarkersLineAdapter;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.bean.ScoreBoardIndividualBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements MoovDisContants, BottomSheetFragmentMain.MarkersListener {

    private static boolean FIRST_ACCESS = true;
    private static TextView TXT_NOME_MENU;
    private CircleImageView ivThumbnailPhotoMenu;
    private ProgressBar progressBar;

    private boolean busqueiLastMarkers = false;


    private RecyclerView mRecyclerView;
    private LastMarkersLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        FacebookSdk.setApplicationName(getString(R.string.app_name));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // MENU de Navegacaos
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                if (item.getItemId() == R.id.nav_maps) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_placar) {
                    Intent intent = new Intent(MainActivity.this, ScoreboardActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_virtual_assit) {
                    Intent intent = new Intent(MainActivity.this, VirtualAssistantActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_feeds) {
                    Intent intent = new Intent(MainActivity.this, TwitterFeedsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_events) {
                    Intent intent = new Intent(MainActivity.this, CalendarEventsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_rotas) {
                    Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_pref) {
                    Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_invite_friends) {
                    Intent intent = new Intent(MainActivity.this, InviteFriendsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_about) {
                    Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_places) {
                    Intent intent = new Intent(MainActivity.this, PlacesEventsActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        // TOPO DO MENU
        View header = navigationView.getHeaderView(0);
        ivThumbnailPhotoMenu = (CircleImageView) header.findViewById(R.id.imgProfileMenu);
        TXT_NOME_MENU = (TextView) header.findViewById(R.id.txtNomeMenu);
        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        // carrega as ultimas marcacoes

        mRecyclerView = findViewById(R.id.recycler_view_last_markers);
        mRecyclerView.setHasFixedSize(false);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // SE JA EXISTE UM USUARIO CADASTRADO
        if (!sharedPref.contains(ID_USUARIO)) {
            // FACA O CADASTRO
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.first_access), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
            startActivity(intent);
        } else {
            new AlertsGetRequestTask().execute();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Direct to Maps
     */
    private void directToMaps() {
        if (FIRST_ACCESS) {
            Log.d("directToMaps", "Primeiro acesso com usuario Logado");
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
            FIRST_ACCESS = false;
        }
    }

    /**
     * @param v view
     */
    public void goToView(View v) {
        Log.d("goToView", "Go To View...");
        switch (v.getId()) {
            case R.id.lbl_welcome_points:
            case R.id.img_welcome_coin: {
                Intent intent = new Intent(MainActivity.this, ScoreboardActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lbl_welcome: {
                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_mapa: {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_places: {
                Intent intent = new Intent(MainActivity.this, PlacesEventsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_assist: {
                Intent intent = new Intent(MainActivity.this, VirtualAssistantActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_feeds: {
                Intent intent = new Intent(MainActivity.this, TwitterFeedsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_ranking: {
                Intent intent = new Intent(MainActivity.this, ScoreboardActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_routes: {
                Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_invite: {
                Intent intent = new Intent(MainActivity.this, InviteFriendsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.card_events: {
                Intent intent = new Intent(MainActivity.this, CalendarEventsActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                Log.d("goToView", "No Option Selected");

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // SE JA EXISTE UM USUARIO CADASTRADO
        if (sharedPref.contains(ID_USUARIO)) {
            // OBTEM OS DADOS DO USUARIO NO BANCO DE DADOS
            String idUsuario = sharedPref.getString(ID_USUARIO, StringUtils.EMPTY);
            new ScoreboardIndividualRequestTask().execute(idUsuario);
            new UserGetByIdRequestTask().execute(idUsuario);

            if (!busqueiLastMarkers) {
                progressBar.setVisibility(View.VISIBLE);
                new AlertsGetRequestTask().execute();
            }
        }
    }

    /**
     * Setup Recycler
     */
    private void setupRecycler(final List<MarkerBean> markers) {

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LastMarkersLineAdapter(new ArrayList<>(markers));
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * showing bottom sheet dialog fragment
     * same layout is used in both dialog and dialog fragment
     */
    public void showBottomSheetDialogFragment(final View view) {
        TextView description = view.findViewById(R.id.cell_markes_descriptions);
        MarkerBean bean = (MarkerBean) description.getTag();
        BottomSheetFragmentMain bottomSheetFragment = new BottomSheetFragmentMain();
        bottomSheetFragment.setMarkerBean(bean);
        bottomSheetFragment.setCallback(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getApplicationContext(), data);
                Log.d("Place", ToStringBuilder.reflectionToString(place, ToStringStyle.MULTI_LINE_STYLE));
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void markerNeedsUpdate() {
        progressBar.setVisibility(View.VISIBLE);
        new AlertsGetRequestTask().execute();
    }

    /**
     * Rest to get Scoreboard Individual
     */
    private class ScoreboardIndividualRequestTask extends AsyncTask<String, Void, ScoreBoardIndividualBean> {
        @Override
        protected ScoreBoardIndividualBean doInBackground(String... params) {
            try {
                Log.d("ScoreboardIndividual", Arrays.toString(params));
                //MoovDisUtil.showProgress(MainActivity.this, "Recuperando meu placar");
                final String url = MoovdisServices.getApiGetRankingUserUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                ScoreBoardIndividualBean[] retorno = restTemplate.getForObject(url, ScoreBoardIndividualBean[].class);
                if (retorno.length > 0) {
                    return retorno[0];
                }
            } catch (Exception e) {
                Log.e("ScoreboardIndividual", e.getMessage(), e);
                if (e.getCause() instanceof UnknownHostException) {
                    MoovDisUtil.showErrorMessage(MainActivity.this, getString(R.string.ops), getString(R.string.failed_connection));
                } else {
                    MoovDisUtil.showErrorMessage(MainActivity.this, getString(R.string.ops), e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ScoreBoardIndividualBean retorno) {
            if (retorno == null) {
                return;
            }
            TextView txtWelcomePoints = (TextView) findViewById(R.id.lbl_welcome_points);
            txtWelcomePoints.setText(String.valueOf(retorno.getTotalPoints()));
            txtWelcomePoints.setContentDescription(getString(R.string.quantidade_pontos, String.valueOf(retorno.getTotalPoints())));
        }
    }

    /**
     * Rest do add user
     */
    private class UserGetByIdRequestTask extends AsyncTask<String, Void, UsuarioBean> {
        @Override
        protected UsuarioBean doInBackground(String... params) {
            try {
                Log.d("UserGetByIdRequestTask", Arrays.toString(params));
                //MoovDisUtil.showProgress(MainActivity.this, "Recuperando informações do usuário");
                final String url = MoovdisServices.getApiGetUserUrl(null) + "/" + params[0];
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.getForObject(url, UsuarioBean.class);
            } catch (Exception e) {
                Log.e("UserGetByIdRequestTask", e.getMessage(), e);
                if (e.getCause() instanceof UnknownHostException) {
                    MoovDisUtil.showErrorMessage(MainActivity.this, getString(R.string.ops), getString(R.string.failed_connection));
                } else {
                    MoovDisUtil.showErrorMessage(MainActivity.this, getString(R.string.ops), e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(UsuarioBean retorno) {
            if (retorno == null) {
                return;
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(NAME_USER, retorno.getName());
            editor.putInt(WEELCHAIR_WIDTH, retorno.getWeelchairWidht());
            editor.putBoolean(USE_WEELCHAIR, retorno.isWeelchair());

            TXT_NOME_MENU.setText(retorno.getName());
            TextView txtWelcome = (TextView) findViewById(R.id.lbl_welcome);
            txtWelcome.setText(getString(R.string.welcome, retorno.getName().split("\\s")[0]));

            // SETA A IMAGEM DO PROFILE E DO MENU
            if (retorno.getImage() != null) {
                Bitmap bMap = BitmapFactory.decodeByteArray(retorno.getImage(), 0, retorno.getImage().length);
                ivThumbnailPhotoMenu.setImageBitmap(bMap);
                editor.putString(IMG_PROFILE, Base64.encodeToString(retorno.getImage(), Base64.DEFAULT));

            } else {
                ivThumbnailPhotoMenu.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
            }
            editor.commit();

            // DIRECIONA PARA TELA DE MAPAS
            // directToMaps();

            // VERIFY IF PUSH ID IS UPDATED
            String actualPushId = FirebaseInstanceId.getInstance().getToken();
            boolean updatePush = false;
            if (StringUtils.isBlank(actualPushId)) {
                updatePush = false;
            } else if (StringUtils.isBlank(retorno.getPushId())) {
                updatePush = true;
            } else if (!actualPushId.equals(retorno.getPushId())) {
                updatePush = true;
            }

            if (updatePush) {
                retorno.setPushId(actualPushId);
                retorno.setLang(Locale.getDefault().getLanguage());
                new UserPushUpdateRequestTask().execute(retorno);
            }
        }
    }

    /**
     * Rest do add user
     */
    private class UserPushUpdateRequestTask extends AsyncTask<UsuarioBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(UsuarioBean... params) {
            try {
                Log.d("UpdateFcm", params.toString());
                final String url = MoovdisServices.getApiUserFrmUpdateUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                UsuarioBean usuario = params[0];
                return restTemplate.postForObject(url, usuario, CadastroRetornoBean.class);
            } catch (Exception e) {
                Log.e("UpdateFcm", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            if (retorno == null) {
                return;
            }
            Log.d("UpdateFcm", "SUCCESS");

        }

    }

    /**
     * Rest do add user
     */
    private class AlertsGetRequestTask extends AsyncTask<Boolean, Void, List<MarkerBean>> {
        @Override
        protected List<MarkerBean> doInBackground(Boolean... params) {
            busqueiLastMarkers = true;
            try {
                Log.i("AlertsGetRequestTask", "Init");
                final String url = MoovdisServices.getApiGetMarkersLastUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                MarkerBean[] retorno = restTemplate.getForObject(url, MarkerBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("AlertsGetRequestTask", e.getMessage(), e);
            } finally {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<MarkerBean> retorno) {
            if (retorno == null) {
                return;
            }
            setupRecycler(retorno);
        }
    }

}
