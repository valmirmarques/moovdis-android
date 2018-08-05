package br.com.cardapia.company.acessmap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import br.com.cardapia.company.acessmap.adapter.CalendarEventsLineAdapter;
import br.com.cardapia.company.acessmap.adapter.TwitterFeedsLineAdapter;
import br.com.cardapia.company.acessmap.bean.CalendarEventsBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.NullAuthorization;

public class CalendarEventsActivity extends AppCompatActivity implements MoovDisContants{

    private RecyclerView mRecyclerView;
    private CalendarEventsLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_events);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        mRecyclerView = findViewById(R.id.recycler_view_events);
        mRecyclerView.setHasFixedSize(false);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // RECUPERA CALENDARIO DE EVENTOS
        new CelandarEventsGetRequestTask().execute();

    }


    /**
     * Rest do add user
     */
    private class CelandarEventsGetRequestTask extends AsyncTask<Void, Void, List<CalendarEventsBean>> {
        @Override
        protected List<CalendarEventsBean> doInBackground(Void... params) {
            try {
                Log.i("EventsGetRequestTask", "Init");
                MoovDisUtil.showProgress(CalendarEventsActivity.this, getString(R.string.rec_info));
                final String url = MoovdisServices.getApiGetEventsUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CalendarEventsBean[] retorno = restTemplate.getForObject(url, CalendarEventsBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("EventsGetRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(CalendarEventsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }

            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(List<CalendarEventsBean> retorno) {
            if (retorno == null) {
                return;
            }
            setupRecycler(retorno);
        }
    }


    /**
     * Setup Recycler
     */
    private void setupRecycler(final List<CalendarEventsBean> events) {

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new CalendarEventsLineAdapter(new ArrayList<>(events));
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    /**
     * Open URL
     *
     * @param view
     */
    public void openUrl(View view) {
        Object obj = view.getTag();
        if (obj != null) {
            String s = (String) obj;
            this.openWebPage(s);
        }
    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.failed_open_url),  Toast.LENGTH_LONG).show();
            Log.e("openWebPage", e.getMessage(), e);
        }
    }

    @OnClick(R.id.txt_divulgue_evento)
    public void sendEmailContact(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"acessibilidade.moovdis.app@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_divulgue_title));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_divulgue_text));
        startActivity(Intent.createChooser(intent, getString(R.string.email_action)));

    }
}
