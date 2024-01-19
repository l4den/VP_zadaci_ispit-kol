package mk.ukim.finki.wp.kol2022.g3.model.exceptions;

public class InvalidForumUserIdException extends RuntimeException {
    public InvalidForumUserIdException(Long id) {
        super(String.format("No Forum User with ID= %d found.", id));
    }
}
