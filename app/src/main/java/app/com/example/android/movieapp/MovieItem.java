package app.com.example.android.movieapp;

/**
 * Created by e194715 on 8/20/2015.
 */
public class MovieItem {
    String name;     // title
    String imageUrl; // URL to Movie Poster
    String synopsis; // overview
    String date;     // release date
    double rating;    // vote average

    public MovieItem(String name, String imageUrl, String synopsis, String date, double rating)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.synopsis = synopsis;
        this.date = date;
        this.rating = rating;
    }
}
