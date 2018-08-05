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

import br.com.cardapia.company.acessmap.MapsActivity;
import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.bean.BotaoReportBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.util.MoovDisContants;


public class LastMarkersLineAdapter extends RecyclerView.Adapter<LastMarkersLineHolder> implements MoovDisContants {

    private final List<MarkerBean> mMarkers;

    public LastMarkersLineAdapter(ArrayList markers) {
        mMarkers = markers;
    }

    @Override
    public LastMarkersLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LastMarkersLineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_row_last_markers, parent, false));
    }

    @Override
    public void onBindViewHolder(LastMarkersLineHolder holder, int position) {
        MarkerBean marker = mMarkers.get(position);
        MarkerBean markerNoPhoto = new MarkerBean();
        markerNoPhoto.set_id(marker.get_id());
        markerNoPhoto.setLocation(marker.getLocation());
        markerNoPhoto.setAddressDescription(marker.getAddressDescription());


        String textAddress = marker.getAddressDescription();
        //Filling in cells
        // ajusta campos se a fonte for grande
        if (holder.comments.getTextSize() > 54.0f) {
            holder.comments.setVisibility(View.INVISIBLE);
            if (textAddress.length() > 60) {
                textAddress = textAddress.substring(0, 60) + "...";
            }
        }
        holder.description.setText(marker.getLabel());
        holder.description.setTag(markerNoPhoto);
        holder.description.setContentDescription(marker.getLabel());
        if (marker.getLikeCount() > 0) {
            textAddress += " - " + marker.getLikeCount() + (marker.getLikeCount() == 1 ? " Like " : " Likes");
        }
        holder.addreess.setText(textAddress);
        holder.comments.setText(marker.getComment());


        setBitmapImage(marker, holder);
        setImageIcon(marker, holder);

    }

    private void openOptions() {

    }

    @Override
    public int getItemCount() {
        return mMarkers != null ? mMarkers.size() : 0;
    }

    /**
     * Load Image from URL
     *
     * @param markerBean
     * @return
     */
    private void setBitmapImage(final MarkerBean markerBean, final LastMarkersLineHolder holder) {
        holder.imgMarcacao.setAlpha(0.7f);
        holder.imgMarcacao.setContentDescription(markerBean.getLabel());
        String addr = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(markerBean.getAddressDescription())) {
            addr = markerBean.getAddressDescription().split(",")[0];
        }
        holder.imgMarcacao.setContentDescription(holder.getContext().getString(R.string.ocorrencia_na, markerBean.getLabel(), addr));
        if (markerBean.getImage() != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(markerBean.getImage(), 0, markerBean.getImage().length);
            holder.imgMarcacao.setImageBitmap(bMap);
        } else {
            //holder.imgMarcacao.setImageDrawable(holder.getContext().getDrawable(R.drawable.back_city));
            new AsyncTask<String, String, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    try {
                        String center = String.format("%s,%s", markerBean.getLocation().getCoordinates()[1], markerBean.getLocation().getCoordinates()[0]);
                        String urlString = "https://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=17&scale=1&size=450x150&maptype=satellite&format=png&visual_refresh=true";
                        URL url = new URL(urlString);
                        //Log.d("obtem_goo_map", urlString);
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

    /// https://maps.googleapis.com/maps/api/staticmap?center=Albany,+NY&zoom=13&scale=1&size=600x300&maptype=roadmap&format=png&visual_refresh=true

    /**
     * Load Image from URL
     *
     * @param markerBean
     * @return
     */
    private void setImageIcon(final MarkerBean markerBean, final LastMarkersLineHolder holder) {
        if (markerBean.getMarkerType().equals(MARKER_GOOD)) {
            for (BotaoReportBean botao : MapsActivity.BOTAO_GOOD) {
                if (markerBean.getMarkerIcon().equals(botao.getButtonName())) {
                    holder.imgIcon.setImageDrawable(holder.getContext().getDrawable(botao.getImageButtonBack()));
                    break;
                }
            }
        } else {
            for (BotaoReportBean botao : MapsActivity.BOTAO_BAD) {
                if (markerBean.getMarkerIcon().equals(botao.getButtonName())) {
                    holder.imgIcon.setImageDrawable(holder.getContext().getDrawable(botao.getImageButtonBack()));
                    break;
                }
            }
        }
    }

}