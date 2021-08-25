package com.example.alcos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


public class LoginTabFragment extends Fragment {
    private FirebaseAuth Auth;
    EditText email,password;
    TextView signin,forget;
    Button login_btn;
     boolean client ;
    private DatabaseReference UsersRef;
    FloatingActionButton fb,google,twitter;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab,container,false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forget = root.findViewById(R.id.forget);
        login_btn = root.findViewById(R.id.login_btn);
        signin=root.findViewById(R.id.signin);
        Auth=FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        fb = root.findViewById(R.id.fab_fb);
        google = root.findViewById(R.id.fab_google);
        twitter = root.findViewById(R.id.fab_twitter);

       /* email.setTranslationY(10);
        password.setTranslationY(10);

       signin.setTranslationY(20);
        login_btn.setTranslationY(80);*/


        email.setAlpha(0);
        password.setAlpha(0);

        login_btn.setAlpha(0);
        signin.setAlpha(0);
forget.setAlpha(0);
        email.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(600).start();
        signin.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(600).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(1200).start();
forget.animate().translationX(0).alpha(1).setDuration(2000).setStartDelay(600).start();

        fb.setTranslationY(200);
        google.setTranslationY(200);
        twitter.setTranslationY(200);


        fb.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);


        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getActivity(), "un code vous sera envoyé par mail", Toast.LENGTH_SHORT).show();
                int code=(int)Math.random() * ( 100000 );
                if( email.getText().toString().isEmpty())
                {   Toast.makeText(getActivity(), "veuillez entrer votre email", Toast.LENGTH_SHORT).show();
                }
            else{
                    /*Intent intent = new Intent(getActivity(),NewPasswordActivity.class);
                    intent.putExtra("code",code);
                    intent.putExtra("email",email.getText().toString());
                    startActivity(intent);*/
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = email.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        auth.setLanguageCode("fr");
                                        Toast.makeText(getActivity(), "un email vous a été envoyé", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if(mail.equals("")||pass.equals(""))

                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    loginF(mail,pass);
                    Intent intent  = new Intent(getActivity(), MainActivity.class);
                    System.out.println("heri admin");
                    startActivity(intent);



                     /*   if(loginF(mail,pass))
                        { Toast.makeText(getActivity(), "Sign in successfull", Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(getActivity(), MainActivity.class);
                            System.out.println("heri admin");
                            startActivity(intent);
                        }
                        else{
                            Intent intent  = new Intent(getActivity(),AdminActivity.class);
                            System.out.println("here admin");
                            startActivity(intent);
                        }*/

                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), SignupTabFragment.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private boolean estclient(String mail, String pass) {
        final boolean[] client = new boolean[1];
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String currentUserId = Auth.getCurrentUser().getUid();
                           if(snapshot.child(currentUserId).child("email").getValue().toString()==mail)
                           {
                               if(snapshot.child("role").getValue().toString()=="client")
                                   client[0] = true;
                           }
                           else client[0] = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
        return client[0];
    }


    private boolean loginF(String mail, String pass) {
client=true;
        Auth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        String currentUserId = Auth.getCurrentUser().getUid();
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        UsersRef.child(currentUserId).child("device_token")
                                .setValue(deviceToken)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {

                                    }
                                });


                        UsersRef.child(currentUserId).
                                addListenerForSingleValueEvent(new ValueEventListener(){

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        System.out.println("samia");
                                        System.out.println(dataSnapshot.child("role").toString());
                                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("role") ))
                                        {
                                            if(dataSnapshot.child("role").getValue().toString().equals("client"))
                                            { System.out.println("mama"+dataSnapshot.child("role").getValue().toString());
                                                client= true;
                                            }
                                            else {
                                                System.out.println("role");
                                                client = false;
                                            }
                                        }

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }





                                });
                    }
                        else {


                            Toast.makeText(getContext(), "votre email ou mot de passe est erroné", Toast.LENGTH_SHORT).show();

                        }
                    }




                });
        System.out.println("client"+client);
      return client;
    }


}
