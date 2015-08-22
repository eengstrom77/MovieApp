package app.com.example.android.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieItemAdapter movieAdapter;

    MovieItem[] movieItems = {
            new MovieItem("Movie1", R.drawable.exampleposterw343),
            new MovieItem("Movie2", R.drawable.exampleposterw343),
            new MovieItem("Movie3", R.drawable.exampleposterw343),
            new MovieItem("Movie4", R.drawable.exampleposterw343),
            new MovieItem("Movie5", R.drawable.exampleposterw343),
            new MovieItem("Movie6", R.drawable.exampleposterw343),
            new MovieItem("Movie7", R.drawable.exampleposterw343),
            new MovieItem("Movie8", R.drawable.exampleposterw343),
            new MovieItem("Movie9", R.drawable.exampleposterw343),
            new MovieItem("Movie10", R.drawable.exampleposterw343),
            new MovieItem("Movie11", R.drawable.exampleposterw343),
            new MovieItem("Movie12", R.drawable.exampleposterw343),
            new MovieItem("Movie13", R.drawable.exampleposterw343),
            new MovieItem("Movie14", R.drawable.exampleposterw343),
            new MovieItem("Movie15", R.drawable.exampleposterw343),
            new MovieItem("Movie16", R.drawable.exampleposterw343),
            new MovieItem("Movie17", R.drawable.exampleposterw343),
            new MovieItem("Movie18", R.drawable.exampleposterw343),
            new MovieItem("Movie19", R.drawable.exampleposterw343),
            new MovieItem("Movie20", R.drawable.exampleposterw343)
    };
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieItemAdapter(getActivity(), Arrays.asList(movieItems));

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieAdapter);

        return rootView;
    }
}
