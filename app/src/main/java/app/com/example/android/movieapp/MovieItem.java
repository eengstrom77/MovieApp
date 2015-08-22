package app.com.example.android.movieapp;

/**
 * Created by e194715 on 8/20/2015.
 */
public class MovieItem {
    String name;
    int image; // drawable reference id

    public MovieItem(String name, int image)
    {
        this.name = name;
        this.image = image;
    }
}
