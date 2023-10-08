package exceptions;

/**
 * @author bbaniya
 */
public class DateRangeNotSupported extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DateRangeNotSupported(String message) {
        super(message);
    }

    public DateRangeNotSupported(String message, Throwable cause) {
        super(message, cause);
    }

}
