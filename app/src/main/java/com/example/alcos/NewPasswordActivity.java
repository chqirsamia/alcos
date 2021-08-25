package com.example.alcos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPasswordActivity extends AppCompatActivity {
EditText codeinput;
Button confirmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        int code=getIntent().getIntExtra("code",0);
        String email=getIntent().getStringExtra("email");
        confirmer=findViewById(R.id.confirmer);
        codeinput=findViewById(R.id.code);

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codeentre=Integer.parseInt(codeinput.getText().toString());
                if(codeentre==code)
                {
                    Intent intent = new Intent(NewPasswordActivity.this,EnterPasswordActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(NewPasswordActivity.this, "Le code que vous avez entré n'est pas valide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}