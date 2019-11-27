package com.autodokta.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    protected boolean _active = true;
    protected  int _splashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
//        theview = (GifImageView) findViewById(R.id.gifImageView);

        Thread splashTread = new Thread(){
            @Override
            public void run() {
                try{
                    int waited = 0;
                    while(_active && waited < _splashTime){
                        sleep(100);
                        if(_active){
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Splash_Screen.this,MainActivity.class));
                    finish();
                }
            };
        };
        splashTread.start();
    }

}
