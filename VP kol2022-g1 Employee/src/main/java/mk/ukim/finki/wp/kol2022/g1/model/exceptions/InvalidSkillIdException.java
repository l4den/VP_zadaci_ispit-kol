package mk.ukim.finki.wp.kol2022.g1.model.exceptions;

public class InvalidSkillIdException extends RuntimeException {
    public InvalidSkillIdException(Long id) {
        super(String.format("No skill with ID=%d found.", id));
    }
}
