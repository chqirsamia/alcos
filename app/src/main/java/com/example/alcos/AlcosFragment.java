package com.example.alcos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.alcos.Model.Categorie;
import com.example.alcos.Model.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AlcosFragment extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ProductsRef;
    private DatabaseReference RootRef2,users;
    RecyclerView.LayoutManager layoutManager,lm;
    private RecyclerView recyclerView,rv;
    private RecyclerView recyclerView1;
    ArrayList<Produit> list;
    ArrayList<Categorie> listC;
    ImageView searchviewim;
    EditText searchView;
    String input;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_alcos,container,false);
searchView=root.findViewById(R.id.searchview);
        searchviewim=root.findViewById(R.id.searchim);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef2 = FirebaseDatabase.getInstance().getReference().child("Categories");

searchviewim.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            input=searchView.getText().toString();
            onStart();
    }
});

        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        rv=root.findViewById(R.id.categories);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);
        return root;

    }

    @Override
    public void onStart()
    {
        super.onStart();

                       // .setQuery(ProductsRef.orderByChild("nom").startAt(input), Produit.class)


        FirebaseRecyclerOptions<Produit> options =
                new FirebaseRecyclerOptions.Builder<Produit>()
                       // .setQuery(ProductsRef, Produit.class)
                        .setQuery(ProductsRef.orderByChild("nom").startAt(input), Produit.class)
                        .build();


        FirebaseRecyclerAdapter<Produit, ProduitViewHolder> adapter =
                new FirebaseRecyclerAdapter<Produit, ProduitViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProduitViewHolder holder, int position, @NonNull Produit model)
                    {if(Integer.parseInt(model.getQuantite())>0)
                    { holder.txtProductName.setText(model.getNom());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Prix = " + model.getPrix() + "DH");
                        //holder.qty.setText(model.getQuantite());
                        holder.qty.setVisibility(View.INVISIBLE);
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                intent.putExtra("pid",model.getId_produit());
                                intent.putExtra("type",model.getType());
                                intent.putExtra("type2",model.getType2());

                                System.out.println("pid"+model.getId_produit());
                                if(Integer.parseInt(model.getQuantite())<10)
                                {
                                    users.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.child("role").toString().equals("admin"))
                                            {
                                                JavaMailAPI sendemail=new JavaMailAPI(getContext(),snapshot.child("email").getValue().toString(),"Alerte insuffisance de produit",
                                                       "le produit "+model.getNom()+" de code "+
                                                        model.getCode()+"a une quantite  moins de 10" );
                                                sendemail.execute();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                startActivity(intent);

                            }
                        });}}


                    @NonNull
                    @Override
                    public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout, parent, false);
                        ProduitViewHolder holder = new ProduitViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

/////////////////////////////////////////////////////////////////////
        ///////////////categories///////////////////////////////////
        FirebaseRecyclerOptions<Categorie> option =
                new FirebaseRecyclerOptions.Builder<Categorie>()
                        .setQuery(RootRef2, Categorie.class)
                        .build();


        FirebaseRecyclerAdapter<Categorie, CategorieViewHolder> adapter2 =
                new FirebaseRecyclerAdapter<Categorie, CategorieViewHolder>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategorieViewHolder holder, int position, @NonNull Categorie model)
                    {
                        holder.txtCategName.setText(model.getNom_categorie());
                        //  holder.txtCategDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), SelonCategActivity.class);
                                intent.putExtra("categ",model.getNom_categorie());

                                startActivity(intent);
                            }
                        });
                    }




                    @NonNull
                    @Override
                    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
                        CategorieViewHolder holder = new CategorieViewHolder(view);
                        return holder;
                    }
                };
        rv.setAdapter(adapter2);
        adapter2.startListening();
    }





}
