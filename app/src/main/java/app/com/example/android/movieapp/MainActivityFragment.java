package app.com.example.android.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * Main activty fragment to display grid of movie posters
 */
public class MainActivityFragment extends Fragment {

    private MovieItemAdapter movieAdapter;
    private ArrayList<MovieItem> movieList;

    //Constructor
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<MovieItem>(new ArrayList<MovieItem>());
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //movieAdapter = new MovieItemAdapter(getActivity(), Arrays.asList(movieItems));
        //movieAdapter = new MovieItemAdapter(getActivity(), new ArrayList<MovieItem>());
        movieAdapter = new MovieItemAdapter(getActivity(),movieList);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                MovieItem movieSelected =  movieAdapter.getItem(position);

                //Toast.makeText(getActivity(), movieSelected.name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("MovieItem", movieSelected);
                intent.putExtras(mBundle);
                //.putExtra("MovieItem", movieSelected);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_option = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        moviesTask.execute(sort_option);
        //moviesTask.execute("popularity.desc");
        //moviesTask.execute("vote_count.desc");
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieItem[]>{

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String MOVIE_API_KEY = "<Insert-key-here>";

        private MovieItem[] getMovieDataFromJson(String mJsonStr, int numMovies)
            throws JSONException {

            final String DB_RESULTS = "results";
            final String DB_POSTER = "poster_path";
            final String DB_TITLE = "original_title";
            final String DB_SYNOPSIS = "overview";
            final String DB_DATE = "release_date";
            final String DB_RATING = "vote_average";

            JSONObject movieJson = new JSONObject(mJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(DB_RESULTS);

            MovieItem[] movieItems = new MovieItem[numMovies];

            for(int i = 0; i <numMovies; i++){
                if(i >= movieArray.length()){
                    movieItems[i]= new MovieItem("Blank "+i, null, null, null, 0.0);
                }
                else {
                    JSONObject movie = movieArray.getJSONObject(i);
                    movieItems[i] = new MovieItem(movie.getString(DB_TITLE),
                            movie.getString(DB_POSTER),
                            movie.getString(DB_SYNOPSIS),
                            movie.getString(DB_DATE),
                            movie.getDouble(DB_RATING));
                }
            }

            //Dummy data
            /*movieItems[0] = new MovieItem("MovieA", "http://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
            movieItems[1] = new MovieItem("MovieB", "http://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
            movieItems[2] = new MovieItem("MovieC", "http://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
            movieItems[3] = new MovieItem("MovieD", "http://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
            movieItems[4] = new MovieItem("MovieE", null);*/


            return(movieItems);
        }

        @Override
        protected MovieItem[] doInBackground(String... params){
            if(params.length == 0)
            {
                //Undefined Sort - Coding error
                return null;
            }

            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            int numMovies = 20;  //Number of Movie Items to create. themoviedb.org defaults to 20 for discover API

            try {
                // need to break this up to allow different sorts and a way to easily chnage the API Key

                //"https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+MOVIE_API_KEY
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_PARAM, MOVIE_API_KEY).build();
                URL url = new URL(builtUri.toString());

                //Create request to get movies from themoviedb.org
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length()==0) {
                    //if buffer is empty do not parse
                    return null;
                }

                movieJsonStr = buffer.toString();

            } catch(IOException e){
                // if unable to get movie data do not parse it
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            // Try JSON Processing to create array to pass to adapter
            try{
                return getMovieDataFromJson(movieJsonStr, numMovies);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // return null on any error of the above attempts to get or process data
            return null;
        }

        @Override
        protected void onPostExecute(MovieItem[] results){
            if(results != null) {
                movieAdapter.clear();
                for(MovieItem mi: results) {
                    movieAdapter.add(mi);
                }
            }
        }
    }
}
