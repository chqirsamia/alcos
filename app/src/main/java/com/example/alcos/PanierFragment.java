package com.example.alcos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alcos.Model.Panier;
import com.example.alcos.Model.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PanierFragment extends Fragment {

    private DatabaseReference ProductsRef, CategoriesRef;
    private RecyclerView recyclerView, rv;
    RecyclerView.LayoutManager layoutManager, lm;
    DatabaseReference ref;
    private FirebaseAuth Auth;
    Button next;
    TextView total;
    String currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_panier, container, false);

        recyclerView = root.findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        next = root.findViewById(R.id.next);
        total = root.findViewById(R.id.total);
        Auth = FirebaseAuth.getInstance();


        return inflater.inflate(R.layout.fragment_panier, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference PanierRef = FirebaseDatabase.getInstance().getReference().child("Cart");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println(user + "hello");
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        } else {
            currentUserId = Auth.getCurrentUser().getUid();
            FirebaseRecyclerOptions options =
                    new FirebaseRecyclerOptions.Builder<Panier>()
                            .setQuery(PanierRef.child("userView").child(currentUserId).child("products"), Panier.class)
                            .build();

            final FirebaseRecyclerAdapter<Panier, PanierVieHolder> adapter
                    = new FirebaseRecyclerAdapter<Panier, PanierVieHolder>(options) {
                @NonNull
                @Override
                public PanierVieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panier_items_layout, viewGroup, false);
                    PanierVieHolder viewHolder = new PanierVieHolder(view);
                    return viewHolder;
                }

                @Override
                protected void onBindViewHolder(@NonNull PanierVieHolder holder, int position, @NonNull Panier model) {
                    holder.txtProductName.setText(model.getName());
                    holder.qty.setText(model.getQty());
                    holder.txtProductPrice.setText("Prix = " + model.getPrice() + "DH");
                }


            };
recyclerView.setAdapter(adapter);
            adapter.startListening();

        }
    }
}