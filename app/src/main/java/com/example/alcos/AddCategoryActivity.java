package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class AddCategoryActivity extends AppCompatActivity {

    private String Description, Cname,Cid;
    private Button AddNewCategButton;
    private ImageView InputCategImage;
    private EditText InputCategName, InputCategDescription;
    private static final int GalleryPick = 1;
    ArrayList<Categorie> listC;
    ArrayList<String> list;
    TextView categ;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference CategoryImagesRef;
    private DatabaseReference ProductsRef,RootRef,RootRef2;
    private ProgressDialog loadingBar;
    int nbr=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        CategoryImagesRef = FirebaseStorage.getInstance().getReference().child("category Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        RootRef2 = FirebaseDatabase.getInstance().getReference().child("Categories");
        RootRef = FirebaseDatabase.getInstance().getReference();
        AddNewCategButton = (Button) findViewById(R.id.add_new_category);
        InputCategImage = (ImageView) findViewById(R.id.categ_image);
        InputCategName = (EditText) findViewById(R.id.categ_name);
        InputCategDescription = (EditText) findViewById(R.id.categ_description);
        loadingBar = new ProgressDialog(this);


        InputCategImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewCategButton.setOnClickListener(new View.OnClickListener() {
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
            InputCategImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
        Description = InputCategDescription.getText().toString();
        Cname = InputCategName.getText().toString();
        RootRef.child(("nombre_categ")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int nombre=Integer.parseInt(snapshot.getValue().toString());
                    Cid=String.valueOf(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (ImageUri == null)
        {
            Toast.makeText(this, "Category image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write Category description...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Cname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }

        else {
            nbr=0;
            RootRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.child("nom_categorie").getValue().toString().equals(Cname))
                                existe();}

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
        loadingBar.setTitle("Add New Categorie");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new category.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference filePath = CategoryImagesRef.child(ImageUri.getLastPathSegment() + Cid + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddCategoryActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddCategoryActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AddCategoryActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("id_category", Cid);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("nom_categorie", Cname);

String id=String.valueOf(Integer.parseInt(Cid)+1);
        RootRef.child("nombre_categ").setValue(id);

        RootRef2.child(id).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddCategoryActivity.this, AdminActivity.class);
                            startActivity(intent);
finish();
                            loadingBar.dismiss();
                            Toast.makeText(AddCategoryActivity.this, "Category is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddCategoryActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}