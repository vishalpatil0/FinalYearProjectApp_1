package com.example.attesta.LensTabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attesta.AttestationActivity;
import com.example.attesta.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TranslationFragment extends Fragment {

     public TranslationFragment() {
        // Required empty public constructor
    }

    private ImageView lensImage;
    private Button detect;
    private FloatingActionButton lensImageButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_translation, container, false);
         lensImage=view.findViewById(R.id.lensImage);
         detect=view.findViewById(R.id.detect);
         lensImageButton=view.findViewById(R.id.lensImageButton);
         
         lensImageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ImagePicker.with(getActivity()).crop().start(103);
             }
         });
         detect.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(lensImage.getDrawable()!=null)
                 {

                 }
                 else
                 {
                     Toast.makeText(getContext(), "Please import your image first", Toast.LENGTH_SHORT).show();
                 }
             }
         });
         return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
            Uri uri=data.getData();
            lensImage.setImageURI(uri);
    }
}