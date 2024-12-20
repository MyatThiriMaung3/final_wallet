package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewHome;
    private ImageView imgSettings;
    private TextView txtName;
    private TextView txtBalance;
    private String name = "";
    private double balance = -1;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Intent intentFromLogin = getIntent();
        name = intentFromLogin.getStringExtra("name");

        reference = FirebaseDatabase.getInstance().getReference("User");

        createNotificationChannel();
        scheduleDailyReport();

        // Check and request storage permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!isStoragePermissionGranted()) {
                requestStoragePermission();
            }
        }

        reference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double tempBalance = snapshot.child("balance").getValue(Double.class);
                    if (tempBalance != null) {
                        balance = tempBalance;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


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
                        switchToCalculator();
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
                    case "Logout":
                        logout();
                        break;
                }

                return true;
            }
        });

    }

    private void switchToCalculator() {
        Intent intentCalculator = new Intent(this, CalculatorActivity.class);
        intentCalculator.putExtra("name", name);
        startActivity(intentCalculator);
    }

    private void scheduleDailyReport() {
        // Set the alarm time to 10 PM
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23); // 11 PM
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        // Create the alarm intent
        Intent intent = new Intent(this, DailyReportReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    private void createNotificationChannel() {
        String channelId = "transaction_notifications";
        CharSequence channelName = "Transaction Notifications";
        String channelDescription = "Notifications for successful transactions";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription(channelDescription);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String category, String amount, boolean isIncome) {
        String temp;
        if (isIncome) {
            temp = "INCOME, ";
        } else {
            temp = "EXPENSE. ";
        }
        String channelId = "transaction_notifications";
        int notificationId = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.tdt_logo)
                .setContentTitle("Transaction Saved")
                .setContentText(temp + "Category: " + category + ", Amount: " + amount)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Show the notification
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        notificationManager.notify(notificationId, builder.build());
    }

    private void logout() {
        Intent intentLogout = new Intent(this, LoginActivity.class);
        startActivity(intentLogout);
        finish();
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
        if (txtBalance.getText().toString().equals("***** VND")) {
            txtBalance.setText(balance + " VND");
        } else {
            txtBalance.setText("***** VND");
        }
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

    public void topUp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Top Up");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Top Up", (dialog, which) -> {
            String addBalance = input.getText().toString().trim();
            if (!addBalance.isEmpty()) {
                double newBalance = balance + Double.parseDouble(addBalance);
                reference.child(name).child("balance").setValue(newBalance);
                balance = newBalance;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(new Date());

                Transaction transaction = new Transaction("Bank", "Top Up", addBalance, "From Top Up", formattedDate, true);
                String transactionId = reference.push().getKey();
                reference.child(name).child("transactions").child(Objects.requireNonNull(transactionId)).setValue(transaction).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Transaction from TopUp saved!", Toast.LENGTH_SHORT).show();
                        showNotification("Top Up", addBalance, true);
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to save transaction", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void exportCSV(View view) {
        exportToCsv(this);
    }

    private boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void exportToCsv(Context context) {
        String[] data = {"This", "is", "the", "list", "of", "empty", "transactions"};
        String fileName = "transactions_" + name + ".csv";

        OutputStream outputStream = null;
        try {
            Uri fileUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 (API 29) and above
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                // Insert the file into MediaStore
                fileUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            } else {
                // For Android 9 and below
                File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File csvFile = new File(downloadsDirectory, fileName);
                fileUri = Uri.fromFile(csvFile);
            }

            if (fileUri != null) {
                // Open the output stream for writing
                outputStream = context.getContentResolver().openOutputStream(fileUri);

                // Write data to the CSV
                StringBuilder csvContent = new StringBuilder();
                csvContent.append("Index, Value\n"); // Optional header
                for (int i = 0; i < data.length; i++) {
                    csvContent.append(i + 1).append(", ").append(data[i]).append("\n");
                }

                if (outputStream != null) {
                    outputStream.write(csvContent.toString().getBytes());
                    outputStream.flush();
                }

                Toast.makeText(context, "CSV exported successfully to Downloads.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Failed to create CSV file.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting CSV: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Close the stream
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}