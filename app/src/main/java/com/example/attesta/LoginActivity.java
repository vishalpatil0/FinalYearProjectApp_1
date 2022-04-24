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
import android.util.Log;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private Button login,signup;
    private ImageView showHidePassword;
    private EditText email,password;
    private TextView invalidEmail;
    private ProgressBar progressBar;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        email=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        invalidEmail=findViewById(R.id.invalidEmail);
        progressBar=findViewById(R.id.loginProgressBar);
        showHidePassword=findViewById(R.id.showHidePassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String emailValidate=email.getText().toString().trim();
                String passwordValidate=password.getText().toString().trim();
                if(emailValidate.length()>0 && passwordValidate.length()>0)
                {
                    if(emailValidate.matches(emailPattern))
                    {
                        firebaseAuth.signInWithEmailAndPassword(emailValidate,passwordValidate).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                    finish();
                                }
                                else
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, "Errorr ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
                    if(emailValidate.length()==0) {
                        email.setHint("Email field is empty");
                        email.setHintTextColor(Color.RED);
                    }
                    if(passwordValidate.length()==0) {
                        password.setHint("Password filed is empty");
                        password.setHintTextColor(Color.RED);
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

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password.setHint("Password");
                password.setHintTextColor(getColor(R.color.white));
                if(password.getText().length()>0)
                {
                    showHidePassword.setVisibility(View.VISIBLE);
                    showHidePassword.setClickable(true);
                }
                else
                {
                    showHidePassword.setVisibility(View.INVISIBLE);
                    showHidePassword.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    showHidePassword.setImageResource(R.drawable.password_invisible);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                }
                else
                {
                    showHidePassword.setImageResource(R.drawable.password_visible);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                }
            }
        });
    }
}