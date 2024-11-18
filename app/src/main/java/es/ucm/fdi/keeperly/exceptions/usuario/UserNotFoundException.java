package es.ucm.fdi.keeperly.exceptions.usuario;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super(message);
    }
}
