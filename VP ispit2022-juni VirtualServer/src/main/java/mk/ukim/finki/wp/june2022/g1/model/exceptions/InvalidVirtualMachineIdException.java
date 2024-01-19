package mk.ukim.finki.wp.june2022.g1.model.exceptions;

public class InvalidVirtualMachineIdException extends RuntimeException {
    // dodadov construktor za spravuvanje so exceptions
    public InvalidVirtualMachineIdException(Long id) {
        super(String.format("No virtual machine with ID=%d found.", id));
    }
}
