package br.com.cardapia.company.acessmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.cardapia.company.acessmap.adapter.TwitterFeedsLineAdapter;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.NullAuthorization;

public class TwitterFeedsActivity extends AppCompatActivity implements MoovDisContants{

    private RecyclerView mRecyclerView;
    private TwitterFeedsLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_feeds);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        mRecyclerView = findViewById(R.id.recycler_view_layour_recycler);
        mRecyclerView.setHasFixedSize(false);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FetchTwitterSearch fetchUrlTranslator = new FetchTwitterSearch();
        fetchUrlTranslator.execute();

    }


    // Fetches data from url passed
    private class FetchTwitterSearch extends AsyncTask<String, Void, QueryResult> {

        @Override
        protected QueryResult doInBackground(String... url) {

            MoovDisUtil.showProgress(TwitterFeedsActivity.this, getString(R.string.recover_feed));

            Twitter twitter = TwitterFactory.getSingleton();

            if (twitter.getAuthorization() instanceof NullAuthorization) {
                twitter.setOAuthConsumer(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
                twitter.setOAuthAccessToken(new AccessToken(getString(R.string.com_twitter_sdk_android_TOKEN), getString(R.string.com_twitter_sdk_android_TOKEN_SECRET)));
            }

            String queryString = "acessibilidade mobilidade";
            // FOR ENGLISH
            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase(LANG_ENGLISH)) {
                queryString = "accessibility mobility";
            }

            Query query = new Query(queryString);
            //GeoLocation geo = new GeoLocation(-25.4284, -49.2733);
            //query.setGeoCode(geo, 20, Query.Unit.mi);
            query.setCount(50);
            query.setResultType(Query.ResultType.mixed);
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                Log.e("TwitterSearch", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(TwitterFeedsActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return result;
        }

        @Override
        protected void onPostExecute(QueryResult result) {
            Log.i("onPostExecute", "Start");
            super.onPostExecute(result);
            int count = 0;
            if (result != null && result.getTweets() != null && !result.getTweets().isEmpty()) {
                List<twitter4j.Status> newTweetsList = new ArrayList<>();
                for (twitter4j.Status status : result.getTweets()) {
                    if (!status.isRetweet()) {
                        newTweetsList.add(status);
                    }
                }
                setupRecycler(newTweetsList);
            } else {
                MoovDisUtil.showInfoMessage(TwitterFeedsActivity.this, getString(R.string.feed_label), getString(R.string.feed_not_found));
            }
            Log.d("onPostExecute", "Start");
        }
    }


    /**
     * Setup Recycler
     */
    private void setupRecycler(final List<Status> tweets) {

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new TwitterFeedsLineAdapter(new ArrayList<>(tweets));
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        //mRecyclerView.addItemDecoration(
        //        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
            this.openUrl(s);
        }
    }

    /**
     * Abre URL
     *
     * @param url
     */
    private void openUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            Log.d("OpenURL", url);

            final String twitter = "twitter.com";
            Intent intent;
            if (url.contains(twitter)) {
                try {
                    this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (PackageManager.NameNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            }
            this.startActivity(intent);
        }

    }
}
