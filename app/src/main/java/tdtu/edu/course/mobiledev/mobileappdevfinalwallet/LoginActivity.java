package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_loginName;
    private EditText edt_loginPassword;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        reference = FirebaseDatabase.getInstance().getReference("User");

        edt_loginName = findViewById(R.id.edt_loginName);
        edt_loginPassword = findViewById(R.id.edt_loginPassword);
    }

    public void switchToRegister(View view) {
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
        finish();
    }

    public void login(View view) {
        String name = edt_loginName.getText().toString().trim();
        String password = edt_loginPassword.getText().toString().trim();

        Query checkUserDatabase = reference.orderByChild("username").equalTo(name);

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        reference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edt_loginName.setError(null);
                    String passwordFromDB = snapshot.child("password").getValue(String.class);
                    if (passwordFromDB.equals(password)){
                        edt_loginName.setError(null);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();

                    }else{
                        edt_loginPassword.setError("Invalid");
                        edt_loginPassword.requestFocus();
                    }
                }else{
                    edt_loginName.setError("User does not exits");
                    edt_loginName.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}