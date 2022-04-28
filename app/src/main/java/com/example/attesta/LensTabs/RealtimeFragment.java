package com.example.attesta.LensTabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attesta.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class RealtimeFragment extends Fragment {

    public RealtimeFragment() {
        // Required empty public constructor
    }

    TextView textView;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    public static final int PERMISSION=100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_realtime, container, false);
        textView=view.findViewById(R.id.textview);
        surfaceView=view.findViewById(R.id.surfaceView);
        startCameraSource();
        return view;
    }
    private void startCameraSource(){
        final TextRecognizer textRecognizer=new TextRecognizer.Builder(getContext()).build();
        if(!textRecognizer.isOperational())
        {
            Log.d("TextRecognizer","Dependency not loaded yet");
        }
        else
        {
            cameraSource=new CameraSource.Builder(getContext(),textRecognizer).setFacing(cameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280,1024).setAutoFocusEnabled(true).setRequestedFps(2.0f).build();
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    try {
                        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA},PERMISSION);
                            return;
                        }
                        cameraSource.start(surfaceView.getHolder());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    //Detect all text from camera
                }

                @Override
                public void receiveDetections(@NonNull Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if(items.size()!=0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder=new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }
}