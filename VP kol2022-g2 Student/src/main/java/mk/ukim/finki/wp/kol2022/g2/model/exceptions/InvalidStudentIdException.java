package mk.ukim.finki.wp.kol2022.g2.model.exceptions;

public class InvalidStudentIdException extends RuntimeException {
    public InvalidStudentIdException(Long id) {
        super(String.format("No student with ID=%d found", id));
    }
}
