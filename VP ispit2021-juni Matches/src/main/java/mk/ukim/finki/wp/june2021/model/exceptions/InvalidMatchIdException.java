package mk.ukim.finki.wp.june2021.model.exceptions;

public class InvalidMatchIdException extends RuntimeException {
    public InvalidMatchIdException(Long id) {
        super(String.format("No match with ID=%d found.", id));
    }
}
