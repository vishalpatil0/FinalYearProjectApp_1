package com.example.attesta;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

public class Validation {
    private InputImage originalImage;
    private Context context;
    private String UID_Data="";
    private TextRecognizer recognizer;

    /**
     *
     * @param bitmap bitmap of images from imageviews on attesation_activity
     */
    public Validation(Context context,Bitmap bitmap)
    {
        this.context=context;
        originalImage=InputImage.fromBitmap(bitmap,0);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    /**
     *
     * @return string which contain the recognized text
     */
    public String detect()
    {
        final String[] UID = {""};
        Task<Text> result =
                recognizer.process(originalImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                Log.d("recognizer is successful","InputImage got recognized successfully");
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to detect text form image..", Toast.LENGTH_SHORT).show();
                                    }

                                });
        //Infinite loop
        while(true)
        {
            if(result.isComplete())
            {
                if(result.isSuccessful())
                {
                    List<Text.TextBlock> textBlocks=result.getResult().getTextBlocks();
                    for(int i=0;i<textBlocks.size();i++)
                    {
                        List<Text.Line> textLines=textBlocks.get(i).getLines();
                        for(int j=0;j<textLines.size();j++)
                        {
                            List<Text.Element> elements=textLines.get(j).getElements();
                            for(int k=0;k<elements.size();k++)
                            {
                                UID[0] +=elements.get(k).getText();
                            }
                        }
                    }
                }
                else
                {
                    UID[0]="Failed";
                    Log.d("result failed","TextRecognizer failed due to some reason");
                }
                break;
            }
        }
        return UID[0];
    }
    public String extractor(String str)
    {
        int startIndex=-1,endIndex=0,count=0;
        char stringArray[] = str.toCharArray();
        for (int i = 0; i < stringArray.length; i++) {   //string.length is 23
            if(stringArray[i]>=48 && stringArray[i]<=57)
            {
                if(startIndex==-1)
                {
                    startIndex=i;
                }
                count++;
                if(count==12)
                {
                    endIndex=i+1;
                    break;
                }
            }
            else
            {
                startIndex=-1;
                count=0;
            }
        }
        if(startIndex==-1)
        {
            Toast.makeText(context, "UID extraction failed", Toast.LENGTH_SHORT).show();
            return "000000000000";
        }
        return str.substring(startIndex,endIndex);
    }
    public void setImageBitmap(Bitmap bitmap)
    {
        this.originalImage=InputImage.fromBitmap(bitmap,0);
    }

    /**
     * This function returns the string with spaces in it.
     * @return recognized text
     */
    public String detectText()
    {
        final String[] UID = {""};
        Task<Text> result =
                recognizer.process(originalImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                Log.d("recognizer is successful","InputImage got recognized successfully");
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to detect text form image..", Toast.LENGTH_SHORT).show();
                                    }

                                });
        //Infinite loop
        while(true)
        {
            if(result.isComplete())
            {
                if(result.isSuccessful())
                {
                    List<Text.TextBlock> textBlocks=result.getResult().getTextBlocks();
                    for(int i=0;i<textBlocks.size();i++)
                    {
                        List<Text.Line> textLines=textBlocks.get(i).getLines();
                        for(int j=0;j<textLines.size();j++)
                        {
                            UID[0]+=textLines.get(j).getText();
                        }
                        UID[0]+="\n";
                    }
                }
                else
                {
                    UID[0]="Failed";
                    Log.d("result failed","TextRecognizer failed due to some reason");
                }
                break;
            }
        }
        return UID[0].trim();
    }
}