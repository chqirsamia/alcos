package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PanierActivity extends AppCompatActivity {
    private DatabaseReference ProductsRef, CategoriesRef;
    private RecyclerView recyclerView, rv;
    RecyclerView.LayoutManager layoutManager, lm;
    DatabaseReference ref;
    private FirebaseAuth Auth;
    Button next;
    TextView total,text,value,value2;
    ImageView validate;
    String currentUserId,pid,email,message="",val="",val2="";
    DatabaseReference PanierRef ,OrderRef,UserRef,CartRef,AdminRef;
    Float totali= 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(PanierActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        next = findViewById(R.id.next);
        total = findViewById(R.id.total);
        validate = findViewById(R.id.delete);
        text = findViewById(R.id.text);


        Auth = FirebaseAuth.getInstance();
        String savedate,saveDate,savetime;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentdate =new SimpleDateFormat("dd:MM:yyyy");
        savedate=currentDate.format(cal.getTime());
        saveDate=currentdate.format(cal.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss");
        savetime=currentTime.format(cal.getTime());
      // OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        AdminRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("adminView");
        PanierRef = FirebaseDatabase.getInstance().getReference().child("Cart");

next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String savedate,saveDate,savetime;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentdate =new SimpleDateFormat("dd:MM:yyyy");
        savedate=currentDate.format(cal.getTime());
        saveDate=currentdate.format(cal.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss");
        savetime=currentTime.format(cal.getTime());
        String id=saveDate+savetime;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println(user + "hello");
            Intent loginIntent = new Intent(PanierActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {

            currentUserId = Auth.getCurrentUser().getUid();
            UserRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    message = message + " cher " + snapshot.child("nom").getValue().toString() + " " + snapshot.child("prenom").getValue().toString()
                            + "\n Merci d'avoir acheté chez nous,vous avez fait une commande le " + saveDate + " à " + savetime + "\n le colis vous sera envoyé dans l'adresse suivante: "
                            + snapshot.child("adresse").getValue().toString() + "\n Les produits: \n";
                    System.out.println(message+"message");
                    email=snapshot.child("email").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            System.out.println(id + "je suis dans confirmer");
AdminRef.child(currentUserId).child("productsnv").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        System.out.println("samsam"+snapshot);
        if(snapshot.exists()){
            System.out.println("samsam"+snapshot);
            int i=0;
            for(DataSnapshot ds:snapshot.getChildren() )
            {
           i++;
                System.out.println("samsam"+ds);
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("date").setValue(ds.child("date").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("name").setValue(ds.child("name").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("pid").setValue(ds.child("pid").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("price").setValue(ds.child("price").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("qty").setValue(ds.child("qty").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("time").setValue(ds.child("time").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("radioid").setValue(ds.child("radioid").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("radioid2").setValue(ds.child("radioid2").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("type").setValue(ds.child("type").getValue().toString());
                AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("type2").setValue(ds.child("type2").getValue().toString());

                if(ds.child("value").exists()) {
                    AdminRef.child(currentUserId).child("products").child(id + currentUserId).child(ds.getKey().toString()).child("value").setValue(ds.child("value").getValue().toString());
                    AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("type").setValue(ds.child("type").getValue().toString());

                }
                if(ds.child("values2").exists()) {
                    AdminRef.child(currentUserId).child("products").child(id + currentUserId).child(ds.getKey().toString()).child("value2").setValue(ds.child("value2").getValue().toString());
                    AdminRef.child(currentUserId).child("products").child(id+currentUserId).child(ds.getKey().toString()).child("type2").setValue(ds.child("type2").getValue().toString());

                }
                message=message+"article n: "+i+" "+ds.child("name").getValue().toString()+" avec une quantite de "+ds.child("qty").getValue().toString()+"pour le prix de:"+ds.child("price").getValue().toString()+"\n";

            }

            JavaMailAPI sendemail=new JavaMailAPI(PanierActivity.this,email,"Details de votre commande",message);
            sendemail.execute();
            System.out.println(message+"messageadmin");
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
         //
            OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(id+currentUserId);
        UserRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> Order = new HashMap<>();
                System.out.println(user + "la je suis");
                Order.put("total",String.valueOf(totali));
                Order.put("uid",currentUserId);
                Order.put("id",id);
                Order.put("name", snapshot.child("nom").getValue().toString());
                Order.put("prenom", snapshot.child("prenom").getValue().toString());
                Order.put("date", savedate);
                Order.put("time", savetime);
                Order.put("adresse", snapshot.child("adresse").getValue().toString());
                Order.put("email", snapshot.child("email").getValue().toString());
                Order.put("phone",snapshot.child("phone").getValue().toString());
                Order.put("etat","nv");

                OrderRef.updateChildren(Order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseDatabase.getInstance().getReference().child("Cart")
                                    .child("userView").child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(PanierActivity.this, ConfirmActivity.class);

                                        startActivity(intent);
                                        AdminRef.child(currentUserId).child("productsnv").removeValue();
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }}




});

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println(user + "hello");
            Intent loginIntent = new Intent(PanierActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            checkState();
            currentUserId = Auth.getCurrentUser().getUid();
            FirebaseRecyclerOptions options =
                    new FirebaseRecyclerOptions.Builder<Panier>()
                            .setQuery(PanierRef.child("userView").child(currentUserId).child("products"), Panier.class)
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
              /*      OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUserId).child(id);
OrderRef.child("products").child(model.getPid()).child("nom").setValue(model.getName());
                    OrderRef.child("products").child(model.getPid()).child("price").setValue(model.getPrice());
                    OrderRef.child("products").child(model.getPid()).child("qty").setValue(model.getQty());*/
                  Float totalperprod=Float.parseFloat(model.getPrice())*Integer.parseInt(model.getQty());
                  totali=totalperprod+totali;
total.setText("total="+String.valueOf(totali));
                    holder.update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("pid"+pid);
                            Intent intent2  = new Intent(PanierActivity.this,ProductDetailActivity.class);
                            intent2.putExtra("pid",model.getPid());
                            intent2.putExtra("qty",model.getQty());
                            intent2.putExtra("id",model.getRadioid());
                            intent2.putExtra("id2",model.getRadioid2());
                            intent2.putExtra("update","1");
                            intent2.putExtra("type",model.getType());
                            intent2.putExtra("type2",model.getType2());
                            startActivity(intent2);

                        }
                    });

                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentUserId = Auth.getCurrentUser().getUid();
                            PanierRef.child("userView").child(currentUserId).child("products").child(model.getPid())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(PanierActivity.this, "Product was deleted successfully..", Toast.LENGTH_SHORT).show();
                                       Intent intent2  = new Intent(PanierActivity.this,PanierActivity.class);
                                        intent2.putExtra("pid",pid);
                                        startActivity(intent2);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }


            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }
    }
    public void checkState()
    {
        currentUserId = Auth.getCurrentUser().getUid();
        CartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("userView");

        CartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentUserId).exists())
                {

                    total.setVisibility(View.VISIBLE);
                    total.setText("total=" +totali+"DH");
                    recyclerView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    validate.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                }
                else{
                    total.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    validate.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}