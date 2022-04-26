package com.example.attesta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class HomeActivity extends AppCompatActivity {

    private CardView Attestation,lens;
    private TextView currentUser;
    private ImageView logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String UserID;
    private Boolean rememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Attestation=(CardView) findViewById(R.id.attestation);
        logout=findViewById(R.id.logout);
        currentUser=findViewById(R.id.currentUser);
        lens=findViewById(R.id.lens);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        rememberMe=getIntent().getBooleanExtra("status",true);
            //getting UID of current user.
            UserID = firebaseAuth.getCurrentUser().getUid();

            DocumentReference documentReference = firebaseFirestore.collection("Users").document(UserID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null)
                    {
                        Log.d("snapshot listener","Snapshot Listener "+error.toString());
                    }
                    else if(value.exists())
                    {
                        currentUser.setText(value.getString("fName"));
                    }
                    else
                    {
                        Log.d("snapshot","something else gone wrong");
                    }
                }
            });
        Attestation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AttestationActivity.class));
            }
        });
        lens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,LensActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!rememberMe){
            FirebaseAuth.getInstance().signOut();
        }
    }
}