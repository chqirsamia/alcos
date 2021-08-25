package com.example.alcos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
 private BottomNavigationView BottomNavigationView;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
private CompteFragment compteFragment;
private MessageFragment messageFragment;
private PanierFragment panierFragment;
private AlcosFragment alcosFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView=findViewById(R.id.bottombar);
compteFragment=new CompteFragment();
messageFragment =new MessageFragment();
panierFragment=new PanierFragment();
alcosFragment= new AlcosFragment();
setFragment(alcosFragment);





        BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                   /* case R.id.alcos:
                       // setFragment(alcosFragment);
                        Intent intent  = new Intent(MainActivity.this,HomeActivity.class);
                        System.out.println("here admin");
                        startActivity(intent);
                        return true;*/
                    case R.id.message:
                        RootRef = FirebaseDatabase.getInstance().getReference();
                        mAuth = FirebaseAuth.getInstance();
                       
                        FirebaseAuth.AuthStateListener mAuthListener1;
                        System.out.println("hello");
                        mAuthListener1 = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null ) {
                                    System.out.println(user+"null hello");
                                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                }
                            else {
                                    String currentUserId = mAuth.getCurrentUser().getUid();

                                    RootRef.child("Users").child(currentUserId).child("role").
                                            addListenerForSingleValueEvent(new ValueEventListener(){

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    System.out.println("samia");
                                                    if ((dataSnapshot.exists()) )
                                                    {
                                                        if(dataSnapshot.getValue().toString().equals("client"))
                                                        {
                                                            Intent intent  = new Intent(MainActivity.this,MesaagerieClient.class);
                                                            System.out.println("here client");
                                                            startActivity(intent);
                                                        }
                                                        else {
                                                           /* System.out.println("role");
                                                            Intent intent  = new Intent(MainActivity.this,MessagerieAdmin.class);
                                                            System.out.println("here admin");
                                                            startActivity(intent);*/
                                                            setFragment(messageFragment);
                                                        }
                                                    }



                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }





                                            });
  /* if(!user.isEmailVerified()) {
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this,  "the verification email was sent successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Error : " + "please write a valid email", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(MainActivity.this, "please verify your email", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                            }

                        }

                        ;

                        mAuth.addAuthStateListener(mAuthListener1);

                        return true;

                    case R.id.panier:
                        Intent intent2  = new Intent(MainActivity.this,PanierActivity.class);
                        System.out.println("here admin");
                        startActivity(intent2);

                        return true;
                    case R.id.profile:
                        RootRef = FirebaseDatabase.getInstance().getReference();
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseAuth.AuthStateListener mAuthListener;
                        System.out.println("hello");
                        mAuthListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null) {
                                    System.out.println(user+"hello");
                                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                }
                               /* else if(!user.isEmailVerified())
                                    Toast.makeText(MainActivity.this,"please verify your email", Toast.LENGTH_SHORT).show();
                               */ else {
                                    String currentUserId = mAuth.getCurrentUser().getUid();
                                    RootRef.child("Users").child(currentUserId).child("role").
                                            addListenerForSingleValueEvent(new ValueEventListener(){

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    System.out.println("samia");
                                                    if ((dataSnapshot.exists()) )
                                                    {
                                                        if(dataSnapshot.getValue().toString().equals("client"))
                                                        {
                                                            Intent intent  = new Intent(MainActivity.this,Compte.class);
                                                            System.out.println("here client");
                                                            startActivity(intent);

                                                        }
                                                        else {
                                                            System.out.println("role");
                                                            Intent intent  = new Intent(MainActivity.this,AdminActivity.class);
                                                            System.out.println("here admin");
                                                            startActivity(intent);
finish();
                                                        }
                                                    }



                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }





                                            });

                                }
                            }

                            }

                        ;

                        mAuth.addAuthStateListener(mAuthListener);
                       // setFragment(compteFragment);
                     return true;
                    default:
                       /* Intent intent  = new Intent(MainActivity.this,HomeActivity.class);
                        System.out.println("here admin");
                        startActivity(intent);*/
                        setFragment(alcosFragment);

                        return true;
                }

            }


            }
    );
}






    public void setFragment(Fragment fragment)
{
    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
fragmentTransaction.replace(R.id.frame,fragment);
fragmentTransaction.commit();
}

}