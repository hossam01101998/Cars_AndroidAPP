package be.ehb.eindopdrachthossam.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Car {

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


