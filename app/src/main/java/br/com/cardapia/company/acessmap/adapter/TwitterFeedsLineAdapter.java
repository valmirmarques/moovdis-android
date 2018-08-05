package br.com.cardapia.company.acessmap.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import twitter4j.Status;

public class TwitterFeedsLineAdapter extends RecyclerView.Adapter<TwitterFeedsLineHolder> implements MoovDisContants {

    private final List<Status> mTweets;

    public TwitterFeedsLineAdapter(ArrayList tweets) {
        mTweets = tweets;
    }

    @Override
    public TwitterFeedsLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TwitterFeedsLineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_row_twitter_feeds, parent, false));
    }

    @Override
    public void onBindViewHolder(TwitterFeedsLineHolder holder, int position) {
        Status status = mTweets.get(position);
        String tempUrl = null;

        if (status.getURLEntities() != null && status.getURLEntities().length > 0) {
            tempUrl = status.getURLEntities()[0].getExpandedURL();
        }

        final String url = tempUrl;
        //Filling in cells
        holder.description.setText(status.getText());
        holder.description.setHint(status.getText());
        String text = "@" + status.getUser().getScreenName() + " - " + status.getUser().getLocation() + " - " + MoovDisUtil.formatLongDate(status.getCreatedAt());
        holder.userData.setText(text);
        holder.userData.setHint(text);

        holder.cardItem.setTag(url);
        holder.description.setTag(url);
        holder.userData.setTag(url);


        setBitmapImage(status.getUser().getProfileImageURLHttps(), holder);

        //holder.description.setOnClickListener(view -> openUrl(url));

    }

    @Override
    public int getItemCount() {
        return mTweets != null ? mTweets.size() : 0;
    }

    /**
     * Load Image from URL
     *
     * @param bitmapUrl
     * @return
     */
    private void setBitmapImage(final String bitmapUrl, final TwitterFeedsLineHolder holder) {

        new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                //Log.d("imageProfileGetter", "Obtendo Imagem do Profile User");
                try {
                    URL url = new URL(bitmapUrl);
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.imgUser.setImageBitmap(bitmap);
            }
        }.execute();
    }

}