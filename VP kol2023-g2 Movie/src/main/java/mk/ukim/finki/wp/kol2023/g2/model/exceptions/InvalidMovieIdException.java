package mk.ukim.finki.wp.kol2023.g2.model.exceptions;

public class InvalidMovieIdException extends RuntimeException {
    public InvalidMovieIdException(Long id) {
        super(String.format("No movie with Id=%d found.", id));
    }
}
