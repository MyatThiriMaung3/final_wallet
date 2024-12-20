package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.pojos;

import java.util.List;

public class NewsResponse {
    public String status;
    public int totalResults;
    public List<Article> articles;

    public NewsResponse() {
    }

    public static class Article {
        public String title;
        public String description;
        public String url;
        public String urlToImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
