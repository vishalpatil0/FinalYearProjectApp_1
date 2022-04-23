package com.example.attesta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyUser;
    private EditText full_name,email,password,confrim_password;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyUser=findViewById(R.id.alreadyUser);
        full_name=findViewById(R.id.fullname);
        email=findViewById(R.id.registerEmail);
        password=findViewById(R.id.registerPassword);
        confrim_password=findViewById(R.id.registerConfirmPassword);
        progressBar=findViewById(R.id.registerProgressBar);


        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}