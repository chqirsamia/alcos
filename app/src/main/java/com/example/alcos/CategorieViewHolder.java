package com.example.alcos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alcos.Model.Categorie;
import com.example.alcos.Model.Produit;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategorieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtCategName, txtCategDescription;
    public ImageView imageView;
    public ItemClickListner listner;


    public CategorieViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.imageView3);
        txtCategName = (TextView) itemView.findViewById(R.id.nom);
       // txtCategDescription = (TextView) itemView.findViewById(R.id.description);
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