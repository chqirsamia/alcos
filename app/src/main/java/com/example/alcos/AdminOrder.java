package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alcos.Model.Order;
import com.example.alcos.Model.Panier;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminOrder extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView, rv;
    RecyclerView.LayoutManager layoutManager, lm;
    DatabaseReference ref;
    private FirebaseAuth Auth;
    String currentUserId;
    DatabaseReference OrderRef,UserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AdminOrder.this);
        recyclerView.setLayoutManager(layoutManager);
        Auth = FirebaseAuth.getInstance();
        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUserId = Auth.getCurrentUser().getUid();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(OrderRef.orderByChild("etat").startAt("nv"), Order.class)
                        .build();

        final FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter
                = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
                if (model.getEtat().equals("nv")) {
                    holder.nom.setText("Nom: " + model.getName());
                    holder.prenom.setText("Prenom: " + model.getPrenom());
                    holder.adresse.setText("Adresse: " + model.getAdresse());
                    holder.telephone.setText("Telephone: " + model.getPhone());
                    holder.date.setText("Date:" + model.getDate() + " " + model.getTime());
                    holder.email.setText("Email: " + model.getEmail());
                    holder.total.setText("Total: " + model.getTotal() + "DH");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "OUI","EXIT"
                                    };
                            AlertDialog.Builder builder=new AlertDialog.Builder(AdminOrder.this);
                            builder.setTitle("Voulez vous validez cette commande pour la livrer");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                  if(i==0)
                                  {
                                     String email=model.getEmail();

                                        System.out.println("email"+email);
                                      JavaMailAPI sendemail=new JavaMailAPI(AdminOrder.this,email,"Traitement de votre commande","Votre commande vient d'etre traitée vous receverez votre colis aujaurd'hui ,le livreur vous appellera....");
                                      sendemail.execute();

                                      OrderRef.child(model.getId()+model.getUid()).child("etat").setValue("v");
                                  }

                                }
                            });
                            builder.show();
                        }
                    });
                    holder.showprod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AdminOrder.this, AdminDisplayProductsActivity.class);
                            intent.putExtra("uid", model.getUid());
                            intent.putExtra("id", model.getId());
                            System.out.println("uid" + model.getUid());
                            System.out.println("uid" + model.getId());

                            startActivity(intent);

                        }
                    });
                }
                else{
                    holder.nom.setVisibility(View.GONE);
                    holder.prenom.setVisibility(View.GONE);
                    holder.adresse.setVisibility(View.GONE);
                    holder.telephone.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.email.setVisibility(View.GONE);
                    holder.total.setVisibility(View.GONE);
                    holder.rl.setVisibility(View.GONE);
                    holder.card.setVisibility(View.GONE);
                }
            }
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                OrderViewHolder viewHolder = new OrderViewHolder(view);
                return viewHolder;
            }


};
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }}