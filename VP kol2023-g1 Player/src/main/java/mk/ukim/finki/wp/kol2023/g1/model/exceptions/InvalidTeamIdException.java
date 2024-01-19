package mk.ukim.finki.wp.kol2023.g1.model.exceptions;

public class InvalidTeamIdException extends RuntimeException {
    public InvalidTeamIdException(Long id) {
        super(String.format("No team with Id= %d found.", id));
    }
}
