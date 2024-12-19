package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewHome;
    private ImageView imgSettings;
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

        drawerLayout = findViewById(R.id.main);
        navigationViewHome = findViewById(R.id.navigationViewHome);
        imgSettings = findViewById(R.id.imgSettings);
        txtName = findViewById(R.id.txtName);
        txtBalance = findViewById(R.id.txtBalance);

        txtName.setText(name);

        imgSettings.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationViewHome)) {
                drawerLayout.closeDrawer(navigationViewHome);
            } else {
                drawerLayout.openDrawer(navigationViewHome);
            }
        });

        // Handle navigation item clicks
        navigationViewHome.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Close the drawer after selection
                drawerLayout.closeDrawer(navigationViewHome);

                // Handle menu item clicks
                switch (item.getTitle().toString()) {
                    case "Home":
                        // not doing anything as we are currently in home activity
                        break;
                    case "Account":
                        switchToAccountDetails();
                        break;
                    case "Calculator":
                        Toast.makeText(HomeActivity.this, "Calculator clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "News":
                        switchToNews();
                        break;
                    case "Debts":
                        Toast.makeText(HomeActivity.this, "Debts clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "Theme":
                        Toast.makeText(HomeActivity.this, "Theme clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "Language":
                        Toast.makeText(HomeActivity.this, "Language clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "Font size":
                        Toast.makeText(HomeActivity.this, "Font Size clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case "Updates":
                        Toast.makeText(HomeActivity.this, "Updates clicked", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

    }

    public void switchToNews() {
        Intent intentNews = new Intent(this, NewsActivity.class);
        intentNews.putExtra("name", name);
        startActivity(intentNews);
    }

    public void news(View view) {
        switchToNews();
    }

    public void switchToAccountDetails() {
        Intent intentAccountDetails = new Intent(this, AccountDetailsActivity.class);
        intentAccountDetails.putExtra("name", name);
        startActivity(intentAccountDetails);
    }

    public void accountDetails(View view) {
        switchToAccountDetails();
    }

    public void switchToAddTransaction() {
        Intent intentAddTransaction = new Intent(this, AddTransactionActivity.class);
        intentAddTransaction.putExtra("name", name);
        startActivity(intentAddTransaction);
    }

    public void addTransaction(View view) {
        switchToAddTransaction();
    }

    public void seeBalance(View view) {
    }

    public void switchToTransactionHistory() {
        Intent intentTransactionHistory = new Intent(this, TransactionHistoryActivity.class);
        intentTransactionHistory.putExtra("name", name);
        startActivity(intentTransactionHistory);
    }

    public void transactionHistory(View view) {
        switchToTransactionHistory();
    }

    public void switchToAnalysis() {
        Intent intentAnalysis = new Intent(this, AnalysisActivity.class);
        intentAnalysis.putExtra("name", name);
        startActivity(intentAnalysis);
    }

    public void analysis(View view) {
        switchToAnalysis();
    }
}