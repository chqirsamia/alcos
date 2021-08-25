package com.example.alcos;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PanierVieHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, qty, txtProductPrice,total;
    //public ImageView imageView;
    Button showprod;
    ImageView update,delete;
    TextView value,value2;

    public ItemClickListner listner;


    public PanierVieHolder(View itemView)
    {
        super(itemView);
showprod=itemView.findViewById(R.id.showprod);

        //imageView = (ImageView) itemView.findViewById(R.id.imageView3);
        txtProductName = (TextView) itemView.findViewById(R.id.nom);
        qty = (TextView) itemView.findViewById(R.id.qty);
        txtProductPrice = (TextView) itemView.findViewById(R.id.prix);
        update=itemView.findViewById(R.id.edit);
        delete=itemView.findViewById(R.id.delete);
        value=itemView.findViewById(R.id.values);
        value2=itemView.findViewById(R.id.values2);
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