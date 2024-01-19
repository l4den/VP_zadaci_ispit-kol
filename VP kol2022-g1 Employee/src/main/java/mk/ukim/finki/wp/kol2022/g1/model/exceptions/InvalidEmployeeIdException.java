package mk.ukim.finki.wp.kol2022.g1.model.exceptions;

public class InvalidEmployeeIdException extends RuntimeException {
    public InvalidEmployeeIdException(Long id) {
        super(String.format("No employee with ID=%d found.", id));
    }
}
