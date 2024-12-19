package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

    }

    public void switchToNews(View view) {
        Intent intentNews = new Intent(this, NewsActivity.class);
        startActivity(intentNews);
    }

    public void switchToAccountDetails(View view) {
        Intent intentAccountDetails = new Intent(this, AccountDetailsActivity.class);
        startActivity(intentAccountDetails);
    }

    public void switchToAddTransaction(View view) {
        Intent intentAddTransaction = new Intent(this, AddTransactionActivity.class);
        startActivity(intentAddTransaction);
    }
}