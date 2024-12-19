package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText edt_registerName;
    private EditText edt_registerPhone;
    private EditText edt_registerPassword;
    private EditText edt_registerRepeatPassword;
    private Button btn_register;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        reference = FirebaseDatabase.getInstance().getReference("User");

        edt_registerName = findViewById(R.id.edt_registerName);
        edt_registerPhone = findViewById(R.id.edt_registerPhone);
        edt_registerPassword = findViewById(R.id.edt_registerPassword);
        edt_registerRepeatPassword = findViewById(R.id.edt_registerRepeatPassword);
        btn_register = findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_registerName.getText().toString().trim();
                String phone = edt_registerPhone.getText().toString().trim();
                String password = edt_registerPassword.getText().toString().trim();
                String repeatPassword = edt_registerRepeatPassword.getText().toString().trim();


                if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repeatPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password and confirmed password don't match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(name, phone, password, 0);
                reference.child(name).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "You have signed up successfully", Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intentLogin);
                                finish();
                            }
                        }, 1000);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Firebase registration failed", task.getException());
                    }
                });
            }
        });
    }

    public void switchToLogin(View view) {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
        finish();
    }
}