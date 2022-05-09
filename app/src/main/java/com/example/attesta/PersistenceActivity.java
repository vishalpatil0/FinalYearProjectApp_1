package com.example.attesta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.common.InputImage;

//Java Imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PersistenceActivity extends AppCompatActivity {

    private Button validate;
    private String temp;
    private ImageView imageView;
    private TextView textView;
    private BitmapDrawable bitmapDrawable;
    String adharName="A",formName="F";
    String uidTV="uidTV",resultTv="resultTV";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistence);
        validate=findViewById(R.id.persistenceValidate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i <=11; i++) {
                    String adharUID,formUID;

                    //adhar image string extractor
                    temp=adharName+String.valueOf(i);
                    int resID=getResources().getIdentifier(temp,"id",getPackageName());
                    imageView=findViewById(resID);
                    bitmapDrawable=(BitmapDrawable) imageView.getDrawable();
                    Validation validation=new Validation(PersistenceActivity.this,bitmapDrawable.getBitmap());
                        adharUID = validation.detect();
                        adharUID = validation.extractor(adharUID);


                    //setting UID textview
                    temp=uidTV+String.valueOf(i);
                    resID=getResources().getIdentifier(temp,"id",getPackageName());
                    textView=findViewById(resID);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(getColor(R.color.black));
                    textView.setPadding(0,24,0,0);
                    textView.setTextSize(20);
                    textView.setText(adharUID);

                    //form image string extractor
                    temp=formName+String.valueOf(i);
                    resID=getResources().getIdentifier(temp,"id",getPackageName());
                    imageView=findViewById(resID);
                    bitmapDrawable=(BitmapDrawable) imageView.getDrawable();
                    validation.setImageBitmap(bitmapDrawable.getBitmap());
                    formUID=validation.detect();
                    formUID=validation.extractor(formUID);

                    //comparing the UID's
                    temp=resultTv+String.valueOf(i);
                    resID=getResources().getIdentifier(temp,"id",getPackageName());
                    textView=findViewById(resID);
                    textView.setTextSize(20);
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0,24,0,0);
                    if(adharUID.equals(formUID))
                    {
                        textView.setText("Verified");
                        textView.setTextColor(Color.GREEN);
                    }
                    else
                    {
                        textView.setText("Not matched!");
                        textView.setTextColor(getColor(R.color.yellow));
                    }
                }
            }
        });
    }
}