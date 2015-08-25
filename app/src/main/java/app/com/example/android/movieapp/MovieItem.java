package app.com.example.android.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e194715 on 8/20/2015.
 *  * Adapted from Udacity android-custom-arrayadapter project from git hub
 */

public class MovieItem implements Parcelable {
    String name;     // title
    String imageUrl; // URL to Movie Poster
    String synopsis; // overview
    String date;     // release date
    double rating;    // vote average

    public MovieItem()
    {
        this.name = "unkown";
        this.imageUrl = null;
        this.synopsis = "unkown synopsis";
        this.date ="??/??/????";
        this.rating = 0;
    }

    public MovieItem(String name, String imageUrl, String synopsis, String date, double rating)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.synopsis = synopsis;
        this.date = date;
        this.rating = rating;
    }

    private MovieItem(Parcel in){
        name = in.readString();
        imageUrl = in.readString();
        synopsis = in.readString();
        date = in.readString();
        rating = in.readDouble();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String toString() {return name + "--"+imageUrl+"--"+synopsis+"--"+date+"--"+rating;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(synopsis);
        parcel.writeString(date);
        parcel.writeDouble(rating);
    }

    public static final Parcelable.Creator<MovieItem>  CREATOR = new Parcelable.Creator<MovieItem>(){

        @Override
        public MovieItem createFromParcel(Parcel parcel){
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i){
            return new MovieItem[i];
        }

    };
}

