package com.example.alcos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompteFragment extends Fragment {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    public CompteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    RootRef.child("Users").child(currentUserId).child("role").
                            addListenerForSingleValueEvent(new ValueEventListener(){

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    System.out.println("samia");
                                    if ((dataSnapshot.exists()) )
                                    {
                                        if(dataSnapshot.getValue().toString().equals("client"))
                                        { System.out.println("mama"+dataSnapshot.child("role").getValue().toString());
                                            Intent intent  = new Intent(getActivity(),Compte.class);
                                            System.out.println("here client");
                                            startActivity(intent);
                                        }
                                        else {
                                            System.out.println("role");
                                            Intent intent  = new Intent(getActivity(),AdminActivity.class);
                                            System.out.println("here admin");
                                            startActivity(intent);
                                        }
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }





                            });

                }


            }

        };
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener;
        final View[] view = {new View(getContext())};
        System.out.println("hello");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    System.out.println(user+"hello");
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                    view[0] =   inflater.inflate(R.layout.activity_login, container, false)        ;
                } else {
                    Intent loginIntent = new Intent(getActivity(), Compte.class);
                    startActivity(loginIntent);
                    view[0] =   inflater.inflate(R.layout.fragment_compte, container, false)        ;
                }


            }

        };
      return  inflater.inflate(R.layout.activity_login, container, false);
        //view[0];
    }*/
}