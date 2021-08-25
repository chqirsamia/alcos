package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupTabFragment extends Fragment {
    private CircleImageView profilepic;
    private static final int pickpic=1;
private DatePickerDialog.OnDateSetListener DateListener;
    private String currentUserID,id, downloadImageUrl;
    Uri imageUri;
    private ProgressDialog loadingBar;

    private StorageReference UsersImagesRef;
    private static final int GalleryPick = 1;
    EditText name,lastname,email,password,telephone,confirmpassword,cina,sex,adresse;
    TextView datenaiss;
    private FirebaseAuth Auth;
    Button signup;
    RadioButton homme,femme;
    RadioGroup group;
    ImageView calendar;
    Button next;
    FirebaseDatabase database;
    private DatabaseReference RootRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savecInstanceState){

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab,container,false);
        name = root.findViewById(R.id.name);
        lastname =root.findViewById(R.id.lastname);
        email = root.findViewById(R.id.email);
        datenaiss =root.findViewById(R.id.datenaiss);
        password = root.findViewById(R.id.password);
        confirmpassword = root.findViewById(R.id.confirmpassword);
        next = root.findViewById(R.id.suivantbutton);
        profilepic = root.findViewById(R.id.profile);
        telephone = root.findViewById(R.id.telephone);
        adresse=root.findViewById(R.id.adresse);
        loadingBar = new ProgressDialog(getContext());
        UsersImagesRef = FirebaseStorage.getInstance().getReference().child("users Images");
        group=root.findViewById(R.id.gender);
        homme = root.findViewById(R.id.homme);
        femme = root.findViewById(R.id.femme);


        calendar=root.findViewById(R.id.calendar);


        database = FirebaseDatabase.getInstance();
        RootRef = database.getReference();
        Auth= FirebaseAuth.getInstance();
        name.setTranslationY(10);
        lastname.setTranslationY(10);
        email.setTranslationY(10);
        datenaiss.setTranslationY(10);
        password.setTranslationY(10);
        confirmpassword.setTranslationY(10);
        next.setTranslationY(10);
        profilepic.setTranslationY(10);
        adresse.setTranslationY(10);
        telephone.setTranslationY(10);
        group.setTranslationY(10);
        homme.setTranslationY(10);
        femme.setTranslationY(10);
        calendar.setTranslationY(10);

        name.setAlpha(0);
        lastname.setAlpha(0);
        email.setAlpha(0);
        datenaiss.setAlpha(0);
        profilepic.setAlpha(0);
        password.setAlpha(0);
        confirmpassword.setAlpha(0);
        next.setAlpha(0);
        homme.setAlpha(0);
        femme.setAlpha(0);
        group.setAlpha(0);
        telephone.setAlpha(0);

        adresse.setAlpha(0);



        name.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        lastname.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        email.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(700).start();
        datenaiss.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        profilepic.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(900).start();
        password.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(1000).start();
        confirmpassword.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        homme.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        femme.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        group.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(1200).start();
        telephone.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        calendar.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        next.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();
        adresse.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(500).start();



        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);

                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth
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

                if (  validationTel(phone)==false || !validationEmail(user) || pass.equals("") || repass.equals("") || nom.equals("") || datenaissance.equals("") || !validationMotDePasse(pass, repass))
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(repass)) {
                        if(pass.length()<6)
                        {
                            Toast.makeText(getActivity(), "votre mot de passe est court veuillez entrer un mot de passe de taille superieure a 6", Toast.LENGTH_SHORT).show();

                        }
                        else{



                              enregistrerUser(user, pass, prenom, nom, "client", datenaissance, sexe, phone,adress);
                            FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();


                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                if (imageUri != null)
                                    StoreProductInformation();

                        }} else {
                        Toast.makeText(getActivity(), "Passwords not matching", Toast.LENGTH_SHORT).show();
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
        return root;
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
    private void StoreProductInformation()
    {

        loadingBar.setTitle("Add New User");
        loadingBar.setMessage("Dear User, please wait while we are adding you.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference filePath = UsersImagesRef.child(imageUri.getLastPathSegment() + currentUserID + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getActivity(), "your Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(getActivity(), "got the image Url Successfully...", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });
    }


    private boolean validationEmail(String email) {
       // Boolean checkuser = checkemail(email);
        boolean validation=true;
        if ( email != null) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) )  {
                Toast.makeText(getActivity(), "Merci de saisir une adresse mail valide.", Toast.LENGTH_SHORT).show();;
                validation=false;
            }
        } else {
            Toast.makeText(getActivity(), "Merci de saisir une adresse mail.", Toast.LENGTH_SHORT).show();
            validation=false;
        }
        return validation;
    }
    private boolean validationMotDePasse(String motDePasse,String confirmation){
        boolean validation=true;
        if (motDePasse != null && confirmation != null) {
            if (!motDePasse.equals(confirmation)) {
                Toast.makeText(getActivity(), "Les mots de passe entrés sont différents, merci de les saisir à nouveau.", Toast.LENGTH_SHORT).show();
                validation=false;
            } else if (motDePasse.trim().length() < 3) {
                Toast.makeText(getActivity(), "Les mots de passe doivent contenir au moins 3 caractères.", Toast.LENGTH_SHORT).show();;
                validation=false;
            }
        } else {
            Toast.makeText(getActivity(), "Merci de saisir et confirmer votre mot de passe.", Toast.LENGTH_SHORT).show();;
            validation=false;

        }
        return validation;
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
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode== Activity.RESULT_OK  &&  data!=null)
        {
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
        }
    }
    private boolean validationTel(String tel) {
        boolean validation=true;
        if (tel != null) {
            if (!tel.matches("[0]{1}[0-9]{9}[ ]*")) {
                Toast.makeText(getActivity(), "Merci de saisir un numéro de téléphone valide.", Toast.LENGTH_SHORT).show();
                validation=false;
            }
        } else {
            Toast.makeText(getActivity(), "Merci de saisir un numéro de téléphone.", Toast.LENGTH_SHORT).show();
            validation=false;
        }
        return validation;
    }
    private boolean enregistrerUser(String user, String pass, String prenom, String nom, String client, String datenaissance, String sexe, String phone, String adresse) {
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
FirebaseUser fuser=Auth.getCurrentUser();
                            RootRef.child("Users").child(currentUserID).setValue(" ");
                            RootRef.child("Users").child(currentUserID).child("email").setValue(user);
                            RootRef.child("Users").child(currentUserID).child("password").setValue(pass);
                            RootRef.child("Users").child(currentUserID).child("prenom").setValue(prenom);
                            RootRef.child("Users").child(currentUserID).child("nom").setValue(nom);
                            RootRef.child("Users").child(currentUserID).child("role").setValue("client");
                            RootRef.child("Users").child(currentUserID).child("datenaissance").setValue(datenaissance);
                            RootRef.child("Users").child(currentUserID).child("sexe").setValue(sexe);
                            RootRef.child("Users").child(currentUserID).child("phone").setValue(phone);
                            RootRef.child("Users").child(currentUserID).child("adresse").setValue(adresse);
                            RootRef.child("Users").child(currentUserID).child("image").setValue(downloadImageUrl);

                          //  Toast.makeText(getActivity(), "Registered successfully", Toast.LENGTH_SHORT).show();

fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        Toast.makeText(getActivity(),  "the verification email was sent successfully", Toast.LENGTH_SHORT).show();
       }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getActivity(), "Error : " + "please write a valid email", Toast.LENGTH_SHORT).show();
    }
});


                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error : " + message, Toast.LENGTH_SHORT).show();
                            succes[0] =false;
                        }
                    }
                });
   return succes[0] ;
    }

}