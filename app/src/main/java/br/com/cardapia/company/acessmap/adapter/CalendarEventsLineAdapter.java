package br.com.cardapia.company.acessmap.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.bean.CalendarEventsBean;
import br.com.cardapia.company.acessmap.util.MoovDisContants;


public class CalendarEventsLineAdapter extends RecyclerView.Adapter<CalendarEventsLineHolder> implements MoovDisContants {

    private final List<CalendarEventsBean> mEvents;

    public CalendarEventsLineAdapter(ArrayList events) {
        mEvents = events;
    }

    @Override
    public CalendarEventsLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CalendarEventsLineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_row_events, parent, false));
    }

    @Override
    public void onBindViewHolder(CalendarEventsLineHolder holder, int position) {
        CalendarEventsBean event = mEvents.get(position);

        //Filling in cells
        holder.description.setText(event.getTitle());
        holder.addreess.setText(event.getAddress());
        holder.facebook.setText(event.getFacebook());
        holder.date.setText(event.getTimeDescription());

        holder.description.setTag(event.getEventUrl());
        holder.addreess.setTag(event.getEventUrl());
        holder.facebook.setTag(event.getFacebook());
        holder.imgMarcacao.setTag(event.getEventUrl());
        holder.date.setTag(event.getEventUrl());

        if (StringUtils.isBlank(event.getFacebook())) {
            holder.facebook.setVisibility(View.GONE);
        } else {
            holder.facebook.setVisibility(View.VISIBLE);
        }

        setBitmapImage(event, holder);
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }


    /**
     * Load Image URL
     * @param eventBean
     * @param holder
     */
    private void setBitmapImage(final CalendarEventsBean eventBean, final CalendarEventsLineHolder holder) {
        new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    String urlString = eventBean.getBannerUrl();
                    URL url = new URL(urlString);
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.imgMarcacao.setImageBitmap(bitmap);
            }
        }.execute();


    }


}