package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

    }

    public void switchToRegister(View view) {
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
        finish();
    }

    public void switchToHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish();
    }
}