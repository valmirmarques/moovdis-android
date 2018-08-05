package br.com.cardapia.company.acessmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appinvite.AppInviteInvitation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.RankingBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import io.fabric.sdk.android.Fabric;

public class InviteFriendsActivity extends AppCompatActivity implements MoovDisContants {

    private static final String TAG = InviteFriendsActivity.class.getCanonicalName();

    final int MULTIPLER = 10;

    private SharedPreferences sharedPref;
    private String idUsuario = StringUtils.EMPTY;


    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        idUsuario = sharedPref.getString(ID_USUARIO, StringUtils.EMPTY);


        Button button = (Button) findViewById(R.id.btn_invite_google);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.invite_friends_toast_after_share, Toast.LENGTH_LONG).show();

                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.action_invite_friends_moovdis))
                        .setMessage(getString(R.string.download_moovdis))
                        .setDeepLink(Uri.parse(getString(R.string.invite_friends_market_url_short)))
                        .setCustomImage(Uri.parse(getString(R.string.invite_friends_market_url_image)))
                        .setCallToActionText(getString(R.string.invite_friends_share_chooser))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE_GOOGLE);

            }
        });

        Button buttonWhats = (Button) findViewById(R.id.btn_invite_whats);
        buttonWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWhatsAppInvite(view);
            }
        });
    }

    /**
     * Login do Facebook
     *
     * @param view
     */
    public void fbLogin(View view) {
        AccessToken acessToken = null;//AccessToken.getCurrentAccessToken();

        // SE NAO ESTIVER AINDA AUTENTICADO... FAZ O LOGIN
        if (acessToken == null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_friends", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            findFacebookFriends(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getApplicationContext(), getString(R.string.cancel_sharing), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            findFacebookFriends(acessToken);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (resultCode == RESULT_OK) {
            int points = 0;
            if (requestCode == REQUEST_INVITE_GOOGLE) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                    points++;
                }
            } else if (requestCode == REQUEST_INVITE_WHATSAPP) {
                points = 5;
            }

            RankingBean bean = new RankingBean();
            bean.setDateCreated(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            bean.setGoal(new String[]{INVITE_FRIENDS});
            bean.setPoints(points * MULTIPLER);
            UsuarioBean user = new UsuarioBean();
            user.set_id(idUsuario);
            bean.setUser(user);

            new RankingAddRequestTask().execute(bean);
        }

    }


    /**
     * Convidar amigos para o Whatsapp
     *
     * @param v
     */
    public void onWhatsAppInvite(View v) {
        Toast.makeText(this, R.string.invite_friends_toast_after_share, Toast.LENGTH_LONG).show();

        final String shareBody = getString(R.string.download_moovdis) + "\n" + getString(R.string.invite_friends_market_url);

        try {
            Intent shareToWhatsApp = new Intent(Intent.ACTION_SEND);
            shareToWhatsApp.setType("text/plain");

            shareToWhatsApp.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            shareToWhatsApp.setClassName("com.whatsapp", "com.whatsapp.ContactPicker");
            startActivityForResult(shareToWhatsApp, REQUEST_INVITE_WHATSAPP);
        } catch (Exception e) {
            //Intent shareGeneric = new Intent(Intent.ACTION_SEND);
            //shareGeneric.setType("text/plain");
            //shareGeneric.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            //startActivityForResult(Intent.createChooser(shareGeneric, getResources().getString(R.string.invite_friends_share_chooser)), REQUEST_INVITE_WHATSAPP);

            Toast.makeText(this, R.string.app_not_instaled, Toast.LENGTH_LONG).show();
        }

    }

    private void findFacebookFriends(final AccessToken accessToken) {
        /* make the API call */
        new GraphRequest(
                accessToken,
                "/me/friendlists",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("friendsList", response.toString());
                    }
                }
        ).executeAsync();
    }


    /**
     * Rest do add user
     */
    private class RankingAddRequestTask extends AsyncTask<RankingBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(RankingBean... params) {
            try {
                Log.d("RankingAddRequestTask", Arrays.toString(params));
                MoovDisUtil.showProgress(InviteFriendsActivity.this, getString(R.string.gera_pontos));
                final String url = MoovdisServices.getApiGetRankingAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                RankingBean ranking = params[0];
                CadastroRetornoBean retorno = restTemplate.postForObject(url, ranking, CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                MoovDisUtil.showErrorMessage(getApplicationContext(), getString(R.string.ops), e.getMessage());
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

        }

    }

}
