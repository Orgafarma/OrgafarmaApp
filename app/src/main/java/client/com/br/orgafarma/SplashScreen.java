package client.com.br.orgafarma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    int progress = 0;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++){
                    progress += 20;

                    handler.post(new Runnable() {
                        @Override
                        public void run(){
                            progressBar.setProgress(progress);

                            if(progress == progressBar.getMax()){
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {

                    }

                }
            }
        }).start();
    }
}
