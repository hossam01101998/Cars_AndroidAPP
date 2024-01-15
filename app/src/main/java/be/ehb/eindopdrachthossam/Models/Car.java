package be.ehb.eindopdrachthossam.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Car implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name, poster, overview;
    private Double rating;

    public Car(String title, String poster, String overview, Double rating) {
        this.name = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
    }
    // para crear coches nuevos
    protected Car(Parcel in) {
        name = in.readString();
        poster = in.readString();
        overview = in.readString();
        rating = in.readDouble();
    }
    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeDouble(rating);
    }
    // fin del parcelable

    public Car(){

    }

    public String getName() {
        return name;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


