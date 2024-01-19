package mk.ukim.finki.wp.september2021.model.exceptions;

public class InvalidNewsCategoryIdException extends RuntimeException {
    public InvalidNewsCategoryIdException(Long id) {
        super(String.format("No category with Id=%d found", id));
    }
}
