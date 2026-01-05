package exception;

public class AlreadyDefinedException extends MyException {
    public AlreadyDefinedException(String message) {
        super(message);
    }
    public AlreadyDefinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
