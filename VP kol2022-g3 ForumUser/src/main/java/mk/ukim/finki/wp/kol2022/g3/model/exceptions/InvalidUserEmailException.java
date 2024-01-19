package mk.ukim.finki.wp.kol2022.g3.model.exceptions;

public class InvalidUserEmailException extends RuntimeException{
    public InvalidUserEmailException(String email) {
        super(String.format("No Forum User with email= %s", email));
    }
}
