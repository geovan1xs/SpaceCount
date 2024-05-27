package com.example.spacecount;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class TelaInicial extends AppCompatActivity {
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(TelaInicial.this, Home.class);
                startActivity(i);
                finish();
            }
        },3000);


    }
}