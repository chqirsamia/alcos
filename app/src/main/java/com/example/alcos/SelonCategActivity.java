package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alcos.Model.Categorie;
import com.example.alcos.Model.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelonCategActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ProductsRef;
    private DatabaseReference RootRef2;
    RecyclerView.LayoutManager layoutManager,lm;
    private RecyclerView recyclerView,rv;
    private RecyclerView recyclerView1;
    ArrayList<Produit> list;
    ArrayList<Categorie> listC;
    ImageView searchviewim;
    EditText searchView;
    String input,categ=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selon_categ);
        searchView=findViewById(R.id.searchview);
        searchviewim=findViewById(R.id.searchim);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        categ=getIntent().getStringExtra("categ");


        searchviewim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input=searchView.getText().toString();
                FirebaseRecyclerOptions<Produit> options =
                        new FirebaseRecyclerOptions.Builder<Produit>()
                                // .setQuery(ProductsRef, Produit.class)
                                .setQuery(ProductsRef.orderByChild("nom").startAt(input), Produit.class)
                                .build();


                FirebaseRecyclerAdapter<Produit, ProduitViewHolder> adapter =
                        new FirebaseRecyclerAdapter<Produit, ProduitViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ProduitViewHolder holder, int position, @NonNull Produit model) {
                                if (Integer.parseInt(model.getQuantite()) > 0 && model.getCategorie().equals(categ)) {
                                    holder.txtProductName.setText(model.getNom());
                                    holder.txtProductDescription.setText(model.getDescription());
                                    holder.txtProductPrice.setText("Price = " + model.getPrix() + "DH");
                                    //holder.qty.setText(model.getQuantite());
                                    holder.qty.setVisibility(View.INVISIBLE);
                                    Picasso.get().load(model.getImage()).into(holder.imageView);


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(SelonCategActivity.this, ProductDetailActivity.class);
                                            intent.putExtra("pid", model.getId_produit());
                                            System.out.println("pid" + model.getId_produit());
                                            startActivity(intent);

                                        }
                                    });
                                }
                                else{
                                    holder.txtProductName.setVisibility(View.INVISIBLE);
                                    holder.txtProductDescription.setVisibility(View.INVISIBLE);
                                    holder.txtProductPrice.setVisibility(View.INVISIBLE);

                                    holder.qty.setVisibility(View.INVISIBLE);
                                    holder.imageView.setVisibility(View.INVISIBLE);
                                    holder.constraintLayout.setVisibility(View.INVISIBLE);
                                }
                            }



                            @NonNull
                            @Override
                            public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout, parent, false);
                                ProduitViewHolder holder = new ProduitViewHolder(view);
                                return holder;
                            }
                        };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SelonCategActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        // .setQuery(ProductsRef.orderByChild("nom").startAt(input), Produit.class)


        FirebaseRecyclerOptions<Produit> options =
                new FirebaseRecyclerOptions.Builder<Produit>()
                        // .setQuery(ProductsRef, Produit.class)
                        .setQuery(ProductsRef.orderByChild("categorie").startAt(categ), Produit.class)
                        .build();


        FirebaseRecyclerAdapter<Produit, ProduitViewHolder> adapter =
                new FirebaseRecyclerAdapter<Produit, ProduitViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProduitViewHolder holder, int position, @NonNull Produit model) {
                        if (Integer.parseInt(model.getQuantite()) > 0 && model.getCategorie().equals(categ)) {
                            holder.txtProductName.setText(model.getNom());
                            holder.txtProductDescription.setText(model.getDescription());
                            holder.txtProductPrice.setText("Price = " + model.getPrix() + "DH");
                            //holder.qty.setText(model.getQuantite());
                            holder.qty.setVisibility(View.INVISIBLE);
                            Picasso.get().load(model.getImage()).into(holder.imageView);


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SelonCategActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("pid", model.getId_produit());
                                    System.out.println("pid" + model.getId_produit());
                                    startActivity(intent);

                                }
                            });
                        }
                       else{
                            holder.txtProductName.setVisibility(View.INVISIBLE);
                            holder.txtProductDescription.setVisibility(View.INVISIBLE);
                            holder.txtProductPrice.setVisibility(View.INVISIBLE);

                            holder.qty.setVisibility(View.INVISIBLE);
                            holder.imageView.setVisibility(View.INVISIBLE);
                            holder.constraintLayout.setVisibility(View.INVISIBLE);
                        }
                        }



                    @NonNull
                    @Override
                    public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout, parent, false);
                        ProduitViewHolder holder = new ProduitViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    }