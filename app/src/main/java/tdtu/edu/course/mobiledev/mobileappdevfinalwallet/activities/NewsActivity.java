package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.interfaces.NewsAPI;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.adapters.NewsAdapter;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.pojos.NewsResponse;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;

public class NewsActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://newsapi.org/";
    private static final String API_KEY = "e5a3545260d34caaa6aa6c4430892a52"; // Your API key
    private RecyclerView recyclerView;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NewsAdapter newsAdapter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");

        initializeViews();

        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load default news articles
        fetchNews("finance");
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.news_toolbar);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void fetchNews(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsAPI newsAPI = retrofit.create(NewsAPI.class);

        Call<NewsResponse> call = newsAPI.getNews(query, API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsResponse.Article> articles = response.body().articles;
                    if (articles.isEmpty()) {
                        Toast.makeText(NewsActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    }
                    newsAdapter = new NewsAdapter(articles);
                    recyclerView.setAdapter(newsAdapter);
                } else {
                    Toast.makeText(NewsActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                Toast.makeText(NewsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);

        // Search functionality
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchView != null;
        searchView.setQueryHint("Search news...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    fetchNews(query); // Pass the query to fetchNews
                }
                searchView.clearFocus(); // Clear focus to hide keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Handle text changes if needed
            }
        });

        return true;
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}