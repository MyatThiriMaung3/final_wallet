package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtBalance;
    private String name;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        reference = FirebaseDatabase.getInstance().getReference("User");

        Intent intentFromLogin = getIntent();
        name = intentFromLogin.getStringExtra("name");

        txtName = findViewById(R.id.txtName);
        txtBalance = findViewById(R.id.txtBalance);

        txtName.setText(name);
    }

    public void switchToNews(View view) {
        Intent intentNews = new Intent(this, NewsActivity.class);
        intentNews.putExtra("name", name);
        startActivity(intentNews);
    }

    public void switchToAccountDetails(View view) {
        Intent intentAccountDetails = new Intent(this, AccountDetailsActivity.class);
        intentAccountDetails.putExtra("name", name);
        startActivity(intentAccountDetails);
    }

    public void switchToAddTransaction(View view) {
        Intent intentAddTransaction = new Intent(this, AddTransactionActivity.class);
        intentAddTransaction.putExtra("name", name);
        startActivity(intentAddTransaction);
    }

    public void seeBalance(View view) {
    }

    public void swtichToTransactionHistory(View view) {
        Intent intentTransactionHistory = new Intent(this, TransactionHistoryActivity.class);
        intentTransactionHistory.putExtra("name", name);
        startActivity(intentTransactionHistory);
    }

    public void switchToAnalysis(View view) {
        Intent intentAnalysis = new Intent(this, AnalysisActivity.class);
        intentAnalysis.putExtra("name", name);
        startActivity(intentAnalysis);
    }
}