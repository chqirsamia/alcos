package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alcos.Model.Categorie;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,Pqty,Pcode,Poption,Ptype,Poption2,Ptype2,Pvalues,Pvalues2;
  String colorslist="",colorslist2="";
    private Button AddNewProductButton,addnewvar;
    private ImageView InputProductImage;
    int mycolor=10;
    private EditText InputProductName, InputProductDescription, InputProductPrice,InputProductcategory,InputProductCode,InputProductQty
            ,options,values,options2,values2;
    Switch switchi;
    boolean iscolor=false,iscolor2=false;
    private static final int GalleryPick = 1;
    ArrayList<Categorie> listC;
    ArrayList<String> list,list2,Pvalue,Pvalue2;
    TextView categ,option,type,value,option2,type2,value2,color1
            ,color2,color3,color4,color21,color22,color23,color24;
    CardView card1,card2,card3,card4;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,RootRef,RootRef2;
    private ProgressDialog loadingBar;
    boolean isprix=true;
    boolean isqty=true;
Spinner spinner,spinner2,spinner3;
    int nbr=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        options=findViewById(R.id.options2);
        values=findViewById(R.id.values2);
        options2=findViewById(R.id.options);
        values2=findViewById(R.id.values);
        option=findViewById(R.id.option2);
        type=findViewById(R.id.type2);
       value=findViewById(R.id.value2);
        option2=findViewById(R.id.option);
        type2=findViewById(R.id.type);
        value2=findViewById(R.id.value);
        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        spinner2=findViewById(R.id.spinner2);
        addnewvar=findViewById(R.id.add_new_var);
        spinner3=findViewById(R.id.spinner3);
        color1=findViewById(R.id.color1);
        color2=findViewById(R.id.color2);
        color3=findViewById(R.id.color3);
        color4=findViewById(R.id.color4);
        color21=findViewById(R.id.color21);
        color22=findViewById(R.id.color22);
        color23=findViewById(R.id.color23);
        color24=findViewById(R.id.color24);
card3=findViewById(R.id.colorvalues2);
        card4=findViewById(R.id.colorvalues);

        switchi=findViewById(R.id.switchi);


        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        RootRef2 = FirebaseDatabase.getInstance().getReference().child("Categories");

        RootRef = FirebaseDatabase.getInstance().getReference();

                AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        categ = (TextView) findViewById(R.id.product_category);
        InputProductCode = (EditText) findViewById(R.id.product_code);
        InputProductQty = (EditText) findViewById(R.id.product_qty);
spinner=(Spinner)findViewById(R.id.spinner);
///////////////////////////////////switchi///////////////////////////////////////////

        switchi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchi.isChecked() )
                {
                    addnewvar.setVisibility(View.VISIBLE);
                    card1.setVisibility(View.VISIBLE);
                    if(type.getText().equals("color"))
                    card3.setVisibility(View.VISIBLE);
                    else
                        card3.setVisibility(View.GONE);
                }
else{
                    addnewvar.setVisibility(View.GONE);
                    card1.setVisibility(View.GONE);
                    card2.setVisibility(View.GONE);

                }
            }
        });
        /////////////////////////////////////
        addnewvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card2.setVisibility(View.VISIBLE);
                card4.setVisibility(View.GONE);
            }
        });

////////////////////////variants spinner//////////////////////////////////
        list2 = new ArrayList<>();
        list2.add("nothing");
        list2.add("color");
        list2.add("text");
        ArrayAdapter<CharSequence> adapter2=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,list2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("comeon"+list);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter2);

//////////////////////////////////colors//////////////////////////
color1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openDialog(false,color1, colorslist2);

    }
});
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,color2, colorslist2);

            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,color3, colorslist2);

            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color= openDialog(false,color4, colorslist2);

            }
        });
        color21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int color= openDialog(false,color21, colorslist);
                System.out.println("eeee"+colorslist);

            }

        });
        color22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int color=  openDialog(false,color22, colorslist);

            }
        });
        color23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,color23, colorslist);

            }
        });
        color24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDialog(false,color24,colorslist);
                         }
        });

        //////////////////////////////////////
//////////////////spinner2////////////////////////////////
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newItem = list2.get(i);
                //Toast.makeText(getApplicationContext(), "You selected: " + newItem, Toast.LENGTH_LONG).show();
                type.setText(newItem);
                if(i==1)
                {
                    iscolor=true;
                    values.setVisibility(View.GONE);
                    card3.setVisibility(View.VISIBLE);
                    value.setVisibility(View.GONE);



                }
              else {
                    iscolor=false;
                    values.setVisibility(View.VISIBLE);
                   value.setVisibility(View.VISIBLE);
                    card3.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

//////////////////spinner3///////////////////////////////
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newItem = list2.get(i);
                //Toast.makeText(getApplicationContext(), "You selected: " + newItem, Toast.LENGTH_LONG).show();
                type2.setText(newItem);
                if(iscolor)
                {
                    values.setVisibility(View.GONE);
                    card3.setVisibility(View.VISIBLE);
                    value.setVisibility(View.GONE);
                }
                else {

                    values.setVisibility(View.VISIBLE);
                    value.setVisibility(View.VISIBLE);
                    card3.setVisibility(View.GONE);
                }
                if(i==1)
                {
                    iscolor2=true;
                    values2.setVisibility(View.GONE);
                    card4.setVisibility(View.VISIBLE);
                    value2.setVisibility(View.GONE);



                }
                else {
                    iscolor2=false;
                    values2.setVisibility(View.VISIBLE);
                    value2.setVisibility(View.VISIBLE);
                    card4.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        ////////////////////////fin variants spinner//////////////////////////////
        loadingBar = new ProgressDialog(this);
        if(RootRef2 != null) {
            RootRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        listC = new ArrayList<>();
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            listC.add(ds.getValue(Categorie.class));
                            String nom=ds.child("nom_categorie").getValue().toString();
                            list.add(nom);
                            System.out.println(list+"mamamia");
                        }
                        ArrayAdapter<CharSequence> adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        System.out.println("comeon"+list);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String newItem = list.get(i);
                                //Toast.makeText(getApplicationContext(), "You selected: " + newItem, Toast.LENGTH_LONG).show();
                                categ.setText(newItem);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}

//categ.setText(spinner.getSelectedItemPosition());
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();


            }
        });
    }



    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
