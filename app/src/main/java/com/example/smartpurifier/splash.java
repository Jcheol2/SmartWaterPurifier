package com.example.smartpurifier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity{
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_nav_main,menu);

        return false;
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), com.example.smartpurifier.MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
