package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alcos.Model.Produit;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductUpdateActivity extends AppCompatActivity {

    private ImageView imageView,delete;
    String  currentUserID;
    EditText qty;
    boolean isprix=true;
   // private ScrollableNumberPicker qty;
    private TextView nom,description,prix;
    private String pid="";
    private FirebaseAuth Auth;
    private String downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,CartRef;
    private Button save;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        pid = getIntent().getStringExtra("pid");
        imageView = findViewById(R.id.image);
        nom = findViewById(R.id.nom);
        description = findViewById(R.id.description);
        prix = findViewById(R.id.prix);
        save = findViewById(R.id.update);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        getDetails(pid);
        delete=findViewById(R.id.delete);
        qty = findViewById(R.id.qty);
        Auth = FirebaseAuth.getInstance();
        currentUserID = Auth.getCurrentUser().getUid();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(ProductUpdateActivity.this);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popup =new AlertDialog.Builder(ProductUpdateActivity.this);
                popup.setTitle("Delete Product");
                popup.setMessage("etes vous sur de vouloir supprimer le produit...Cette action est irreversible");
                popup.setPositiveButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                popup.setNegativeButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ProductsRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();
                                startActivity(new Intent(ProductUpdateActivity.this, AdminActivity.class));
finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                               // Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }

                });
                popup.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked"))
                {
                    productInfoSaved();
                }
                else
                {
                    updateOnlyProductInfo();
                }
            }
        });


    }


    private void getDetails(String pid) {
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        ProductsRef.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Produit prod=snapshot.getValue(Produit.class);
                    nom.setText(prod.getNom());
                    description.setText(prod.getDescription());
                    prix.setText(prod.getPrix());
                    qty.setText(snapshot.child("quantite").getValue().toString());
                    Picasso.get().load(prod.getImage()).into(imageView);
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateOnlyProductInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

        HashMap<String, Object> userMap = new HashMap<>();

        userMap. put("prix", prix.getText().toString());
        userMap. put("nom", nom.getText().toString());
        userMap. put("description", description.getText().toString());
        userMap. put("quantite", qty.getText().toString());
        ref.child(pid).updateChildren(userMap);

        Toast.makeText(ProductUpdateActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

           imageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ProductUpdateActivity.this, ProductUpdateActivity.class));
        }
    }
    private void productInfoSaved()
    {
        try {
            Float price = Float.parseFloat(prix.getText().toString());
        } catch (NumberFormatException e) {
            isprix = false;
        }
        if (TextUtils.isEmpty(nom.getText().toString()))
        {
            Toast.makeText(this, "name is mendatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description.getText().toString()))
        {
            Toast.makeText(this, "description is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prix.getText().toString()))
        {
            Toast.makeText(this, "price is mandatory.", Toast.LENGTH_SHORT).show();
        }

        else if (!isprix)
        {
            Toast.makeText(this, "Please write valid price", Toast.LENGTH_SHORT).show();
            try {
                Float price = Float.parseFloat(prix.getText().toString());
            } catch (NumberFormatException e) {
                isprix = false;
            }
        }
        else if (TextUtils.isEmpty(qty.getText().toString()))
        {
            Toast.makeText(this, "quantity is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }



    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Product");
        progressDialog.setMessage("Please wait, while we are updating the product information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(pid + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("prix", prix.getText().toString());
                                userMap. put("nom", nom.getText().toString());
                                userMap. put("description", description.getText().toString());
                                userMap. put("quantite", qty.getText().toString());
                                userMap. put("image",myUrl);
                                ref.child(pid).updateChildren(userMap);


                                progressDialog.dismiss();
                                Toast.makeText(ProductUpdateActivity.this, "the product informations were updated successfully", Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(ProductUpdateActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }






}