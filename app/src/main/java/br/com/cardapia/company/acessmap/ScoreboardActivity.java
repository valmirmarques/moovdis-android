package br.com.cardapia.company.acessmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.txusballesteros.widgets.FitChart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.cardapia.company.acessmap.bean.ScoreBoardGerenalBean;
import br.com.cardapia.company.acessmap.bean.ScoreBoardIndividualBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MemoryUtil;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import cn.refactor.lib.colordialog.PromptDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import mobi.gspd.segmentedbarview.Segment;
import mobi.gspd.segmentedbarview.SegmentedBarView;

public class ScoreboardActivity extends AppCompatActivity {

    private ScrollView mScroolGeneralScore;
    private FrameLayout frameGeneralScore;

    private ScrollView mScroollWeeklyScore;
    private FrameLayout frameWeeklyScore;

    private ScrollView mScroolMyScore;
    private FrameLayout frameMyScore;

    private TableLayout tableGeneralScore;
    private TableLayout tableWeeklyScore;

    private SegmentedBarView segmentedBarView;

    private SharedPreferences sharedPref;

    private String idUsuario;
    private static final float[] META_POINTS = new float[] {0f, 500f, 1000f};
    private static String[] META_LABELS;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mine:

                    if (StringUtils.isBlank(idUsuario)) {
                        MoovDisUtil.showInfoMessage(ScoreboardActivity.this, getString(R.string.not_cad), getString(R.string.do_cad));
                        return false;
                    }

                    mScroolMyScore.setVisibility(View.VISIBLE);
                    frameMyScore.setVisibility(View.VISIBLE);
                    mScroolGeneralScore.setVisibility(View.INVISIBLE);
                    frameGeneralScore.setVisibility(View.INVISIBLE);
                    mScroollWeeklyScore.setVisibility(View.INVISIBLE);
                    frameWeeklyScore.setVisibility(View.INVISIBLE);
                    new ScoreboardIndividualRequestTask().execute(idUsuario);
                    new ScoreboardIndividualPhotosRequestTask().execute(idUsuario);
                    return true;
                case R.id.navigation_general:
                    mScroolGeneralScore.setVisibility(View.VISIBLE);
                    frameGeneralScore.setVisibility(View.VISIBLE);
                    mScroolMyScore.setVisibility(View.INVISIBLE);
                    frameMyScore.setVisibility(View.INVISIBLE);
                    mScroollWeeklyScore.setVisibility(View.INVISIBLE);
                    frameWeeklyScore.setVisibility(View.INVISIBLE);
                    new ScoreboardGeneralRequestTask().execute();
                    return true;
                case R.id.navigation_weekly:
                    mScroollWeeklyScore.setVisibility(View.VISIBLE);
                    frameWeeklyScore.setVisibility(View.VISIBLE);
                    mScroolGeneralScore.setVisibility(View.INVISIBLE);
                    frameGeneralScore.setVisibility(View.INVISIBLE);
                    mScroolMyScore.setVisibility(View.INVISIBLE);
                    frameMyScore.setVisibility(View.INVISIBLE);
                    new ScoreboardWeeklyRequestTask().execute();
                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(MoovDisContants.PREFS_NAME, Context.MODE_PRIVATE);

        mScroolGeneralScore = (ScrollView) findViewById(R.id.aba_general_score);
        frameGeneralScore = (FrameLayout) findViewById(R.id.view_general_score_info);

        mScroollWeeklyScore = (ScrollView) findViewById(R.id.aba_weekly_score);
        frameWeeklyScore = (FrameLayout) findViewById(R.id.view_weekly_score_info);

        mScroolMyScore = (ScrollView) findViewById(R.id.aba_my_score_scroll);
        frameMyScore = (FrameLayout) findViewById(R.id.view_my_score_info);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView txtDescMyScore = (TextView) findViewById(R.id.txt_desc_my_placar);
        String nameUser = sharedPref.getString(MoovDisContants.NAME_USER, StringUtils.EMPTY).split(" ")[0];
        txtDescMyScore.setText(getString(R.string.desc_placar_individual, nameUser));

        CircleImageView myImgProfile = (CircleImageView) findViewById(R.id.img_profile_my_scorecard);
        String imgProfileB64 = sharedPref.getString(MoovDisContants.IMG_PROFILE, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(imgProfileB64)) {
            // Pega a Imagem do Perfil
            byte[] imgByte = Base64.decode(imgProfileB64, Base64.DEFAULT);
            Bitmap bMap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            myImgProfile.setImageBitmap(bMap);
        }

        META_LABELS = new String[] {getString(R.string.nivel_novato), getString(R.string.nivel_aplicado), getString(R.string.nivel_avancado)};

