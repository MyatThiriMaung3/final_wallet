package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout lilo_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intentLogin);
//                finish();
//            }
//        }, 3000);

        lilo_splash = findViewById(R.id.main);

        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .repeat(0)
                .playOn(lilo_splash);

        // Delay and move to the next activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000);
    }
}