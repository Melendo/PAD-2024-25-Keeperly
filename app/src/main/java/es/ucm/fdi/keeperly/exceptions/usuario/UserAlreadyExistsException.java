package es.ucm.fdi.keeperly.exceptions.usuario;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
