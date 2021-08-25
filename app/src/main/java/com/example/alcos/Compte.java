package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Compte extends AppCompatActivity {
    private BottomNavigationView BottomNavigationView;
    private FirebaseAuth Auth;
    String  currentUserID;
    TextView nom,prenom,email,mescommandes;
    EditText phone,adresse;
    CircleImageView pic;
    private DatabaseReference UsersRef;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);
        BottomNavigationView=findViewById(R.id.bottombar);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("users Images");

        nom=findViewById(R.id.nom);
        prenom=findViewById(R.id.prenom);
        mescommandes=findViewById(R.id.mescommandes);
        adresse=findViewById(R.id.adresse1);
        email=findViewById(R.id.emailup);
        phone=findViewById(R.id.tel);
        pic=findViewById(R.id.profile);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
       // currentUserID = Auth.getCurrentUser().getUid();
        Auth= FirebaseAuth.getInstance();
       currentUserID= Auth.getCurrentUser().getUid();
        userInfoDisplay(pic,  phone, adresse);
        UsersRef.child(currentUserID).
                addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("nom"))&& (dataSnapshot.hasChild("prenom") ))
                        {
                            nom.setText(dataSnapshot.child("nom").getValue().toString());
                            prenom.setText(dataSnapshot.child("prenom").getValue().toString());
                            adresse.setText(dataSnapshot.child("adresse").getValue().toString());
                            email.setText(dataSnapshot.child("email").getValue().toString());
                            phone.setText(dataSnapshot.child("phone").getValue().toString());


                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }





                });

        mescommandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Compte.this, CommandesActivity.class);
                startActivity(intent);

            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Compte.this);
            }
        });


        BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                                                     @Override
                                                                     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                                         switch(item.getItemId())
                                                                         {


                                                                             case R.id.logout:
                                                                               Auth.signOut();

                                                                                 Intent loginIntent = new Intent(Compte.this, MainActivity.class);
                                                                                 startActivity(loginIntent);
                                                                                 finish();
                                                                                 return true;
                                                                             case R.id.update:
                                                                                 if (checker.equals("clicked"))
                                                                                 {
                                                                                     userInfoSaved();
                                                                                 }
                                                                                 else
                                                                                 {
                                                                                     updateOnlyUserInfo();
                                                                                 }
                                                                                 return true;
                                                                             default:

                                                                                 return false;
                                                                         }

                                                                     }


                                                                 }
        );
    }


    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();

        userMap. put("adresse", adresse.getText().toString());
        userMap. put("phone", phone.getText().toString());
        userMap. put("email", email.getText().toString());
        ref.child(currentUserID).updateChildren(userMap);

        Toast.makeText(Compte.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            pic.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Compte.this, Compte.class));
            finish();
        }
    }
    private void userInfoSaved()
    {

         if (TextUtils.isEmpty(adresse.getText().toString()))
        {
            Toast.makeText(this, "adress is mendatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString()))
        {
            Toast.makeText(this, "Phone is mandatory.", Toast.LENGTH_SHORT).show();
        }
         else if (TextUtils.isEmpty(email.getText().toString()))
         {
             Toast.makeText(this, "Email is mandatory.", Toast.LENGTH_SHORT).show();
         }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }



    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(currentUserID + ".jpg");

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

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("email", email.getText().toString());
                                userMap. put("adresse", adresse.getText().toString());
                                userMap. put("phone", phone.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(currentUserID).updateChildren(userMap);


                                progressDialog.dismiss();
                                Toast.makeText(Compte.this, "your profile informations were updated successfully", Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Compte.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText userPhoneEditText, final EditText addressEditText)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String telphone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("adresse").getValue().toString();

                        Picasso.get().load(image).into(pic);
                        phone.setText(telphone);
                        adresse.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
