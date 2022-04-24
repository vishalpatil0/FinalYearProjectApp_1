package com.example.attesta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation top,bottom;
    ImageView logo;
    TextView slogan,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Animations
        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        logo=findViewById(R.id.logo);
        title=findViewById(R.id.title);
        slogan=findViewById(R.id.slogan);
        logo.setAnimation(top);
        title.setAnimation(bottom);
        slogan.setAnimation(bottom);

        //hides the action bar
        getSupportActionBar().hide();

        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}