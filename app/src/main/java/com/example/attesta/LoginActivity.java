package com.example.attesta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private Button login,signup;
    private EditText email,password;
    private TextView invalidEmail;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        email=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        invalidEmail=findViewById(R.id.invalidEmail);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValidate=email.getText().toString().trim();
                if(emailValidate.length()>0)
                {
                    if(emailValidate.matches(emailPattern))
                    {
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        invalidEmail.setVisibility(View.VISIBLE);
                        email.setBackgroundResource(R.drawable.shaperedemail);
                        email.setTextColor(Color.RED);
                        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_email,0,0,0);
                    }
                }
                else
                {
                    email.setHint("Email field is empty");
                    email.setHintTextColor(Color.RED);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setHint("Email");
                email.setHintTextColor(getColor(R.color.white));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String emailValidate=email.getText().toString().trim();
                if(emailValidate.matches(emailPattern))
                {
                    invalidEmail.setVisibility(View.INVISIBLE);
                    email.setTextColor(getColor(R.color.white));
                    email.setBackgroundResource(R.drawable.shapeemail);
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email,0,0,0);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String emailValidate=email.getText().toString().trim();
                if(emailValidate.length()>0)
                {
                    if(!emailValidate.matches(emailPattern))
                    {
                        invalidEmail.setVisibility(View.VISIBLE);
                        email.setBackgroundResource(R.drawable.shaperedemail);
                        email.setTextColor(Color.RED);
                        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_email,0,0,0);
                    }
                }
            }
        });
    }
}