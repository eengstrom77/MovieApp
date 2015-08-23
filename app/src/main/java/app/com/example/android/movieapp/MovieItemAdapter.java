package app.com.example.android.movieapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by e194715 on 8/21/2015.
 */
public class MovieItemAdapter extends ArrayAdapter<MovieItem> {
    private static final String LOG_TAG = MovieItemAdapter.class.getSimpleName();

     /* Custom constructor (it doesn't mirror a superclass constructor).
      The context is used to inflate the layout file, and the List is the data we want
      to populate into the Grid

      @param context      The current context. Used to inflate the layout file.
      @param movieItems   A List of MovieItem objects to display as a grid*/

    public MovieItemAdapter(Activity context, List<MovieItem> movieItems){
        // Second parameter not used because its purpose is supporting a single text view
        super(context, 0, movieItems);
    }

    /* Define the view for AdapterView (Grid)
       @param position    Pposition of the AdapterView made by the requesting a view
       @param convertView Recycled view to populate.
       @param parent      Parent viewgroup to inflation.
       @return            View for the position in the AdapterView.*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MovieItem movieItem = getItem(position);

        //check for new view item - could be a recyeled one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item,
                    parent, false);
        }
        ImageView tileView = (ImageView) convertView.findViewById(R.id.movie_image);
        //tileView.setImageResource(movieItem.image);
        if( (movieItem.imageUrl != null) || (movieItem.imageUrl == ""))
        { Picasso.with(getContext())
                .load(movieItem.imageUrl)
                .into(tileView);
        }else {
            tileView.setImageResource(R.drawable.exampleposterw343);
        }


        //TextView nameView = (TextView)convertView.findViewById(R.id.movie_text);
        //nameView.setText(movieItem.name);

        return convertView;
    }


}
