package mk.ukim.finki.wp.kol2022.g2.model.exceptions;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException(String email) {
        super(String.format("No student with email=%s found", email));
    }
}
