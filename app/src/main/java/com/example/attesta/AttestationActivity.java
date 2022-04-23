package com.example.attesta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AttestationActivity extends AppCompatActivity {

    private FloatingActionButton originalImageButton,formImageButton;
    private ImageView orignalImage,formImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation);
        originalImageButton=(FloatingActionButton) findViewById(R.id.originalImageButton);
        orignalImage=(ImageView)findViewById(R.id.originalImage);
        formImageButton=(FloatingActionButton)findViewById(R.id.formImageButton);
        formImage=(ImageView)findViewById(R.id.formImage);
        originalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AttestationActivity.this).crop().start(101);
            }
        });
        formImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AttestationActivity.this).crop().start(102);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            Uri uri=data.getData();
            orignalImage.setImageURI(uri);
        }
        else if(requestCode==102)
        {
            Uri uri=data.getData();
            formImage.setImageURI(uri);
        }
    }
}