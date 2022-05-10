package com.example.attesta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class TranslatorActivity extends AppCompatActivity {

    private String extractedData;
    private TextView textView;
    private Spinner spinnerList;
    private Button translate;
    String[] languages={"English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Hindi","Marathi"};
    int toLanguage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        textView=findViewById(R.id.translatedText);
        spinnerList=findViewById(R.id.spinner);
        translate=findViewById(R.id.translateExtractedText);

        extractedData=getIntent().getStringExtra("extractedData");
        textView.setText(extractedData);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.spinner_item,languages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.setAdapter(arrayAdapter);

        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguage=getLanguageCode(languages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Downloading model ...");
                FirebaseTranslatorOptions options=new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(11)
                        .setTargetLanguage(toLanguage)
                        .build();

                FirebaseTranslator translator= FirebaseNaturalLanguage.getInstance().getTranslator(options);

                FirebaseModelDownloadConditions conditions =new FirebaseModelDownloadConditions.Builder().build();

                translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        textView.setText("Translating......");
                        translator.translate(extractedData).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                textView.setText(s);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TranslatorActivity.this, "Failed to translate "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslatorActivity.this, "Failed to download the language model "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public static int getLanguageCode(String language){
        int languageCode=0;
        switch (language){
            case "English":
                languageCode= FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                languageCode= FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                languageCode= FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode= FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                languageCode= FirebaseTranslateLanguage.BG;
                break;
            case "Bengali":
                languageCode= FirebaseTranslateLanguage.BN;
                break;
            case "Hindi":
                languageCode= FirebaseTranslateLanguage.HI;
                break;
            case "Marathi":
                languageCode= FirebaseTranslateLanguage.MR;
                break;
            default:
                languageCode=0;
        }
        return languageCode;
    }
}