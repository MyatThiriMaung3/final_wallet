package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddTransactionActivity extends AppCompatActivity {
    private String name = "";
    private double balance = -1;

    private Spinner accountSpinner, categorySpinner;
    private ArrayList<String> accountList, categoryList;
    private ArrayAdapter<String> accountAdapter, categoryAdapter;
    private EditText editTextAmount, editTextNote;
    private Button chooseDayButton;
    private CheckBox checkBoxIncome, checkBoxExpense;
    private String selectedDate = "";
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");

        reference = FirebaseDatabase.getInstance().getReference("User").child(name);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        // Initialize views
        accountSpinner = findViewById(R.id.account_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextNote = findViewById(R.id.editTextNote);
        chooseDayButton = findViewById(R.id.choose_day_button);
        Button saveTransactionButton = findViewById(R.id.save_transaction_button);
        checkBoxIncome = findViewById(R.id.checkBoxIncome);
        checkBoxExpense = findViewById(R.id.checkBoxExpense);

        // Initialize the account list with default accounts
        accountList = new ArrayList<>();
        accountList.add("Select an account");
        accountList.add("Cash");
        accountList.add("Bank");
        accountList.add("Add Account...");

        // Set up the adapter for the account spinner
        accountAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_with_icon, accountList);
        accountAdapter.setDropDownViewResource(R.layout.spinner_item);
        accountSpinner.setAdapter(accountAdapter);

        // Prevent selection of the placeholder
        accountSpinner.setSelection(0, false);

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAccount = accountList.get(position);
                if (selectedAccount.equals("Add Account...")) {
                    showAddAccountDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Initialize the category list with default categories
        categoryList = new ArrayList<>();
        categoryList.add("Select a category");
        categoryList.add("Food");
        categoryList.add("Transport");
        categoryList.add("Shopping");
        categoryList.add("Add Category...");

        // Set up the adapter for the category spinner
        categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_with_addcategory_icon, categoryList);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Prevent selection of the placeholder
        categorySpinner.setSelection(0, false);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryList.get(position);
                if (selectedCategory.equals("Add Category...")) {
                    showAddCategoryDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Date picker
        chooseDayButton.setOnClickListener(v -> showDatePickerDialog());

        checkBoxIncome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxExpense.setChecked(false);
        });

        checkBoxExpense.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxIncome.setChecked(false);
        });

        // Save button functionality
        saveTransactionButton.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString().trim();
            String selectedAccount = accountSpinner.getSelectedItem().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String note = editTextNote.getText().toString().trim();
            boolean isIncome = checkBoxIncome.isChecked();
            boolean isExpense = checkBoxExpense.isChecked();

            if (selectedAccount.equals("Select an account") || amount.isEmpty() || selectedCategory.equals("Select a category") || (!isIncome && !isExpense)) {
                Toast.makeText(AddTransactionActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                double tempAmount = Integer.parseInt(amount);
                if ((tempAmount <= balance && isExpense) || (isIncome)) {
                    Transaction transaction = new Transaction(selectedAccount, selectedCategory, amount, note, selectedDate, isIncome);
                    String transactionId = reference.push().getKey();
                    reference.child("transactions").child(Objects.requireNonNull(transactionId)).setValue(transaction).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (isExpense) {
                                balance -= tempAmount;
                            }

                            if (isIncome) {
                                balance += tempAmount;
                            }

                            reference.child("balance").setValue(balance);
                            Toast.makeText(AddTransactionActivity.this, "Transaction saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddTransactionActivity.this, "Failed to save transaction", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddTransactionActivity.this, "Expense amount exceeds the current balance.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAddAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Account");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newAccount = input.getText().toString().trim();
            if (!newAccount.isEmpty()) {
                accountList.add(accountList.size() - 1, newAccount);
                accountAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Category");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newCategory = input.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                categoryList.add(categoryList.size() - 1, newCategory);
                categoryAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTransactionActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    // Use String.format to ensure two-digit formatting for day and month
                    String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                    String formattedMonth = String.format(Locale.getDefault(), "%02d", (month1 + 1));
                    selectedDate = formattedDay + "/" + formattedMonth + "/" + year1;
                    chooseDayButton.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}