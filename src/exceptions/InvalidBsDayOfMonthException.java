package exceptions;

/**
 * @author bbaniya
 */
public class InvalidBsDayOfMonthException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidBsDayOfMonthException(String message) {
        super(message);
    }

    public InvalidBsDayOfMonthException(String message, Throwable cause) {
        super(message, cause);
    }
}
