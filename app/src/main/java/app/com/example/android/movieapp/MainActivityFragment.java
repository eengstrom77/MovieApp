package app.com.example.android.movieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    //Constructor
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //movieAdapter = new MovieItemAdapter(getActivity(), Arrays.asList(movieItems));
        movieAdapter = new MovieItemAdapter(getActivity(), new ArrayList<MovieItem>());

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, MovieItem[]>{

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String MOVIE_API_KEY = "<insert_personal_api_key_here>";

        private MovieItem[] getMovieDataFromJson(String mJsonStr, int numMovies)
            throws JSONException {

            final String DB_RESULTS = "results";
            final String DB_POSTER = "poster_path";
            final String DB_TITLE = "original_title";

            JSONObject movieJson = new JSONObject(mJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(DB_RESULTS);

            MovieItem[] movieItems = new MovieItem[numMovies];

            for(int i = 0; i <numMovies; i++){
                if(i >= movieArray.length()){
                    movieItems[i]= new MovieItem("Blank "+i, null);
                }
                else {
                    JSONObject movie = movieArray.getJSONObject(i);
                    movieItems[i] = new MovieItem(movie.getString(DB_TITLE),"http://image.tmdb.org/t/p/w342/"+movie.getString(DB_POSTER));
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
        protected MovieItem[] doInBackground(Void... params){
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            int numMovies = 20;  //Number of Movie Items to create. themoviedb.org defaults to 20 for discover API

            try {
                // need to break this up to allow different sorts and a way to easily chnage the API Key
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+MOVIE_API_KEY);

                //Create request to get moves from themoviedb.org
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
