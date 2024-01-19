package mk.ukim.finki.wp.kol2023.g2.model.exceptions;

public class InvalidDirectorIdException extends RuntimeException {
    public InvalidDirectorIdException(Long id) {
        super(String.format("No director with Id=%d found.", id));
    }
}
