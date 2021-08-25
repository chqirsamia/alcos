package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnterPasswordActivity extends AppCompatActivity {
EditText password,confirmpass;
Button confirmer;
String email="";
Boolean existe=false;
String uid="";
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        confirmer=findViewById(R.id.confirmer);
        password=findViewById(R.id.password);
        confirmpass=findViewById(R.id.confirmpassword);
       email=getIntent().getStringExtra("email");
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        confirmpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String repass = confirmpass.getText().toString();
                if(validationMotDePasse(pass,repass)){
                    RootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.child("email").getValue().toString().equals(email))
                                {
                                    existe=true;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if(existe==false)
                        Toast.makeText(EnterPasswordActivity.this, "This user doesn't exist..", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private boolean validationMotDePasse(String motDePasse,String confirmation){
        boolean validation=true;
        if (motDePasse != null && confirmation != null) {
            if (!motDePasse.equals(confirmation)) {
                Toast.makeText(EnterPasswordActivity.this, "Les mots de passe entrés sont différents, merci de les saisir à nouveau.", Toast.LENGTH_SHORT).show();
                validation=false;
            } else if (motDePasse.trim().length() < 3) {
                Toast.makeText(EnterPasswordActivity.this, "Les mots de passe doivent contenir au moins 3 caractères.", Toast.LENGTH_SHORT).show();;
                validation=false;
            }
        } else {
            Toast.makeText(EnterPasswordActivity.this, "Merci de saisir et confirmer votre mot de passe.", Toast.LENGTH_SHORT).show();;
            validation=false;

        }
        return validation;
    }

}