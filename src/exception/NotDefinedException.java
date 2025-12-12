package exception;

public class NotDefinedException extends MyException {
    public NotDefinedException(String message) {
        super(message);
    }

    public NotDefinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
