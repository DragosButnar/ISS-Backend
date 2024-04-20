package a2.A2;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Getter @Setter
@Entity
public class Movie {
    @TableGenerator(
            name = "moviesSeq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="moviesSeq")
    @Id @Column(name="movieID")
    private long id;
    @Column(name="movieTitle")
    private String title;
    @Column(name="movieYear")
    private int year;
    @Column(name="movieGenre")
    private String genre;
    @Column(name="movieFranchise")
    private String franchise;

    static long lastID = 1;

    public Movie(){}

    public Movie(String title, int year, String genre, String franchise) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.franchise = franchise;

        this.id = lastID;
        lastID += 1;
    }

    public Movie(int id, String title, int year, String genre, String franchise){
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.franchise = franchise;

        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return getId() == movie.getId() && Objects.equals(getTitle(), movie.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }

    @Override
    public String toString() {
        String result = this.title + "(" + this.year + ")";

        if(this.genre != null) {
            result += " in ";
            result += this.genre;
        }
        if(this.franchise != null){
            result += " part of ";
            result += this.franchise;
        }

        return result;
    }

}

