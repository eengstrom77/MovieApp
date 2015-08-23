package app.com.example.android.movieapp;

/**
 * Created by e194715 on 8/20/2015.
 */
public class MovieItem {
    String name;
    String imageUrl; // drawable reference id

    public MovieItem(String name, String imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
