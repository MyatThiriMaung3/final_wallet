package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.activities;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.pojos.Transaction;

public class AddTransactionActivity extends AppCompatActivity {
    private String name = "";
    private double balance = -1;

    private Spinner accountSpinner, categorySpinner;
    private ArrayList<String> accountList, categoryList;
    private ArrayAdapter<String> accountAdapter, categoryAdapter;
    private EditText editTextAmount, editTextNote;
    private Button chooseDayButton;
    private Button saveTransactionButton;
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

        createNotificationChannel();

        loadBalance();
        initializeViews();

        accountSpinnerSetUp();

        categorySpinnerSetUp();

        setDate();
        validateCheckboxes();
        saveTransactionButtonEventHandler();
    }

    private void categorySpinnerSetUp() {
        initializeCategoryList();
        setCategorySpinnerAdapter();
        categorySpinner.setSelection(0, false);
        categorySpinnerEventHandler();
    }

    private void accountSpinnerSetUp() {
        initializeAccountList();
        setAccountSpinnerAdapter();
        accountSpinner.setSelection(0, false);
        accountSpinnerEventHandler();
    }

    private void categorySpinnerEventHandler() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryList.get(position);
                if (selectedCategory.equals(getString(R.string.add_category))) {
                    showAddCategoryDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void accountSpinnerEventHandler() {
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAccount = accountList.get(position);
                if (selectedAccount.equals(getString(R.string.add_account))) {
                    showAddAccountDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void validateCheckboxes() {
        checkBoxIncome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxExpense.setChecked(false);
        });

        checkBoxExpense.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxIncome.setChecked(false);
        });
    }

    private void setDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the current date
        selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year);
        chooseDayButton.setText(selectedDate);

        // Date picker
        chooseDayButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private void saveTransactionButtonEventHandler() {
        saveTransactionButton.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString().trim();
            String selectedAccount = accountSpinner.getSelectedItem().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String note = editTextNote.getText().toString().trim();
            boolean isIncome = checkBoxIncome.isChecked();
            boolean isExpense = checkBoxExpense.isChecked();

            if (selectedAccount.equals(getString(R.string.select_an_account)) || amount.isEmpty() || selectedCategory.equals(getString(R.string.select_a_category)) || (!isIncome && !isExpense)) {
                Toast.makeText(AddTransactionActivity.this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddTransactionActivity.this, getString(R.string.transaction_saved), Toast.LENGTH_SHORT).show();
                            showNotification(selectedCategory, amount, isIncome);
                        } else {
                            Toast.makeText(AddTransactionActivity.this, getString(R.string.failed_to_save_transaction), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddTransactionActivity.this, R.string.expense_amount_exceeds_the_current_balance, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAccountSpinnerAdapter() {
        accountAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_with_icon, accountList);
        accountAdapter.setDropDownViewResource(R.layout.item_spinner);
        accountSpinner.setAdapter(accountAdapter);
    }

    private void setCategorySpinnerAdapter() {
        categoryAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_with_add_category, categoryList);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void initializeCategoryList() {
        categoryList = new ArrayList<>();
        categoryList.add(getString(R.string.select_a_category));
        categoryList.add(getString(R.string.food));
        categoryList.add(getString(R.string.transport));
        categoryList.add(getString(R.string.shopping));
        categoryList.add(getString(R.string.add_category));
    }

    private void initializeAccountList() {
        accountList = new ArrayList<>();
        accountList.add(getString(R.string.select_an_account));
        accountList.add(getString(R.string.cash));
        accountList.add(getString(R.string.bank));
        accountList.add(getString(R.string.add_account));
    }

    private void initializeViews() {
        accountSpinner = findViewById(R.id.account_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextNote = findViewById(R.id.editTextNote);
        chooseDayButton = findViewById(R.id.choose_day_button);
        saveTransactionButton = findViewById(R.id.save_transaction_button);
        checkBoxIncome = findViewById(R.id.checkBoxIncome);
        checkBoxExpense = findViewById(R.id.checkBoxExpense);
    }

    private void loadBalance() {
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
                Log.w(TAG, getString(R.string.failed_to_read_value), error.toException());
            }
        });
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
            temp = getString(R.string.income1);
        } else {
            temp = getString(R.string.expense1);
        }
        String channelId = "transaction_notifications";
        int notificationId = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_tdt)
                .setContentTitle(getString(R.string.transaction_saved))
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
        builder.setTitle(getString(R.string.add_category));
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            String newCategory = input.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                categoryList.add(categoryList.size() - 1, newCategory);
                categoryAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
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