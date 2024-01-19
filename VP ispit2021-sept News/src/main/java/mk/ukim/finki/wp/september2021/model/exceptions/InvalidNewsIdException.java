package mk.ukim.finki.wp.september2021.model.exceptions;

public class InvalidNewsIdException extends RuntimeException {
    public InvalidNewsIdException(Long id) {
        super(String.format("No news with Id=%d found", id));
    }
}
