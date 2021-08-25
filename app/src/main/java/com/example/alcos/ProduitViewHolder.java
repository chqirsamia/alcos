package com.example.alcos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alcos.Model.Produit;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProduitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice,qty;
    public ImageView imageView;
   CardView constraintLayout ;
    public ItemClickListner listner;


    public ProduitViewHolder(View itemView)
    {
        super(itemView);

constraintLayout=itemView.findViewById(R.id.cardv);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
qty=itemView.findViewById(R.id.qty);

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
        /*extends RecyclerView.Adapter<ProduitViewHolder.MyViewHolder>
{
    ArrayList<Produit> list;


    public ProduitViewHolder(ArrayList<Produit> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String[] retImage = {"default_image"};
*/
       // retImage[0] = list.get(position).getImage();
      /*  System.out.println("he"+retImage[0]);

        String nom = list.get(position).getNom();
        final String retnom=nom;
        String des= list.get(position).getDescription();
        final String description = des;
        holder.nom.setText(retnom);
        holder.description.setText(description);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImage;
        TextView nom,description;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            profileImage = itemView.findViewById(R.id.imageView3);
            nom = itemView.findViewById(R.id.nom);
            description = itemView.findViewById(R.id.description);
        }


    }

}*/
