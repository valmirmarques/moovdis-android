package br.com.cardapia.company.acessmap.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.cardapia.company.acessmap.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class LastMarkersLineHolder extends RecyclerView.ViewHolder {

    public TextView description;
    public ImageView imgMarcacao;
    public TextView addreess;
    public TextView comments;
    public ImageView imgIcon;

    private Context context;

    public LastMarkersLineHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        description = itemView.findViewById(R.id.cell_markes_descriptions);
        imgMarcacao = itemView.findViewById(R.id.img_marker);
        addreess = itemView.findViewById(R.id.cell_markes_addr);
        comments = itemView.findViewById(R.id.cell_markes_comment);
        imgIcon = itemView.findViewById(R.id.cell_markes_icon);
    }

    public Context getContext() {
        return context;
    }
}