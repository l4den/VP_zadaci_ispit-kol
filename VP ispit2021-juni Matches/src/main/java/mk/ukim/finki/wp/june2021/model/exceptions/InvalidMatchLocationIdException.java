package mk.ukim.finki.wp.june2021.model.exceptions;

public class InvalidMatchLocationIdException extends RuntimeException {
    public InvalidMatchLocationIdException(Long id) {
        super(String.format("No match location with ID=%d found.", id));
    }
}
