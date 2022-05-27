package com.example.attesta.LensTabs;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attesta.R;
import com.example.attesta.TextActivity;
import com.example.attesta.TranslatorActivity;
import com.example.attesta.Validation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class TranslationFragment extends Fragment {

     public TranslationFragment() {
        // Required empty public constructor
    }

    private ImageView lensImage;
    private Button detect,speech,clipboard,viewText,google,call,translate;
    private String extractedData,mobileNo;
    private FloatingActionButton lensImageButton;
    private TextToSpeech textToSpeech;
    private static final int CALL_PERMISSION_CODE = 100;
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
         call=view.findViewById(R.id.call);
         translate=view.findViewById(R.id.Translate);
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
                     translate.setClickable(true);
                     translate.setVisibility(View.VISIBLE);
                     if(validation.checkMobile(extractedData))
                     {
                         call.setVisibility(View.VISIBLE);
                         call.setClickable(true);
                         mobileNo=validation.getMobileNo();
                     }
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
         viewText.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getActivity(), TextActivity.class);
                 intent.putExtra("extractedData",extractedData);
                 startActivity(intent);
             }
         });
         call.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mobileNo.trim();
                 if(checkPermission(Manifest.permission.CALL_PHONE,CALL_PERMISSION_CODE)) {
                     Intent callIntent = new Intent(Intent.ACTION_CALL);
                     callIntent.setData(Uri.parse("tel:" + mobileNo));
                     callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(callIntent);
                 }
             }
         });
         translate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getActivity(), TranslatorActivity.class);
                 intent.putExtra("extractedData",extractedData);
                 startActivity(intent);
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
            call.setVisibility(View.INVISIBLE);
            call.setClickable(false);
            translate.setVisibility(View.INVISIBLE);
            translate.setClickable(false);
            detect.setVisibility(View.VISIBLE);
            detect.setClickable(true);
        }
    }

    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }
}