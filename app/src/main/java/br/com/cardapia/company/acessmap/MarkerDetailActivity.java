package br.com.cardapia.company.acessmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import br.com.felix.imagezoom.ImageZoom;
import io.fabric.sdk.android.Fabric;

public class MarkerDetailActivity extends AppCompatActivity implements MoovDisContants {

    private Button btnLike;
    private Button btnNoExist;
    private MarkerBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_marker_detail);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        btnLike = (Button) findViewById(R.id.btn_like);
        btnNoExist = (Button) findViewById(R.id.btn_no_exist);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getBean() != null) {
                    getBean().setLikeCount(getBean().getLikeCount() + 1);
                    new MarkerLikeRequestTask().execute(getBean());
                }
            }
        });

        btnNoExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.get_id() == null) {
                    finish();
                }
                new MarkerDeleteRequestTask().execute(bean.get_id());
            }
        });

        new MarkerGetRequestTask().execute(getIntent().getStringExtra(MARKER_ID));

    }

    public MarkerBean getBean() {
        return bean;
    }

    public void setBean(MarkerBean bean) {
        this.bean = bean;
    }


    /**
     * Rest to get Scoreboard Individual
     */
    private class MarkerGetRequestTask extends AsyncTask<String, Void, MarkerBean> {
        @Override
        protected MarkerBean doInBackground(String... params) {
            try {
                Log.d("MarkerGetRequestTask", params.toString());
                MoovDisUtil.showProgress(MarkerDetailActivity.this, getString(R.string.rec_info));
                final String url = MoovdisServices.getApiGetMarkerByIdUrl(params[0]);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.getForObject(url, MarkerBean.class);
            } catch (Exception e) {
                Log.e("MarkerGetRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MarkerDetailActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MarkerBean retorno) {
            if (retorno == null) {
                return;
            }

            setBean(retorno);

            ((TextView) findViewById(R.id.txt_title)).setText(retorno.getLabel());
            if (StringUtils.isNotBlank(retorno.getComment())) {
                ((TextView) findViewById(R.id.txt_comment)).setText(retorno.getComment());
            }
            btnLike.setText(getString(R.string.like, retorno.getLikeCount()));

            ImageZoom imgOcorrencia = (ImageZoom) findViewById(R.id.img_local_ocorrencia);
            if (retorno.getImage() != null) {
                Bitmap bMap = BitmapFactory.decodeByteArray(retorno.getImage(), 0, retorno.getImage().length);
                imgOcorrencia.setImageBitmap(bMap);
            } else {
                imgOcorrencia.setImageDrawable(getResources().getDrawable(R.drawable.maps_marker));
            }

            ((TextView) findViewById(R.id.txt_endereco)).setText(retorno.getAddressDescription());

        }
    }


    /**
     * Rest to Mark add General
     */
    private class MarkerLikeRequestTask extends AsyncTask<MarkerBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(MarkerBean... params) {
            try {
                Log.d("MarkerLikeRequestTask", params[0].get_id());
                MoovDisUtil.showProgress(MarkerDetailActivity.this, getString(R.string.generate_like));
                final String url = MoovdisServices.getApiPostMarkerLikeUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("MarkerLikeRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MarkerDetailActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            // DO NOTHING
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
                MoovDisUtil.showProgress(MarkerDetailActivity.this, getString(R.string.apagar_ocorrencia));
                final String url = MoovdisServices.getApiDeleteMarkerUrl(params[0], NO);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                restTemplate.delete(url);
            } catch (Exception e) {
                Log.e("MarkerDeleteRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(MarkerDetailActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
                Intent intent = new Intent();
                intent.putExtra(DELETE_MARKER, true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void retorno) {
            // DO NOTHING
        }
    }


}
