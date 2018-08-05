package br.com.cardapia.company.acessmap;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.cardapia.company.acessmap.bean.BotaoReportBean;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.RankingBean;
import br.com.cardapia.company.acessmap.bean.RouteBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.ColorUtil;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import io.fabric.sdk.android.Fabric;

public class RoutesActivity extends AppCompatActivity implements NoticeDialogFeedbackRouteFragment.NoticeDialogListener, MoovDisContants {

    final String SHARE_ROUTE = "SHARE_ROUTE";
    private SharedPreferences sharedPref;
    private String idUsuario;
    private LinearLayout tableRotas;
    private BotaoReportBean[] botaoSocialNetwork = {new BotaoReportBean(FACEBOOK, R.drawable.if_facebook_circle_294710, "Facebook", R.drawable.if_facebook_circle_294710),
            new BotaoReportBean(TWITTER, R.drawable.if_twitter_834708, "Twitter", R.drawable.if_twitter_834708),
            new BotaoReportBean(WHATSAPP, R.drawable.if_whatsapp_287520, "Whatsapp", R.drawable.if_whatsapp_287520),
    };

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(RoutesActivity.this, getString(R.string.sucess_sharing), Toast.LENGTH_SHORT).show();
                new RankingAddRequestTask().execute(getRankingBean());
            }

            @Override
            public void onCancel() {
                Toast.makeText(RoutesActivity.this, getString(R.string.cancel_sharing), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RoutesActivity.this, getString(R.string.erro_sharing), Toast.LENGTH_SHORT).show();
                Log.e("FacebookException", error.getMessage(), error);
            }
        });


        // Usuario Logado
        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(MoovDisContants.PREFS_NAME, Context.MODE_PRIVATE);
        idUsuario = sharedPref.getString(MoovDisContants.ID_USUARIO, StringUtils.EMPTY);

        tableRotas = (LinearLayout) findViewById(R.id.linear_layout_rotas);

        // check if user is on
        if (StringUtils.isBlank(idUsuario)) {
            MoovDisUtil.showInfoMessage(RoutesActivity.this, getString(R.string.not_cad), getString(R.string.do_cad_routes));
            return;
        }

        // RECUPERA AS ROTAS DO USUARIO
        new RoutesRequestTask().execute(idUsuario);


    }

    /**
     * @param idRota
     */
    private void showCommentDialog(final String idRota) {
        Log.d("showCommentDialog", idRota);
        DialogFragment dialog = new NoticeDialogFeedbackRouteFragment();
        ((NoticeDialogFeedbackRouteFragment) dialog).setIdRota(idRota);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String _id = ((NoticeDialogFeedbackRouteFragment) dialog).getIdRota();
        String comentario = ((NoticeDialogFeedbackRouteFragment) dialog).getComments();
        float rating = ((NoticeDialogFeedbackRouteFragment) dialog).getRating();
        boolean isLiked = ((NoticeDialogFeedbackRouteFragment) dialog).isLiked();
        boolean isPublic = ((NoticeDialogFeedbackRouteFragment) dialog).isPublicFeedback();

        RouteBean bean = new RouteBean();
        UsuarioBean usuarioBean = new UsuarioBean();
        usuarioBean.set_id(sharedPref.getString(MoovDisContants.ID_USUARIO, StringUtils.EMPTY));
        bean.set_id(_id);
        bean.setComment(comentario);
        bean.setLike(isLiked);
        bean.setRating(rating);
        bean.setFeedbackPublic(isPublic);

        bean.setUser(usuarioBean);

        new RoutesCommentTask().execute(bean);


    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // DO NOTHING
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == ShareUtils.SHARE_ROUTE_CODE) {
                new RankingAddRequestTask().execute(getRankingBean());
            }
        }

    }

    /**
     * @return
     */
    private RankingBean getRankingBean() {

        RankingBean bean = new RankingBean();
        bean.setDateCreated(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        bean.setGoal(new String[]{SHARE_ROUTE});
        bean.setPoints(50);
        UsuarioBean user = new UsuarioBean();
        user.set_id(idUsuario);
        bean.setUser(user);

        return bean;
    }

    /**
     * Rest to get Scoreboard Individual
     */
    private class RoutesRequestTask extends AsyncTask<String, Void, List<RouteBean>> {
        @Override
        protected List<RouteBean> doInBackground(String... params) {
            try {
                Log.d("RoutesRequestTask", params.toString());
                MoovDisUtil.showProgress(RoutesActivity.this, getString(R.string.recover_router));
                final String url = MoovdisServices.getApiRouteGetUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                RouteBean[] retorno = restTemplate.getForObject(url, RouteBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("RoutesRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(RoutesActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RouteBean> retorno) {
            if (retorno == null) {
                return;
            }
            if (retorno.isEmpty()) {
                MoovDisUtil.showInfoMessage(RoutesActivity.this, getString(R.string.action_minhas_rotas), getString(R.string.info_not_found));
                return;
            }

            if (tableRotas != null) {
                tableRotas.removeAllViewsInLayout();
                for (int i = 0; i < retorno.size(); i++) {
                    final RelativeLayout tableRow = (RelativeLayout) getLayoutInflater().inflate(R.layout.tablerow_rotas, null);
                    final TextView txtOrigem = (TextView) tableRow.findViewById(R.id.txtOrigem);
                    final TextView txtDestino = (TextView) tableRow.findViewById(R.id.txtDestino);
                    final TextView txtDataRota = (TextView) tableRow.findViewById(R.id.txtDataRota);
                    final TextView txtComments = (TextView) tableRow.findViewById(R.id.txtComment);
                    final ImageView imgLike = (ImageView) tableRow.findViewById(R.id.imgPositiveNegative);
                    //Add row to the table
                    txtOrigem.setText(retorno.get(i).getLocationOrig().getName());
                    txtDestino.setText(retorno.get(i).getLocationDest().getName());
                    txtDataRota.setText(MoovDisUtil.formatLongDate(retorno.get(i).getDateCreated()));
                    txtComments.setText(retorno.get(i).getComment());
                    if (retorno.get(i).isLike()) {
                        imgLike.setImageResource(R.drawable.like);
                        imgLike.setContentDescription(getString(R.string.liked));
                    } else {
                        imgLike.setImageResource(R.drawable.not_like);
                        imgLike.setContentDescription(getString(R.string.not_liked));
                    }
                    if (StringUtils.isBlank(retorno.get(i).getComment())) {
                        imgLike.setVisibility(View.INVISIBLE);
                    } else {
                        imgLike.setVisibility(View.VISIBLE);
                    }

                    final ImageButton btnAddComment = (ImageButton) tableRow.findViewById(R.id.btnAddComment);
                    // DEFINE TAG AS ID
                    btnAddComment.setTag(retorno.get(i).get_id());
                    btnAddComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentDialog((String) view.getTag());
                        }
                    });

                    // Button Share Feedback
                    final BoomMenuButton bmb = (BoomMenuButton) tableRow.findViewById(R.id.btnShareFeedback);
                    bmb.setNormalColor(Color.WHITE);
                    bmb.setBackgroundEffect(true);
                    bmb.setDimColor(ColorUtil.getColorWithAlpha(Color.GRAY, 70f));
                    bmb.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
                    bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);
                    bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
                    for (int j = 0; j < botaoSocialNetwork.length; j++) {
                        botaoSocialNetwork[j].setTag(retorno.get(i).get_id());
                        TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                                .normalImageRes(botaoSocialNetwork[j].getImageButton())
                                .normalText(botaoSocialNetwork[j].getText())
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
                                        try {
                                            String url = MoovdisServices.getApiRouteSharingUrl(botaoSocialNetwork[index].getTag());
                                            if (WHATSAPP.equals(botaoSocialNetwork[index].getButtonName())) {
                                                ShareUtils.shareWhatsapp(RoutesActivity.this, getString(R.string.i_did_route), url);
                                            } else if (FACEBOOK.equals(botaoSocialNetwork[index].getButtonName())) {
                                                if (ShareDialog.canShow(ShareLinkContent.class)) {
                                                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                            .setQuote(getString(R.string.i_did_route) + " #moovdis #mobilidade #acessível")
                                                            .setContentUrl(Uri.parse(url))
                                                            .setShareHashtag(new ShareHashtag.Builder()
                                                                    .setHashtag("#acessibilidade")
                                                                    .build())
                                                            .build();
                                                    shareDialog.show(linkContent);
                                                } else {
                                                    throw new ActivityNotFoundException("app not instaled");
                                                }
                                            } else if (TWITTER.equals(botaoSocialNetwork[index].getButtonName())) {
                                                ShareUtils.shareTwitter(RoutesActivity.this, getString(R.string.i_did_route), url, "moovdisapp", "moovdis,mobilidade,acessibilidade");
                                            }
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(RoutesActivity.this, getString(R.string.app_not_instaled), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        bmb.addBuilder(builder);
                    }

                    tableRotas.addView(tableRow);
                }
            }

        }
    }

    /**
     * Rest to get Scoreboard Individual
     */
    private class RoutesCommentTask extends AsyncTask<RouteBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(RouteBean... params) {
            try {
                Log.d("RoutesCommentTask", params.toString());
                MoovDisUtil.showProgress(RoutesActivity.this, getString(R.string.saving));
                final String url = MoovdisServices.getApiRouteCommentAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                RouteBean bean = params[0];
                CadastroRetornoBean retorno = restTemplate.postForObject(url, bean, CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("RoutesRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(RoutesActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            // RECUPERA AS ROTAS DO USUARIO
            new RoutesRequestTask().execute(idUsuario);

        }
    }

    /**
     * Rest do add user
     */
    private class RankingAddRequestTask extends AsyncTask<RankingBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(RankingBean... params) {
            try {
                Log.d("RankingAddRequestTask", Arrays.toString(params));
                MoovDisUtil.showProgress(RoutesActivity.this, getString(R.string.gera_pontos));
                final String url = MoovdisServices.getApiGetRankingAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                RankingBean ranking = params[0];
                CadastroRetornoBean retorno = restTemplate.postForObject(url, ranking, CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("RankingAddRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(RoutesActivity.this, getString(R.string.ops), e.getMessage());
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
