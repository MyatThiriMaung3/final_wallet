package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountDetailsActivity extends AppCompatActivity {
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");
    }

    public void seeBalance(View view) {
    }

    public void logout(View view) {
        Intent intentLogout = new Intent(this, LoginActivity.class);
        startActivity(intentLogout);
        finish();
    }

    public void viewPassword(View view) {
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}