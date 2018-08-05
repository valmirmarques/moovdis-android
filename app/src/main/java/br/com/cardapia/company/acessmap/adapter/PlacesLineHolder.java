package br.com.cardapia.company.acessmap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesLineHolder extends RecyclerView.ViewHolder implements MoovDisContants {

    public TextView description;
    public ImageView imgMarcacao;
    public TextView addreess;
    public TextView categoria;
    public TextView distance;
    public Button btnRate;
    public CircleImageView iconAcessible;
    public LinearLayout layoutBottom;

    private Context context;

    public PlacesLineHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        description = itemView.findViewById(R.id.cell_places_descriptions);
        imgMarcacao = itemView.findViewById(R.id.img_place);
        addreess = itemView.findViewById(R.id.cell_place_addr);
        categoria = itemView.findViewById(R.id.cell_place_categoria);
        distance = itemView.findViewById(R.id.cell_place_distance);
        btnRate = itemView.findViewById(R.id.angry_btn);
        iconAcessible = itemView.findViewById(R.id.is_place_accessible);
        layoutBottom = itemView.findViewById(R.id.linear_layout_places_local);

        ButterKnife.bind(itemView);

    }


    public Context getContext() {
        return context;
    }
}