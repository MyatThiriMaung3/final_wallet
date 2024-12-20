package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.pojos.NewsResponse;

public interface NewsAPI {

    @GET("v2/everything")
    Call<NewsResponse> getNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}
