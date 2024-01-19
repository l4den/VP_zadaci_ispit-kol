package mk.ukim.finki.wp.kol2023.g1.model.exceptions;

public class InvalidPlayerIdException extends RuntimeException {
    public InvalidPlayerIdException(Long id) {
        super(String.format("No player with Id= %d found.", id));
    }
}
