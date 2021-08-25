package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAdminActivity extends AppCompatActivity {
    private CircleImageView profilepic;
    private DatePickerDialog.OnDateSetListener DateListener;
    private String currentUserID,id, downloadImageUrl;
    Uri imageUri;
    private StorageReference UsersImagesRef;
    private static final int GalleryPick = 1;
    EditText name,lastname,email,password,telephone,confirmpassword,cina,sex,adresse;
    TextView espace,datenaiss;
    private FirebaseAuth Auth;
    Button signup;
    RadioButton homme,femme;
    RadioGroup group;
    ImageView calendar;
    Button next;
    FirebaseDatabase database;
    private DatabaseReference RootRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        name = findViewById(R.id.name);
        lastname =findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        datenaiss =findViewById(R.id.datenaiss);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        next = findViewById(R.id.add_new_admin);
        profilepic = findViewById(R.id.profile);
        telephone = findViewById(R.id.telephone);
        adresse=findViewById(R.id.adresse);
       UsersImagesRef = FirebaseStorage.getInstance().getReference().child("users Images");
        group=findViewById(R.id.gender);
        homme = findViewById(R.id.homme);
        femme = findViewById(R.id.femme);

        calendar=findViewById(R.id.calendar);

        loadingBar = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        RootRef = database.getReference();
        Auth= FirebaseAuth.getInstance();
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);

                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(AddAdminActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth
                        ,DateListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                DateListener=new DatePickerDialog.OnDateSetListener(){


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date=dayOfMonth+"/"+month+"/"+year;
                        datenaiss.setText(date);
                    }
                };
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = name.getText().toString();
                String nom = lastname.getText().toString();
                String user = email.getText().toString();
                String datenaissance = datenaiss.getText().toString();
                String pass = password.getText().toString();
                String repass = confirmpassword.getText().toString();
                String phone = telephone.getText().toString();
                String adress = adresse.getText().toString();
                String sexe;

                traiterTel(phone);
                if (group.getCheckedRadioButtonId() == R.id.homme )
                {  sexe = "homme";

                }
                else
                { sexe = "femme";


                }

                if (   !validationTel(phone) || !validationEmail(user) || pass.equals("") || repass.equals("") || nom.equals("") || datenaissance.equals("") || !validationMotDePasse(pass, repass))
                {
                    Toast.makeText(AddAdminActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    System.out.println("fields" + pass + "fields" + repass + "fields" + datenaissance + "fields" + phone + "fields" + adress + "fields" + sexe);
                }
                else {
                    if (pass.equals(repass)) {
                        if(pass.length()<6)
                        {
                            Toast.makeText(AddAdminActivity.this, "votre mot de passe est court veuillez entrer un mot de passe de taille superieure a 6", Toast.LENGTH_SHORT).show();

                        }
                        else{



                            enregistrerUser(user, pass, prenom, nom, "admin", datenaissance, sexe, phone,adress,downloadImageUrl);
                            loadingBar.dismiss();
                            Intent intent = new Intent(AddAdminActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                            if(imageUri!=null)
                            StoreProductInformation();

                        }} else {
                        Toast.makeText(AddAdminActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();

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
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
        }
    }
    private Boolean checkemail(String user) {
        final Boolean[] exist = new Boolean[1];
        Auth.fetchSignInMethodsForEmail(user)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        exist[0] = isNewUser;

                    }
                });
        return exist[0];
    }

    private boolean validationEmail(String email) {
        // Boolean checkuser = checkemail(email);
        boolean validation=true;
        if ( email != null) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) )  {
                Toast.makeText(AddAdminActivity.this, "Merci de saisir une adresse mail valide.", Toast.LENGTH_SHORT).show();;
                validation=false;
            }
        } else {
            Toast.makeText(AddAdminActivity.this, "Merci de saisir une adresse mail.", Toast.LENGTH_SHORT).show();
            validation=false;
        }
        return validation;
    }
    private boolean validationMotDePasse(String motDePasse,String confirmation){
        boolean validation=true;
        if (motDePasse != null && confirmation != null) {
            if (!motDePasse.equals(confirmation)) {
                Toast.makeText(AddAdminActivity.this, "Les mots de passe entrés sont différents, merci de les saisir à nouveau.", Toast.LENGTH_SHORT).show();
                validation=false;
            } else if (motDePasse.trim().length() < 3) {
                Toast.makeText(AddAdminActivity.this, "Les mots de passe doivent contenir au moins 3 caractères.", Toast.LENGTH_SHORT).show();;
                validation=false;
            }
        } else {
            Toast.makeText(AddAdminActivity.this, "Merci de saisir et confirmer votre mot de passe.", Toast.LENGTH_SHORT).show();;
            validation=false;

        }
        return validation;
    }

    private void StoreProductInformation()
    {

        loadingBar.setTitle("Add New Admin");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new admin.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference filePath = UsersImagesRef.child(imageUri.getLastPathSegment() + currentUserID + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddAdminActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddAdminActivity.this, "admin Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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
                            loadingBar.dismiss();
                            Toast.makeText(AddAdminActivity.this, "got the admin image Url Successfully...", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });
    }


    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose  picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1); }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss(); } }});
        builder.show();
    }

    private void traiterTel(String tel) {

        validationTel(tel);

    }

    private boolean validationTel(String tel) {
        boolean validation=true;
        if (tel != null) {
            if (!tel.matches("[0]{1}[0-9]{9}[ ]*")) {
                Toast.makeText(AddAdminActivity.this, "Merci de saisir un numéro de téléphone valide.", Toast.LENGTH_SHORT).show();
                validation=false;
            }
        } else {
            Toast.makeText(AddAdminActivity.this, "Merci de saisir un numéro de téléphone.", Toast.LENGTH_SHORT).show();
            validation=false;
        }
        return validation;
    }
    private boolean enregistrerUser(String user, String pass, String prenom, String nom, String client, String datenaissance, String sexe, String phone, String adresse, String downloadImageUrl) {
        final boolean[] succes = new boolean[1];
        Auth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            currentUserID = Auth.getCurrentUser().getUid();
                            // id = currentUserID;
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();


                            RootRef.child("Users").child(currentUserID).setValue(" ");
                            RootRef.child("Users").child(currentUserID).child("email").setValue(user);
                            RootRef.child("Users").child(currentUserID).child("password").setValue(pass);
                            RootRef.child("Users").child(currentUserID).child("prenom").setValue(prenom);
                            RootRef.child("Users").child(currentUserID).child("nom").setValue(nom);
                            RootRef.child("Users").child(currentUserID).child("role").setValue( client);
                            RootRef.child("Users").child(currentUserID).child("datenaissance").setValue(datenaissance);
                            RootRef.child("Users").child(currentUserID).child("sexe").setValue(sexe);
                            RootRef.child("Users").child(currentUserID).child("phone").setValue(phone);
                            RootRef.child("Users").child(currentUserID).child("adresse").setValue(adresse);
                            RootRef.child("Users").child(currentUserID).child("image").setValue( downloadImageUrl);
                            RootRef.child("Users").child(currentUserID).child("device_token")
                                    .setValue(deviceToken);
                            Toast.makeText(AddAdminActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            succes[0] =true;
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(AddAdminActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            succes[0] =false;
                        }
                    }
                });
        return succes[0] ;
    }

}

