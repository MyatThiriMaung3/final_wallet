package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class TransactionHistoryActivity extends AppCompatActivity {
    private RecyclerView rvTransaction;
    private SearchView svTransaction;
    private Spinner spinnerSearches;
    private LinkedList<Transaction> transactions;
    private ArrayList<String> searches;
    private int searchPosition;
    private TransactionAdapter adapter;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");

        rvTransaction = findViewById(R.id.rvTransaction);
        svTransaction = findViewById(R.id.svTransaction);
        spinnerSearches = findViewById(R.id.spinnerSearches);

        transactions = loadData();
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

//    private LinkedList<Transaction> loadData() {
//        transactions = new LinkedList<>();
//
//        // Get the Firebase Database reference
//        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");
//
//        // Add a listener to read data from Firebase
//        transactionsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                transactions.clear();  // Clear the current list to avoid duplication
//
//                // Iterate through the transactions in the database
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Transaction transaction = snapshot.getValue(Transaction.class);
//                    transactions.add(transaction);
//                }
//
//                // Notify the adapter of data changes to update the RecyclerView
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(TransactionHistoryActivity.this, "Failed to load transactions.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return transactions;
//    }

    private LinkedList<Transaction> loadData() {
        transactions = new LinkedList<>();

        for (int i = 1; i <= 50; i++) {
            if (i > 9 && i <= 31) {
                if (i % 2 == 0) {
                    transactions.add(new Transaction("admin", "Transport", "" + (i * 100000), "Blah Blah", i + "/07/2024", true));
                } else {
                    transactions.add(new Transaction("admin", "Shopping", "" + (i * 30000), "This is the note for the testing. Ha Ha.", i + "/07/2024", false));
                }
            } else {
                transactions.add(new Transaction("admin", "Food", "" + (i * 10000 ), "Nothing to say", "07/09/2024", false));
            }

        }

        for (int i = 1; i <= 50; i++) {
            if (i > 9 && i <= 31) {
                if (i % 2 == 0) {
                    transactions.add(new Transaction("admin", "Transport", "" + (i * 30000), "Blah Blah", i + "/09/2024", false));
                } else {
                    transactions.add(new Transaction("admin", "Shopping", "" + (i * 7000), "This is the note for the testing. Ha Ha.", i + "/08/2024", false));
                }
            } else {
                transactions.add(new Transaction("admin", "Others", "" + (i * 10000 ), "Nothing to say", "17/09/2024", false));
            }

        }

        for (int i = 1; i <= 50; i++) {
            if (i > 9 && i <= 30) {
                if (i % 3 == 0) {
                    transactions.add(new Transaction("admin", "Transport", "" + (i * 30000), "Blah Blah", i + "/07/2024", false));
                } else {
                    transactions.add(new Transaction("admin", "Food", "" + (i * 30000), "This is the note for the testing. Ha Ha.", i + "/07/2024", false));
                }
            } else {
                transactions.add(new Transaction("admin", "Shopping", "" + (i * 20000 ), "Nothing to say", "03/09/2024", true));
            }

        }

        return transactions;
    }

    public void switchAnalysisActivity(View view) {
        Intent intentAnalysis = new Intent(this, AnalysisActivity.class);
        startActivity(intentAnalysis);
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}