        // CATEGORIAS
        List<Segment> segments = new ArrayList<Segment>();
        Segment segment = new Segment(META_POINTS[0], META_POINTS[1], META_LABELS[0], Color.parseColor("#EE82EE"));
        segments.add(segment);
        Segment segment2 = new Segment(META_POINTS[1], META_POINTS[2], META_LABELS[1], Color.parseColor("#87CEFA"));
        segments.add(segment2);
        Segment segment3 = new Segment(META_POINTS[2], 3000f, META_LABELS[2], Color.parseColor("#3CB371"));
        segments.add(segment3);

        segmentedBarView = (SegmentedBarView) findViewById(R.id.seg_bar_view);
        segmentedBarView.setValueWithUnit(0f, getString(R.string.moeda));
        segmentedBarView.setSegments(segments);

        // FALTA META
        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);
        fitChart.setValue(0f);

        ((TextView) findViewById(R.id.txtQtdeMoedas)).setText(getString(R.string.voce_tem_moedas, 0));
        ((TextView) findViewById(R.id.txtAtingaMeta)).setText(getString(R.string.meta_moedas, (int) META_POINTS[1], META_LABELS[1]));
        ((TextView) findViewById(R.id.txtQtdeMarc)).setText(getString(R.string.qtde_marcacoes, 0));
        ((TextView) findViewById(R.id.txtQtdeFoto)).setText(getString(R.string.qtde_fotos, 0));

        tableGeneralScore = (TableLayout) findViewById(R.id.table_general_score);
        tableWeeklyScore = (TableLayout) findViewById(R.id.table_weekly_score);

        // Usuario Logado
        idUsuario = sharedPref.getString(MoovDisContants.ID_USUARIO, StringUtils.EMPTY);
        new ScoreboardGeneralRequestTask().execute();
    }

    public void onClickTextPlacar(View v) {
        Log.d("onClickTextPlacar", "Clicou para ver as regras");
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.regras_pontos))
                .setContentText(getString(R.string.regras_description))
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * Rest to get Scoreboard General
     */
    private class ScoreboardWeeklyRequestTask extends AsyncTask<String, Void, List<ScoreBoardGerenalBean>> {
        @Override
        protected List<ScoreBoardGerenalBean> doInBackground(String... params) {
            try {
                Log.d("ScoreboardWeekly", params.toString());
                MoovDisUtil.showProgress(ScoreboardActivity.this, getString(R.string.recover_scoreboard));
                final String url = MoovdisServices.getApiGetRankingWeekUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                ScoreBoardGerenalBean[] retorno = restTemplate.getForObject(url, ScoreBoardGerenalBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("ScoreboardWeekly", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(ScoreboardActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ScoreBoardGerenalBean> retorno) {
            if (retorno == null) {
                return;
            }

            if (retorno.isEmpty()) {
                MoovDisUtil.showInfoMessage(ScoreboardActivity.this, getString(R.string.placar_semanal), getString(R.string.info_not_found));
                return;
            }
            montaPlacar(tableWeeklyScore, retorno);
        }
    }

    /**
     * Rest to get Scoreboard General
     */
    private class ScoreboardGeneralRequestTask extends AsyncTask<String, Void, List<ScoreBoardGerenalBean>> {
        @Override
        protected List<ScoreBoardGerenalBean> doInBackground(String... params) {
            try {
                Log.d("ScoreboardGeneral", params.toString());
                MoovDisUtil.showProgress(ScoreboardActivity.this, getString(R.string.recover_scoreboard));
                final String url = MoovdisServices.getApiGetRankingGeneralUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                ScoreBoardGerenalBean[] retorno = restTemplate.getForObject(url, ScoreBoardGerenalBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("ScoreboardGeneral", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(ScoreboardActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ScoreBoardGerenalBean> retorno) {
            if (retorno == null) {
                return;
            }

            if (retorno.isEmpty()) {
                MoovDisUtil.showInfoMessage(ScoreboardActivity.this, getString(R.string.placar_geral), getString(R.string.info_not_found));
                return;
            }
            montaPlacar(tableGeneralScore, retorno);
        }
    }

    /**
     *
     * @param tabelaPlacar
     */
    private void montaPlacar(TableLayout tabelaPlacar, final List<ScoreBoardGerenalBean> lista) {
        tabelaPlacar.removeAllViewsInLayout();
        for (int i = 0; i < lista.size() && i < 10; i++) {
            final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);

            //Filling in cells
            TextView tv;
            tv = tableRow.findViewById(R.id.tableCell1);
            tv.setText(getString(R.string.placar_position, String.valueOf(i + 1)));

            //Filling in cells
            TextView tv2;
            tv2 = tableRow.findViewById(R.id.tableCell2);
            if (lista.get(i).getUserData().length > 0) {
                tv2.setText(lista.get(i).getUserData()[0].getName());
            } else {
                tv2.setText(getString(R.string.anonymus));
            }

            TextView tv3;
            tv3 = tableRow.findViewById(R.id.tableCell3);
            tv3.setText(String.valueOf(lista.get(i).getTotalPoints()));

            CircleImageView imgProfile = tableRow.findViewById(R.id.imgProfileGeneralScore);
            byte[] image = null;
            String _id = StringUtils.EMPTY;

            if (lista.get(i).getUserData().length > 0) {
                image = lista.get(i).getUserData()[0].getImage();
                _id = lista.get(i).getUserData()[0].get_id();
            }
            if (image != null && MemoryUtil.hasMemoryFree()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bMap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
                imgProfile.setImageBitmap(bMap);
            }

            if (idUsuario.equals(_id)) {
                tableRow.setBackgroundResource(R.drawable.border_selected);
                tv2.setTextColor(getResources().getColor(R.color.white));
                tv3.setTextColor(getResources().getColor(R.color.white));
            } else {
                tableRow.setBackgroundResource(R.drawable.border);
            }

            //Add row to the table
            tabelaPlacar.addView(tableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }


    /**
     * Rest to get Scoreboard Individual
     */
    private class ScoreboardIndividualRequestTask extends AsyncTask<String, Void, ScoreBoardIndividualBean> {
        @Override
        protected ScoreBoardIndividualBean doInBackground(String... params) {
            try {
                Log.d("ScoreboardIndividual", params.toString());
                MoovDisUtil.showProgress(ScoreboardActivity.this, getString(R.string.recover_scoreboard));
                final String url = MoovdisServices.getApiGetRankingUserUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                ScoreBoardIndividualBean[] retorno = restTemplate.getForObject(url, ScoreBoardIndividualBean[].class);
                if (retorno.length > 0) {
                    return retorno[0];
                }
            } catch (Exception e) {
                Log.e("ScoreboardActivity", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(ScoreboardActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ScoreBoardIndividualBean retorno) {
            if (retorno == null) {
                return;
            }
            float metaPoints = META_POINTS[0];
            String metaLabel = META_LABELS[0];
            if (retorno.getTotalPoints() >= META_POINTS[0]) {
                metaPoints = META_POINTS[1];
                metaLabel = META_LABELS[1];
            }
            if (retorno.getTotalPoints() >= META_POINTS[1]) {
                metaPoints = META_POINTS[2];
                metaLabel = META_LABELS[2];
            }
            segmentedBarView.setValueWithUnit((float)retorno.getTotalPoints(), getString(R.string.moeda));
            // FALTA META
            final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
            fitChart.setMinValue(0f);
            fitChart.setMaxValue(100f);
            float percentAtingido = (((float)retorno.getTotalPoints()/metaPoints)) * 100;
            if (percentAtingido > fitChart.getMaxValue()) {
                percentAtingido = fitChart.getMaxValue();
            }
            fitChart.setValue(percentAtingido);

            ((TextView) findViewById(R.id.txtQtdeMoedas)).setText(getString(R.string.voce_tem_moedas, retorno.getTotalPoints()));
            if (metaPoints >= (float)retorno.getTotalPoints()) {
                ((TextView) findViewById(R.id.txtAtingaMeta)).setText(getString(R.string.meta_moedas, (int) metaPoints, metaLabel));
            } else {
                ((TextView) findViewById(R.id.txtAtingaMeta)).setText(getString(R.string.parabens_cidadao));
            }
            ((TextView) findViewById(R.id.txtQtdeMarc)).setText(getString(R.string.qtde_marcacoes, retorno.getCount()));
        }
    }

    /**
     * Rest to get Scoreboard Individual
     */
    private class ScoreboardIndividualPhotosRequestTask extends AsyncTask<String, Void, ScoreBoardIndividualBean> {
        @Override
        protected ScoreBoardIndividualBean doInBackground(String... params) {
            try {
                Log.d("ScoreboardIndPhotos", params.toString());
                MoovDisUtil.showProgress(ScoreboardActivity.this, getString(R.string.recover_scoreboard));
                final String url = MoovdisServices.getApiGetRankingUserPhotosUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                ScoreBoardIndividualBean retorno = restTemplate.getForObject(url, ScoreBoardIndividualBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("ScoreboardIndPhotos", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(ScoreboardActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ScoreBoardIndividualBean retorno) {
            if (retorno == null) {
                return;
            }
            ((TextView) findViewById(R.id.txtQtdeFoto)).setText(getString(R.string.qtde_fotos, retorno.getCountImages()));

        }
    }

}
