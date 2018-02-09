package shmehdi.demo.notely;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(){
            public void run(){
                try {
                    Thread.sleep(3000);
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        setContentView(R.layout.activity_splash_screen);
    }
}
