package app.com.example.android.movieapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Adapted from Udacity Android Developer Project Sunshine-Version-2 from Github
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            MovieItem movieSelected = new MovieItem();
            movieSelected = intent.getParcelableExtra("MovieItem");

            TextView nameView = (TextView) rootView.findViewById(R.id.detail_name);
            nameView.setText(movieSelected.name+", "+movieSelected.date);

            TextView synView =  (TextView) rootView.findViewById(R.id.detail_synopsis);
            synView.setText(movieSelected.synopsis);

            TextView ratingView = (TextView) rootView.findViewById(R.id.detail_rating);
            ratingView.setText("Rating: "+movieSelected.rating+" out of 10");

            ImageView posterView = (ImageView)rootView.findViewById(R.id.detail_poster);

            if( (movieSelected.imageUrl != null) || (movieSelected.imageUrl == ""))
            { Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w342/" + movieSelected.imageUrl)
                    .into(posterView);
            }else {
                posterView.setImageResource(R.drawable.exampleposterw343);
            }



        }

        return rootView;
    }
}
