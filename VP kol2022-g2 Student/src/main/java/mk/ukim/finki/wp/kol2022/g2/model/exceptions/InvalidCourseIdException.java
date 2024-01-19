package mk.ukim.finki.wp.kol2022.g2.model.exceptions;

public class InvalidCourseIdException extends RuntimeException {
    public InvalidCourseIdException(Long id) {
        super(String.format("No course with ID=%d found", id));
    }
}
