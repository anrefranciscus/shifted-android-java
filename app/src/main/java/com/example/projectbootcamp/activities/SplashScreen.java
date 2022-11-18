package com.example.projectbootcamp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectbootcamp.R;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    ImageView imageView;

    Animation animRotation;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.logo);


        animRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        imageView.startAnimation(animRotation);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginAcitivty.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}