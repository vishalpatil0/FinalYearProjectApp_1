package com.example.attesta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AttestationActivity extends AppCompatActivity{

    private FloatingActionButton originalImageButton,formImageButton;
    private ImageView originalImage,formImage;
    private Button validate,ready;
    private String originalImageString="vishal",formImageString;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation);

        //Initializing objects with their respective resource IDs
        originalImageButton=(FloatingActionButton) findViewById(R.id.originalImageButton);
        originalImage =(ImageView)findViewById(R.id.originalImage);
        formImageButton=(FloatingActionButton)findViewById(R.id.formImageButton);
        formImage=(ImageView)findViewById(R.id.formImage);
        validate=(Button)findViewById(R.id.validate);
        progressBar=findViewById(R.id.progressBar);

        /**
         * originalImageButton on click listener to launch the Imagepicker library
         */
        originalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AttestationActivity.this).crop().start(101);
            }
        });

        /**
         * formImageButton to import the image.
         */
        formImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AttestationActivity.this).crop().start(102);
            }
        });

        /**
         * Validate function here will compare the UID from 2 images.
         */
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(originalImage.getDrawable()!=null)
                {
                    BitmapDrawable bitmapDrawable= (BitmapDrawable) originalImage.getDrawable();
                    Bitmap originalImageBitmap = bitmapDrawable.getBitmap();
                    String UID="";
                    Validation validation=new Validation(AttestationActivity.this,originalImageBitmap);
                    UID=validation.detect();
                    Toast.makeText(AttestationActivity.this, "Asim : "+UID, Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(AttestationActivity.this, "Please import your images first", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     *
     * @param requestCode to identify which view have requested the data
     * @param resultCode  not needed here
     * @param data  contains the image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            Uri uri=data.getData();
            originalImage.setImageURI(uri);
        }
        else if(requestCode==102)
        {
            Uri uri=data.getData();
            formImage.setImageURI(uri);
        }
    }

}