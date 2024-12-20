package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.authentications.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout lilo_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        lilo_splash = findViewById(R.id.main);

        addAnimation();
        delayMultithreading();
    }

    private void delayMultithreading() {
        //        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intentLogin);
//                finish();
//            }
//        }, 3000);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000);
    }

    private void addAnimation() {
        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .repeat(0)
                .playOn(lilo_splash);
    }
}