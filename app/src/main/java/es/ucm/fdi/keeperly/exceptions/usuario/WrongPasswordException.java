package es.ucm.fdi.keeperly.exceptions.usuario;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