Pqty=InputProductQty.getText().toString();
CategoryName=categ.getText().toString();
Poption=options.getText().toString();
Poption2=options2.getText().toString();
Ptype=type.getText().toString();
Ptype2=type2.getText().toString();
Pvalues =values.getText().toString();
Pvalues2=values2.getText().toString();
        Pcode=InputProductCode.getText().toString();
        try {
            Float prix = Float.parseFloat(Price);
        } catch (NumberFormatException e) {
            isprix = false;
        }
        try {
            int qty = Integer.parseInt(Pqty);
        } catch (NumberFormatException e) {
            isqty = false;
        }

        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(CategoryName))
        {
            Toast.makeText(this, "Please write product category...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (!isprix)
        {
            Toast.makeText(this, "Please write valid price", Toast.LENGTH_SHORT).show();
            try {
                Float prix = Float.parseFloat(Price);
            } catch (NumberFormatException e) {
                isprix = false;
            }
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pcode))
        {
            Toast.makeText(this, "Please write product code...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pqty))
        {
            Toast.makeText(this, "Please write product quantity...", Toast.LENGTH_SHORT).show();
        }
        else if (!isqty)
        {
            Toast.makeText(this, "Please write a valid quantity...", Toast.LENGTH_SHORT).show();
            try {
                int qty = Integer.parseInt(Pqty);
            } catch (NumberFormatException e) {
                isqty = false;
            }
        }
        else if (TextUtils.isEmpty(Pqty))
        {
            Toast.makeText(this, "Please write product quantity...", Toast.LENGTH_SHORT).show();
        }
        else if (switchi.isChecked() && TextUtils.isEmpty(Poption))
            {
                Toast.makeText(this, "Please write the option...", Toast.LENGTH_SHORT).show();
            }
            else if (switchi.isChecked() && !iscolor &&TextUtils.isEmpty(Pvalues))
            {
                Toast.makeText(this, "Please enter values", Toast.LENGTH_SHORT).show();
            }
            else if (switchi.isChecked() &&TextUtils.isEmpty(Ptype))
            {
                Toast.makeText(this, "Please enter the type", Toast.LENGTH_SHORT).show();
            }
            else if (card2.getVisibility()==View.VISIBLE && TextUtils.isEmpty(Poption2))
                {
                    Toast.makeText(this, "Please write the option...", Toast.LENGTH_SHORT).show();
                }
                else if (card2.getVisibility()==View.VISIBLE && !iscolor2 &&TextUtils.isEmpty(Pvalues2))
                {
                    Toast.makeText(this, "Please enter values", Toast.LENGTH_SHORT).show();
                }
                else if (card2.getVisibility()==View.VISIBLE &&TextUtils.isEmpty(Ptype2))
                {
                    Toast.makeText(this, "Please enter the type", Toast.LENGTH_SHORT).show();
                }


        else {

            ProductsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        nbr=0;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.child("id_produit").getValue().toString().equals(Pcode))
                            { System.out.println("pcode"+ds.child("id_produit").getValue().toString());
                          existe();}
                        }


                        }
                    if(nbr==0)
                        StoreProductInformation();


                    }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void existe() {
        Toast.makeText(this, "this code exist already...", Toast.LENGTH_SHORT).show();
nbr=nbr+1;
    }


    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

private int openDialog(boolean support, TextView carre,final String colorslistt)
{

    AmbilWarnaDialog dialog =new AmbilWarnaDialog(this, mycolor, support, new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {

        }
        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {
         mycolor=color;
         System.out.println("coulour1"+mycolor);
        carre.setBackgroundColor(mycolor);
            if (String.valueOf(color)!=null )
                colorslist=colorslistt+" "+String.valueOf(color);
            System.out.println("coulour"+color);

        }
    });
    dialog.show();
    System.out.println("coulour2"+mycolor);
    return mycolor;
}

    private void SaveProductInfoToDatabase()
    {

                    HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("id_produit", Pcode);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("categorie", CategoryName);
        productMap.put("prix", Price);
        productMap.put("nom", Pname);
        productMap.put("quantite", Pqty);
        productMap.put("code", Pcode);
        RootRef.child("nombre_products").setValue(Pcode);
if(switchi.isChecked())
{
    System.out.println("hellosam");
    productMap.put("option",Poption);
    productMap.put("type",Ptype);
   if(iscolor|| Ptype.equals("color"))
       productMap.put("value",colorslist);
   else
       productMap.put("value",Pvalues);
}
else  {productMap.put("type","nothing");
    productMap.put("type2","nothing");
}
        if(card2.getVisibility()==View.VISIBLE)
        {    System.out.println("hellosaman");
            productMap.put("option2",Poption2);
            productMap.put("type2",Ptype2);
            if(iscolor2 || Ptype2.equals("color"))
                productMap.put("value2",colorslist2);
            else
                productMap.put("value2",Pvalues2);
        }else  productMap.put("type2","nothing");
        ProductsRef.child(Pcode).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddProductActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AddProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}