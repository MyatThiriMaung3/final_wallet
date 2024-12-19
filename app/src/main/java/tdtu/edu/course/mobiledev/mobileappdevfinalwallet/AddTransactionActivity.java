package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTransactionActivity extends AppCompatActivity {
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}