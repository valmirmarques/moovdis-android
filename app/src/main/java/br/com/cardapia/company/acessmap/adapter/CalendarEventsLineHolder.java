package br.com.cardapia.company.acessmap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.cardapia.company.acessmap.R;

public class CalendarEventsLineHolder extends RecyclerView.ViewHolder {

    public TextView description;
    public ImageView imgMarcacao;
    public TextView addreess;
    public TextView facebook;
    public TextView date;

    private Context context;

    public CalendarEventsLineHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        description = itemView.findViewById(R.id.cell_event_descriptions);
        imgMarcacao = itemView.findViewById(R.id.img_event);
        addreess = itemView.findViewById(R.id.cell_event_addr);
        facebook = itemView.findViewById(R.id.cell_event_facebook);
        date = itemView.findViewById(R.id.cell_event_date);
    }

    public Context getContext() {
        return context;
    }
}