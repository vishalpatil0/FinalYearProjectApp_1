package com.example.attesta.LensTabs;





import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attesta.AttestationActivity;
import com.example.attesta.R;
import com.example.attesta.Validation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class TranslationFragment extends Fragment {

     public TranslationFragment() {
        // Required empty public constructor
    }

    private ImageView lensImage;
    private Button detect,speech,clipboard,viewText,google;
    private String extractedData;
    private FloatingActionButton lensImageButton;
    private TextToSpeech textToSpeech;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_translation, container, false);
         lensImage=view.findViewById(R.id.lensImage);
         detect=view.findViewById(R.id.detect);
         speech=view.findViewById(R.id.speech);
         clipboard=view.findViewById(R.id.clipboard);
         viewText=view.findViewById(R.id.viewText);
         google=view.findViewById(R.id.google);
         lensImageButton=view.findViewById(R.id.lensImageButton);
         textToSpeech=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
         lensImageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ImagePicker.with(getActivity()).crop().start(103);
             }
         });
         detect.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(lensImage.getDrawable()!=null) {
                     BitmapDrawable bitmapDrawable=(BitmapDrawable) lensImage.getDrawable();
                     Validation validation=new Validation(getContext(),bitmapDrawable.getBitmap());
                     try {
                         extractedData = validation.detectText();
                     } catch (Exception e){
                         Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                     speech.setVisibility(View.VISIBLE);
                     speech.setClickable(true);
                     clipboard.setClickable(true);
                     clipboard.setVisibility(View.VISIBLE);
                     viewText.setVisibility(View.VISIBLE);
                     viewText.setClickable(true);
                     google.setClickable(true);
                     google.setVisibility(View.VISIBLE);
                     detect.setVisibility(View.INVISIBLE);
                     detect.setClickable(false);
                     Toast.makeText(getContext(), "Successfully Detected", Toast.LENGTH_SHORT).show();

                 } else {
                     Toast.makeText(getContext(), "Please import your image first", Toast.LENGTH_SHORT).show();
                 }
             }
         });
         speech.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 textToSpeech.speak(extractedData,TextToSpeech.QUEUE_FLUSH,null,null);
             }
         });
         clipboard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ClipboardManager clipboardManager=(ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                 ClipData clip = ClipData.newPlainText("text", extractedData);
                 clipboardManager.setPrimaryClip(clip);
                 Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
             }
         });
         google.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+extractedData));
                 startActivity(browserIntent);
             }
         });
         return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==103) {
            Uri uri = data.getData();
            lensImage.setImageURI(uri);
            speech.setVisibility(View.INVISIBLE);
            speech.setClickable(false);
            clipboard.setVisibility(View.INVISIBLE);
            clipboard.setClickable(false);
            viewText.setVisibility(View.INVISIBLE);
            viewText.setClickable(false);
            google.setVisibility(View.INVISIBLE);
            google.setClickable(false);
            detect.setVisibility(View.VISIBLE);
            detect.setClickable(true);
        }
    }
}