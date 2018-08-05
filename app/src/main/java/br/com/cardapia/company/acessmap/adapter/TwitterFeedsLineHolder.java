package br.com.cardapia.company.acessmap.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

import org.apache.commons.lang3.StringUtils;

import br.com.cardapia.company.acessmap.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class TwitterFeedsLineHolder extends RecyclerView.ViewHolder {

    public CircleImageView imgUser;
    public TextView description;
    public TextView userData;
    public CardView cardItem;

    private Context context;

    public TwitterFeedsLineHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imgUser = itemView.findViewById(R.id.img_feed_user);
        description = itemView.findViewById(R.id.cell_feed_descriptions);
        userData = itemView.findViewById(R.id.cell_feed_user_data);
        cardItem = itemView.findViewById(R.id.card_item_feed);
    }

}