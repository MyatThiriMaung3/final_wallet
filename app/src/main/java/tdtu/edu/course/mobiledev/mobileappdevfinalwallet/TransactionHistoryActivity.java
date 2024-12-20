package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class TransactionHistoryActivity extends AppCompatActivity {
    private TextView txtAvailableBalance;
    private TextView txtName;
    private RecyclerView rvTransaction;
    private SearchView svTransaction;
    private Spinner spinnerSearches;
    private LinkedList<Transaction> transactions;
    private ArrayList<String> searches;
    private int searchPosition;
    private TransactionAdapter adapter;
    private String name = "";
    private double balance = -1;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");

        txtAvailableBalance = findViewById(R.id.txtAvailableBalance);
        txtName = findViewById(R.id.txtName);
        rvTransaction = findViewById(R.id.rvTransaction);
        svTransaction = findViewById(R.id.svTransaction);
        spinnerSearches = findViewById(R.id.spinnerSearches);

        reference = FirebaseDatabase.getInstance().getReference("User").child(name);

        txtName.setText(name);

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

        loadData();
        loadSearches();

        adapter = new TransactionAdapter(transactions, searchPosition);
        rvTransaction.setAdapter(adapter);

        ArrayAdapter<String> searchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searches);
        searchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearches.setAdapter(searchesAdapter);

        // setting default value for the spinnerSearches
        spinnerSearches.setSelection(0);

        // setting the on click item listener for the selected spinner item
        spinnerSearches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchPosition = position;
                svTransaction.setQueryHint("Search By " + searches.get(position));


                // setting the adapter for the recyclerview again after searchPosition is updated
                adapter = new TransactionAdapter(transactions, searchPosition);
                rvTransaction.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rvTransaction.setLayoutManager(new LinearLayoutManager(this));


        svTransaction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadSearches() {
        searches = new ArrayList<>();

        searches.add("category");
        searches.add("amount");
        searches.add("date");
    }

    private void loadData() {
        transactions = new LinkedList<>();

        reference.child("transactions").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);

                    if (transaction != null) {
                        transactions.addFirst(transaction);
                    }
                }

                // Notify the adapter that the data has changed
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or display the error
                Log.e(TAG, "Failed to load transactions", error.toException());
            }
        });
    }

    public void switchAnalysisActivity(View view) {
        Intent intentAnalysis = new Intent(this, AnalysisActivity.class);
        intentAnalysis.putExtra("name", name);
        startActivity(intentAnalysis);
        finish();
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }

    public void seeBalance(View view) {
        if (txtAvailableBalance.getText().toString().equals("***** VND")) {
            txtAvailableBalance.setText(balance + " VND");
        } else {
            txtAvailableBalance.setText("***** VND");
        }
    }
}