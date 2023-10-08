package exceptions;

public class EmptyDateException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EmptyDateException(String message) {
        super(message);
    }

    public EmptyDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
