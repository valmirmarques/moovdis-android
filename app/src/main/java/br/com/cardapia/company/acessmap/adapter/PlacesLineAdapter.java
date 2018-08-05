package br.com.cardapia.company.acessmap.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.bean.VenueInfoBean;
import br.com.cardapia.company.acessmap.bean.foursquare.Venue;
import br.com.cardapia.company.acessmap.bean.foursquare.photos.VenuesPhotosService;
import br.com.cardapia.company.acessmap.services.FoursquareService;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;


public class PlacesLineAdapter extends RecyclerView.Adapter<PlacesLineHolder> implements MoovDisContants {

    private final List<Venue> mVenues;
    PlacesLineHolder actualHolder;
    private Context mContext;
    private CallbackInterface mCallback;


    private Map<String, String> cachImagesUrls = new HashMap<>();

    public PlacesLineAdapter(Context context, final ArrayList venues) {
        mVenues = venues;
        mContext = context;

        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            Log.e("PlacesLineAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
    }

    @Override
    public PlacesLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesLineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_row_places, parent, false));
    }

    @Override
    public void onBindViewHolder(PlacesLineHolder holder, int position) {
        final Venue event = mVenues.get(position);
        actualHolder = holder;
        //Filling in cells
        holder.description.setText(event.getName());

        String addr = event.getLocation().getAddress();
        if (StringUtils.isBlank(addr) && event.getLocation().getFormattedAddress() != null && !event.getLocation().getFormattedAddress().isEmpty()) {
            addr = event.getLocation().getFormattedAddress().get(0);
            if (event.getLocation().getFormattedAddress().size() > 1) {
                addr += " " + event.getLocation().getFormattedAddress().get(1);
            }
        }
        String categoria = StringUtils.EMPTY;
        if (event.getCategories() != null && !event.getCategories().isEmpty()) {
            categoria = event.getCategories().get(0).getName();
        }
        holder.addreess.setText(addr);
        holder.categoria.setText(categoria);
        holder.distance.setText(mContext.getString(R.string.venue_distance, event.getLocation().getDistance()));

        holder.description.setTag(event.getId());
        holder.addreess.setTag(event.getId());
        holder.categoria.setTag(event.getId());
        holder.imgMarcacao.setTag(event.getId());
        holder.distance.setTag(event.getId());

        if (event.getVenueInfo() != null) {
            VenueInfoBean info = event.getVenueInfo();
            switch (info.getLugarAcessivel()) {
                case YES:
                    holder.iconAcessible.setImageDrawable(mContext.getDrawable(R.drawable.if_accessible_true));
                    holder.iconAcessible.setContentDescription(mContext.getString(R.string.place_acessible));
                    break;
                case MID:
                    holder.iconAcessible.setImageDrawable(mContext.getDrawable(R.drawable.if_accessible_mid));
                    holder.iconAcessible.setContentDescription(mContext.getString(R.string.place_parcially_acessible));
                    break;
                case NO:
                    holder.iconAcessible.setImageDrawable(mContext.getDrawable(R.drawable.if_accessible_false));
                    holder.iconAcessible.setContentDescription(mContext.getString(R.string.place_not_acessible));
                    break;
            }

        } else {
            holder.iconAcessible.setImageDrawable(mContext.getDrawable(R.drawable.if_accessible_none));
            holder.iconAcessible.setContentDescription(mContext.getString(R.string.not_rated));
        }

        addEventsOnScreenComponents(holder, event);

        /*
        String id = event.getId();
        if (cachImagesUrls.containsKey(id)) {
            setBitmapImage(cachImagesUrls.get(id));
        } else {
            new VenuesPhotosGetRequestTask().execute(id);
        }
        */

    }

    private void addEventsOnScreenComponents(PlacesLineHolder holder, final Venue event) {
        holder.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(event.getId(), event.getName());
                }
            }
        });

        holder.imgMarcacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onPlaceDetailSelection(event);
                }
            }
        });

        holder.layoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onPlaceDetailSelection(event);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVenues != null ? mVenues.size() : 0;
    }

    /**
     * Load Image URL
     *
     * @param urlString
     */
    private void setBitmapImage(final String urlString) {

        if (urlString == null || urlString.equals(StringUtils.EMPTY)) {
            actualHolder.imgMarcacao.setImageDrawable(mContext.getDrawable(R.drawable.back_city));
            actualHolder.imgMarcacao.setAlpha(0.7f);

            return;
        }


        new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
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
                actualHolder.imgMarcacao.setImageBitmap(bitmap);
                actualHolder.imgMarcacao.setAlpha(0.7f);
            }
        }.execute();
    }

    /**
     *
     */
    public interface CallbackInterface {
        /**
         * @param idVenue
         */
        void onHandleSelection(final String idVenue, final String venueName);
        /**
         * @param event
         */
        void onPlaceDetailSelection(final Venue event);
    }

    /**
     * Rest do add user
     */
    private class VenuesPhotosGetRequestTask extends AsyncTask<String, Void, VenuesPhotosService> {
        @Override
        protected VenuesPhotosService doInBackground(String... params) {
            try {
                Log.i("PhotosGetRequestTask", "Init");
                String id = params[0];
                cachImagesUrls.put(id, StringUtils.EMPTY);
                final String url = FoursquareService.getPhotoVenuesApiUrl(id,
                        mContext.getString(R.string.fsq_client_id),
                        mContext.getString(R.string.fsq_client_secret));
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                VenuesPhotosService retorno = restTemplate.getForObject(url, VenuesPhotosService.class);
                if (retorno != null && retorno.getResponse() != null) {
                    retorno.getResponse().getAdditionalProperties().put("ID", id);
                }
                return retorno;
            } catch (Exception e) {
                Log.e("PhotosGetRequestTask", e.getMessage(), e);
//                setBitmapImage(StringUtils.EMPTY);
            }
            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(VenuesPhotosService retorno) {

            String urlImage = StringUtils.EMPTY;
            if (retorno != null
                    && retorno.getResponse() != null
                    && retorno.getResponse().getPhotos() != null
                    && retorno.getResponse().getPhotos().getCount() > 0) {
                urlImage = retorno.getResponse().getPhotos().getItems().get(0).getPrefix() +
                        "320x250" +
                        retorno.getResponse().getPhotos().getItems().get(0).getSuffix();
                String id = (String) retorno.getResponse().getAdditionalProperties().get("ID");
                cachImagesUrls.put(id, urlImage);
            }
            setBitmapImage(urlImage);
        }
    }

}