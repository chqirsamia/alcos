package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alcos.Model.Categorie;
import com.example.alcos.Model.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //********************************offer********************************//
    private DatabaseReference ProductsRef,CategoriesRef;
    private RecyclerView recyclerView,rv;
    RecyclerView.LayoutManager layoutManager,lm;
    DatabaseReference ref;
    ArrayList<Produit> list;
    private FirebaseAuth Auth;

    public static Context context;
    //****************************************************************//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        context = getApplicationContext();
        Auth= FirebaseAuth.getInstance();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        CategoriesRef = FirebaseDatabase.getInstance().getReference().child("Categories");


        //  recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ref = FirebaseDatabase.getInstance().getReference().child("Products");


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rv=findViewById(R.id.categories);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

        //***********************************************************************************************************/


        /*-----------------------------Hooks----------------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        /*-----------------------------Toolbar----------------------------------*/
       // setSupportActionBar(toolbar);
        /*-----------------------Navigation drawer Menu -------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
               Intent intent1 = new Intent(AdminActivity.this, AdminActivity.class);
                startActivity(intent1);

                return true;
            case R.id.product:
                Intent intent2 = new Intent(AdminActivity.this, AddProductActivity.class);
                startActivity(intent2);
                break;
            case R.id.categorie:
               Intent intent3 = new Intent(AdminActivity.this, AddCategoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.admin:
                Intent intent4 = new Intent(AdminActivity.this, AddAdminActivity.class);
                startActivity(intent4);
                break;


            case R.id.profil:
                  Intent mainIntent = new Intent(AdminActivity.this, Compte.class);
                  startActivity(mainIntent);

                break;
            case R.id.orders:
                Intent orderIntent = new Intent(AdminActivity.this, AdminOrder.class);
                startActivity(orderIntent);

                break;

            case R.id.logout:
                Auth.signOut();
                 Intent Intent = new Intent(AdminActivity.this, MainActivity.class);
                  startActivity(Intent);

                break;





        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Produit> options =
                new FirebaseRecyclerOptions.Builder<Produit>()
                        .setQuery(ProductsRef, Produit.class)
                        .build();


        FirebaseRecyclerAdapter<Produit, ProduitViewHolder> adapter =
                new FirebaseRecyclerAdapter<Produit, ProduitViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProduitViewHolder holder, int position, @NonNull Produit model)
                    {
                     holder.txtProductName.setText(model.getNom());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price : " + model.getPrix() + "DH");
                        holder.qty.setText("Quantité restante: "+model.getQuantite());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminActivity.this, ProductUpdateActivity.class);
                                intent.putExtra("pid",model.getId_produit());
                                System.out.println("pid"+model.getId_produit());
                                startActivity(intent);

                            }
                        });}


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




        ///////////////categories///////////////////////////////////
        FirebaseRecyclerOptions<Categorie> option =
                new FirebaseRecyclerOptions.Builder<Categorie>()
                        .setQuery(CategoriesRef, Categorie.class)
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
                                Intent intent = new Intent(AdminActivity.this, SelonCategActivity.class);
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

}}