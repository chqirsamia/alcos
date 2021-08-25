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

import com.example.alcos.Model.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommandesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_commandes);
        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CommandesActivity.this);
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
                        .setQuery(OrderRef, Order.class)
                        .build();

        final FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter
                = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
                if (model.getUid().equals(currentUserId)) {
                    holder.nom.setVisibility(View.GONE);
                    holder.prenom.setVisibility(View.GONE);
                    holder.adresse.setVisibility(View.GONE);
                    holder.telephone.setVisibility(View.GONE);
                    holder.date.setText("Date:" + model.getDate() + " " + model.getTime());
                    holder.email.setVisibility(View.GONE);
                    holder.total.setText("Total: " + model.getTotal() + "DH");

                    holder.showprod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CommandesActivity.this, AdminDisplayProductsActivity.class);
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
    }
}