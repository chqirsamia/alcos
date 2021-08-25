package com.example.alcos;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView adresse,nom,prenom,email,telephone,date,total;
public Button showprod;
RelativeLayout rl;
    androidx.cardview.widget.CardView card;
    public ItemClickListner listner;


    public OrderViewHolder (View itemView)
    {
        super(itemView);
rl=itemView.findViewById(R.id.rl);
card=itemView.findViewById(R.id.card);
        total = (TextView) itemView.findViewById(R.id.total);
        date = (TextView) itemView.findViewById(R.id.date);
        nom = (TextView) itemView.findViewById(R.id.nom);
        prenom = (TextView) itemView.findViewById(R.id.prenom);
        adresse = (TextView) itemView.findViewById(R.id.adresse);
        email=itemView.findViewById(R.id.email);
        telephone=itemView.findViewById(R.id.phone);
        showprod=itemView.findViewById(R.id.showprod);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
