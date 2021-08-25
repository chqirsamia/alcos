package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alcos.Model.Produit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailActivity extends AppCompatActivity {
private FloatingActionButton addtocart;
private ImageView imageView;
    String  currentUserID;
CardView card1,card2,colorvalues,colorvalues2;
private ScrollableNumberPicker qty;
private TextView nom,description,prix,option,option2;
private String pid="",qte="",update="",type="",type2="",id="",id2="";

    private FirebaseAuth Auth;
    private String downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,CartRef;
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5,radioButton6,radioButton7,radioButton8,radioButton;
    RadioGroup radioGroup1,radioGroup2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        pid=getIntent().getStringExtra("pid");
        qte=getIntent().getStringExtra("qty");
        type=getIntent().getStringExtra("type");
        type2=getIntent().getStringExtra("type2");
        update=getIntent().getStringExtra("update");
        id=getIntent().getStringExtra("id");
        id2=getIntent().getStringExtra("id2");
        addtocart=(FloatingActionButton) findViewById(R.id.add);
        imageView=findViewById(R.id.image);
        nom=findViewById(R.id.nom);
        description=findViewById(R.id.description);
        prix=findViewById(R.id.prix);
        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
radioGroup1=findViewById(R.id.values2);
radioGroup2=findViewById(R.id.values);
colorvalues=findViewById(R.id.colorvalues);
colorvalues2=findViewById(R.id.colorvalues2);
radioButton1=findViewById(R.id.color21);
option=findViewById(R.id.option);
        option2=findViewById(R.id.option2);
        radioButton2=findViewById(R.id.color22);
        radioButton3=findViewById(R.id.color23);
        radioButton4=findViewById(R.id.color24);
        radioButton5=findViewById(R.id.color1);
        radioButton6=findViewById(R.id.color2);
        radioButton7=findViewById(R.id.color3);
        radioButton8=findViewById(R.id.color4);

        getDetails(pid);
        qty=findViewById(R.id.qty);
        Auth= FirebaseAuth.getInstance();
        currentUserID= Auth.getCurrentUser().getUid();
addtocart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        addtocart();
    }
});
    }

    private void addtocart() {
        String savedate, savetime;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        savedate = currentDate.format(cal.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        savetime = currentTime.format(cal.getTime());
        CartRef = FirebaseDatabase.getInstance().getReference().child("Cart");
        HashMap<String, Object> productMap = new HashMap<>();
        if (qty.getValue()==0)

        { Toast.makeText(ProductDetailActivity.this, "please enter quantity", Toast.LENGTH_SHORT).show();
        }
    else{
            productMap.put("pid", pid);
            productMap.put("name", nom.getText().toString());
            productMap.put("price", prix.getText().toString());
            productMap.put("date", savedate);
            productMap.put("time", savetime);
            System.out.println("qty" + qty.getValueView());
            System.out.println("qty" + qty.getValue());
            productMap.put("qty", String.valueOf(qty.getValue()));
            productMap.put("discount", downloadImageUrl);
            if(card1.getVisibility()==View.VISIBLE)
            { int id=radioGroup1.getCheckedRadioButtonId();
            System.out.println("iddi"+id);
                radioButton=findViewById(id);
                productMap.put("value", String.valueOf( radioButton.getCurrentTextColor()));
                System.out.println("typi"+type);
                productMap.put("type",type);
                productMap.put("radioid", String.valueOf(id));
            }
            else {productMap.put("type","nothing");
                productMap.put("radioid", "10");
            }
            if(card2.getVisibility()==View.VISIBLE) {
                int id=radioGroup2.getCheckedRadioButtonId();
                radioButton=findViewById(id);
                productMap.put("value2", String.valueOf( radioButton.getCurrentTextColor()));
                productMap.put("type2",type2);
                productMap.put("radioid2", String.valueOf(id));
            }
            else {productMap.put("type2","nothing");
                productMap.put("radioid2", "10");
            }
            ProductsRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (Integer.parseInt(snapshot.child("quantite").getValue().toString()) >= qty.getValue()) {
                        CartRef.child("userView").child(currentUserID).child("products").child(pid).updateChildren(productMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            CartRef.child("adminView").child(currentUserID).child("productsnv")
                                                    .child(pid).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ProductDetailActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                                                        if (update != null) {
                                                            Intent intent = new Intent(ProductDetailActivity.this, PanierActivity.class);

                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);

                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                        ProductsRef.child(pid).child("quantite").setValue(String.valueOf(Integer.parseInt(snapshot.child("quantite").getValue().toString()) - qty.getValue()));
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Quantite insuffisante veuillez ne pas depasser" + snapshot.child("quantite").getValue().toString(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }



    private void getDetails(String pid){
            ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
            ProductsRef.child(pid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        Produit prod = snapshot.getValue(Produit.class);
                        nom.setText(prod.getNom());
                        description.setText(prod.getDescription());
                        prix.setText(prod.getPrix());
                        if (qte != null)
                            qty.setValue(Integer.parseInt(qte));

                        Picasso.get().load(prod.getImage()).into(imageView);
                        if (prod.getValue() != null) {
                            card1.setVisibility(View.VISIBLE);
                            option.setText(prod.getOption());
                            String[] list = new String[4];
                            list = prod.getValue().split(" ");
                            int i = list.length;
                            if (i == 3) {

                                if (prod.getType().equals("color")) {
                                    radioButton1.setTextColor(Integer.parseInt(list[1]));
                                    radioButton2.setTextColor(Integer.parseInt(list[2]));

                                    radioButton1.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton2.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton3.setVisibility(View.GONE);
                                    radioButton4.setVisibility(View.GONE);
                                    System.out.println(Integer.parseInt(list[2]) + "coulour");
                                } else {
                                    radioButton1.setText(list[1]);
                                    radioButton2.setText(list[2]);
                                    radioButton3.setVisibility(View.GONE);
                                    radioButton4.setVisibility(View.GONE);
                                }

                            } else if (i == 4) {
                                if (prod.getType().equals("color")) {
                                    radioButton1.setTextColor(Integer.parseInt(list[1]));
                                    radioButton2.setTextColor(Integer.parseInt(list[2]));
                                    radioButton3.setTextColor(Integer.parseInt(list[3]));

                                    radioButton1.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton2.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton3.setBackgroundColor(Integer.parseInt(list[3]));
                                    ;
                                    radioButton4.setVisibility(View.GONE);
                                    System.out.println(Integer.parseInt(list[2]) + "coulour");
                                } else {
                                    radioButton1.setText(list[1]);
                                    radioButton2.setText(list[2]);
                                    radioButton3.setText(list[3]);

                                    radioButton4.setVisibility(View.GONE);
                                }

                            } else {
                                if (prod.getType().equals("color")) {
                                    radioButton1.setTextColor(Integer.parseInt(list[1]));
                                    radioButton2.setTextColor(Integer.parseInt(list[2]));
                                    radioButton3.setTextColor(Integer.parseInt(list[3]));
                                    radioButton4.setTextColor(Integer.parseInt(list[4]));

                                    radioButton1.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton2.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton3.setBackgroundColor(Integer.parseInt(list[3]));
                                    radioButton4.setBackgroundColor(Integer.parseInt(list[4]));
                                    System.out.println(Integer.parseInt(list[2]) + "coulour");
                                } else {
                                    radioButton1.setText(list[1]);
                                    radioButton2.setText(list[2]);
                                    radioButton3.setText(list[3]);
                                    radioButton4.setText(list[4]);
                                }

                            }

                        }
                        if(id!=null)
                        radioGroup1.check(Integer.parseInt(id));
                        if (id2!=null)
                        radioGroup2.check(Integer.parseInt(id2));
                        if (prod.getValue2() != null) {
                            card2.setVisibility(View.VISIBLE);
                            option2.setText(prod.getOption2());
                            String[] list = new String[4];
                            list = prod.getValue2().split(" ");
                            int i = list.length;
                            if (i == 3) {
                                if (prod.getType2().equals("color")) {
                                    radioButton5.setTextColor(Integer.parseInt(list[1]));
                                    radioButton6.setTextColor(Integer.parseInt(list[2]));

                                    radioButton5.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton6.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton7.setVisibility(View.GONE);
                                    radioButton8.setVisibility(View.GONE);
                                } else {
                                    radioButton5.setText(list[1]);
                                    radioButton6.setText(list[2]);
                                    radioButton7.setVisibility(View.GONE);
                                    radioButton8.setVisibility(View.GONE);
                                }

                            } else if (i == 4) {
                                if (prod.getType2().equals("color")) {
                                    radioButton5.setTextColor(Integer.parseInt(list[1]));
                                    radioButton6.setTextColor(Integer.parseInt(list[2]));
                                    radioButton7.setTextColor(Integer.parseInt(list[3]));



                                    radioButton5.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton6.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton7.setBackgroundColor(Integer.parseInt(list[3]));
                                    radioButton8.setVisibility(View.GONE);
                                } else {
                                    radioButton5.setText(list[1]);
                                    radioButton6.setText(list[2]);
                                    radioButton7.setText(list[3]);
                                    radioButton8.setVisibility(View.GONE);
                                }

                            } else {
                                if (prod.getType().equals("color")) {
                                    radioButton5.setTextColor(Integer.parseInt(list[1]));
                                    radioButton6.setTextColor(Integer.parseInt(list[2]));
                                    radioButton7.setTextColor(Integer.parseInt(list[3]));
                                    radioButton8.setTextColor(Integer.parseInt(list[4]));


                                    radioButton5.setBackgroundColor(Integer.parseInt(list[1]));
                                    radioButton6.setBackgroundColor(Integer.parseInt(list[2]));
                                    radioButton7.setBackgroundColor(Integer.parseInt(list[3]));
                                    radioButton8.setBackgroundColor(Integer.parseInt(list[4]));
                                } else {
                                    radioButton5.setText(list[1]);
                                    radioButton6.setText(list[2]);
                                    radioButton7.setText(list[3]);
                                    radioButton8.setText(list[4]);
                                }

                            }

                        }
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }
}