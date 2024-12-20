package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.authentications.LoginActivity;

public class AccountDetailsActivity extends AppCompatActivity {
    private String name = "";
    private String phone = "1";
    private String password = "";
    private double balance = -1;
    private DatabaseReference reference;
    private TextView txtName;
    private TextView txtUserName;
    private TextView txtPhone;
    private TextView txtPassword;
    private TextView txtBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");

        reference = FirebaseDatabase.getInstance().getReference("User").child(name);

        initializeViews();
        setViewValues();
        loadDataFromFirebase();
    }

    private void setViewValues() {
        txtUserName.setText(name);
        txtName.setText(name);
    }

    private void loadDataFromFirebase() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tempPhone = snapshot.child("phone").getValue(String.class);
                    if (tempPhone != null) {
                        phone = tempPhone;
                        txtPhone.setText(phone);
                    }

                    String tempPassword = snapshot.child("password").getValue(String.class);
                    if (tempPassword != null) {
                        password = tempPassword;
                    }

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
    }

    private void initializeViews() {
        txtUserName = findViewById(R.id.txtUserName);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtBalance = findViewById(R.id.txtBalance);
    }

    public void seeBalance(View view) {
        if (txtBalance.getText().toString().equals("***** VND")) {
            txtBalance.setText(balance + " VND");
        } else {
            txtBalance.setText("***** VND");
        }
    }

    public void logout(View view) {
        Intent intentLogout = new Intent(this, LoginActivity.class);
        startActivity(intentLogout);
        finish();
    }

    public void viewPassword(View view) {
        if (txtPassword.getText().toString().equals("*****")) {
            txtPassword.setText(password);
        } else {
            txtPassword.setText("*****");
        }
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}