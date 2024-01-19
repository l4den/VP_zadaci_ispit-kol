package mk.ukim.finki.wp.kol2022.g1.model.exceptions;

public class InvalidEmployeeEmailException extends RuntimeException{
    public InvalidEmployeeEmailException(String email) {
        super(String.format("No employee with email=%s found.", email));
    }
}
