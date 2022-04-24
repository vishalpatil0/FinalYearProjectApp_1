package com.example.attesta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyUser,invalidEmail,invalidFullName;
    private EditText full_name,email,password,confirm_password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private Button register;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String fullNamePattern = "^[a-zA-Z]*$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyUser=findViewById(R.id.alreadyUser);
        full_name=findViewById(R.id.fullname);
        email=findViewById(R.id.registerEmail);
        password=findViewById(R.id.registerPassword);
        confirm_password=findViewById(R.id.registerConfirmPassword);
        progressBar=findViewById(R.id.registerProgressBar);
        invalidEmail=findViewById(R.id.invalidRegisterEmail);
        register=findViewById(R.id.register);
        invalidFullName=findViewById(R.id.invalidRegisterFullName);

        firebaseAuth=FirebaseAuth.getInstance();     //getting current instance of database from firebase to perform various action on database.

        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                else
                {
                    email.setHint("Email field is empty");
                    email.setHintTextColor(Color.RED);
                }
                String fullNameValidate=full_name.getText().toString().trim();
                if(fullNameValidate.length()>0)
                {

                }
                else
                {
                    full_name.setHint("FullName field is empty");
                    full_name.setHintTextColor(Color.RED);
                }
            }
        });

        full_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String fullNameValidate=full_name.getText().toString().trim();
                if(fullNameValidate.length()>0)
                {
                    if(!fullNameValidate.matches(fullNamePattern))
                    {
                        full_name.setBackgroundResource(R.drawable.shaperedemail);
                        full_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_red,0,0,0);
                        full_name.setTextColor(Color.RED);
                        invalidFullName.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                full_name.setHint("FullName");
                full_name.setHintTextColor(getColor(R.color.white));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fullNameValidate=full_name.getText().toString().trim();
                if(fullNameValidate.matches(fullNamePattern))
                {
                    invalidFullName.setVisibility(View.INVISIBLE);
                    full_name.setTextColor(getColor(R.color.white));
                    full_name.setBackgroundResource(R.drawable.shapeemail);
                    full_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp,0,0,0);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String firstPassword=password.getText().toString().trim();
                if(firstPassword.length()>0)
                {

                    confirm_password.setEnabled(true);
                }
                else
                {
                    confirm_password.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String firstPassword=password.getText().toString().trim();
                String confirmPassword=confirm_password.getText().toString().trim();
                if(firstPassword.equals(confirmPassword))
                {
                    confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_green,0,0,0);
                }
                else
                {
                    confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}