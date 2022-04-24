package com.example.attesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyUser,invalidEmail,invalidFullName,differentPasswords,minimumPassword;
    private EditText full_name,email,password,confirm_password;
    private ImageView passwordEye,confirmPasswordEye;
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
        passwordEye=findViewById(R.id.passwordEye);
        confirmPasswordEye=findViewById(R.id.confirmPasswordEye);
        differentPasswords=findViewById(R.id.differentPassword);
        minimumPassword=findViewById(R.id.minimumPassword);

        firebaseAuth=FirebaseAuth.getInstance();     //getting current instance of database from firebase to perform various action on database.

        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                Boolean registerFlag=true;
                String emailValidate=email.getText().toString().trim();
                String fullNameValidate=full_name.getText().toString().trim();
                String passwordValidate=password.getText().toString().trim();
                String confirmPasswordValidate=confirm_password.getText().toString().trim();
                if(emailValidate.length()>0 && fullNameValidate.length()>0 && passwordValidate.length()>0)
                {
                    if(!emailValidate.matches(emailPattern))
                    {
                        registerFlag=false;
                        invalidEmail.setVisibility(View.VISIBLE);
                        email.setBackgroundResource(R.drawable.shaperedemail);
                        email.setTextColor(Color.RED);
                        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_email,0,0,0);
                    }
                    if(!fullNameValidate.matches(fullNamePattern))
                    {
                        registerFlag=false;
                        invalidFullName.setVisibility(View.VISIBLE);
                        full_name.setBackgroundResource(R.drawable.shaperedemail);
                        full_name.setTextColor(Color.RED);
                        full_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_red,0,0,0);
                    }
                    if(passwordValidate.length()<6)
                    {
                        registerFlag=false;
                        minimumPassword.setVisibility(View.VISIBLE);
                    }
                    if(registerFlag)
                    {
                        String firstPassword=password.getText().toString().trim();
                        String confirmPassword=confirm_password.getText().toString().trim();
                        if(firstPassword.equals(confirmPassword))
                        {
                            progressBar.setVisibility(View.VISIBLE);

                            //now register the use with firebase database
                            firebaseAuth.createUserWithEmailAndPassword(emailValidate,confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        finish();
                                    }else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Error ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            differentPasswords.setVisibility(View.VISIBLE);
                            confirm_password.setTextColor(Color.RED);
                        }
                    }
                }
                else
                {
                    if(emailValidate.length()==0) {
                        email.setHint("Email field is empty");
                        email.setHintTextColor(Color.RED);
                    }
                    if(fullNameValidate.length()==0) {
                        full_name.setHint("FullName field is empty");
                        full_name.setHintTextColor(Color.RED);
                    }
                    if(passwordValidate.length()==0){
                        password.setHint("Password field is empty");
                        password.setHintTextColor(Color.RED);
                    }
                }
            }
        });

        full_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String fullNameValidate=full_name.getText().toString().trim().replaceAll("\\s","");
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
                password.setHintTextColor(getColor(R.color.white));
                password.setHint("Password");
                minimumPassword.setVisibility(View.INVISIBLE);
                if(firstPassword.length()>0)
                {
                    passwordEye.setVisibility(View.VISIBLE);
                    passwordEye.setClickable(true);
                    confirm_password.setEnabled(true);
                }
                else
                {
                    differentPasswords.setVisibility(View.INVISIBLE);
                    passwordEye.setVisibility(View.INVISIBLE);
                    passwordEye.setClickable(false);
                    confirm_password.setEnabled(false);
                    confirmPasswordEye.setImageResource(R.drawable.password_visible);
                    confirmPasswordEye.setClickable(false);
                    confirmPasswordEye.setVisibility(View.INVISIBLE);
                    confirm_password.setText("");
                    confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);
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
                differentPasswords.setVisibility(View.INVISIBLE);
                confirm_password.setTextColor(getColor(R.color.white));
                String firstPassword=password.getText().toString().trim();
                String confirmPassword=confirm_password.getText().toString().trim();
                if(confirmPassword.length()>0)
                {
                    confirmPasswordEye.setClickable(true);
                    confirmPasswordEye.setVisibility(View.VISIBLE);
                }
                else
                {
                    confirmPasswordEye.setVisibility(View.INVISIBLE);
                    confirmPasswordEye.setClickable(false);
                }
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

        passwordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    passwordEye.setImageResource(R.drawable.password_invisible);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordEye.setImageResource(R.drawable.password_visible);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        confirmPasswordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirm_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    confirmPasswordEye.setImageResource(R.drawable.password_invisible);
                    confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    confirmPasswordEye.setImageResource(R.drawable.password_visible);
                    confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}