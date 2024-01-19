package mk.ukim.finki.wp.june2022.g1.model.exceptions;

public class InvalidUserIdException extends RuntimeException {
    // dodadov construktor za spravuvanje so exceptions
    public InvalidUserIdException(Long id) {
        super(String.format("No user with ID=%d found.", id));
    }
}
