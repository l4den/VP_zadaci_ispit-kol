package mk.ukim.finki.wp.kol2022.g3.model.exceptions;

public class InvalidInterestIdException extends RuntimeException {
    public InvalidInterestIdException(Long id) {
        super(String.format("No Interest with Id=%d found.", id));
    }
}
