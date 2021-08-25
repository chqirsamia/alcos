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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alcos.Model.Panier;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminDisplayProductsActivity extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView, rv;
    RecyclerView.LayoutManager layoutManager, lm;
    private FirebaseAuth Auth;
    private String id="",uid="";
    TextView total;
    String currentUserId,pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_products);
        uid=getIntent().getStringExtra("uid");
        id=getIntent().getStringExtra("id");
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Cart").child("adminView").child(uid).child("products").child(id+uid);
        System.out.println(ProductsRef.getKey()+"refe");
recyclerView=findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AdminDisplayProductsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public void onStart() {
        super.onStart();
            FirebaseRecyclerOptions options =
                    new FirebaseRecyclerOptions.Builder<Panier>()
                            .setQuery(ProductsRef, Panier.class)
                            .build();

            final FirebaseRecyclerAdapter<Panier, PanierVieHolder> adapter
                    = new FirebaseRecyclerAdapter<Panier, PanierVieHolder>(options) {
                @NonNull
                @Override
                public PanierVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.panier_items_layout, parent, false);
                    PanierVieHolder viewHolder = new PanierVieHolder(view);
                    return viewHolder;
                }

                @Override
                protected void onBindViewHolder(@NonNull PanierVieHolder holder, int position, @NonNull Panier model) {
                    String savedate,saveDate,savetime;
                    Calendar cal=Calendar.getInstance();
                    SimpleDateFormat currentDate =new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat currentdate =new SimpleDateFormat("dd:MM:yyyy");
                    savedate=currentDate.format(cal.getTime());
                    saveDate=currentdate.format(cal.getTime());

                    SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss");
                    savetime=currentTime.format(cal.getTime());
                    String id=saveDate+savetime;
                    holder.txtProductName.setText("Produit: "+model.getName());
                    holder.qty.setText("Quantite: "+model.getQty());
                    holder.txtProductPrice.setText( "Prix:"+model.getPrice() +"DH");
                    if(model.getType().equals("nothing"))
                        holder.value.setVisibility(View.GONE);
                    else
                    {
                        if (model.getType().equals("color"))
                            holder.value.setBackgroundColor(Integer.parseInt(model.getValue()));
                        else
                            holder.value.setText(Integer.parseInt(model.getValue()));
                    }
                    System.out.println("type2"+model.getType2());
                    if(model.getType2().equals("nothing"))
                        holder.value2.setVisibility(View.GONE);

                    else
                    {
                        if(model.getType2().equals("color"))
                            holder.value2.setBackgroundColor(Integer.parseInt(model.getValue2()));
                        else
                            holder.value2.setText(Integer.parseInt(model.getValue2()));}
                    pid=model.getPid();

                    holder.update.setVisibility(View.GONE);

                    holder.delete.setVisibility(View.GONE);
                }


            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }

}