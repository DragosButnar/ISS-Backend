package a2.A2.exceptions;

public class MovieLogicException extends RuntimeException {

    public MovieLogicException(String title) {
        super("Movie logic is incorrect! (" + title + ")" );
    }
}