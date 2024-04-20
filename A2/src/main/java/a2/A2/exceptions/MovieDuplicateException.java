package a2.A2.exceptions;

public class MovieDuplicateException extends RuntimeException {

    public MovieDuplicateException(String title) {
        super("Movie title already exists! (" + title + ")" );
    }
}