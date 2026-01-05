package exception;

public class UnknowOperatorException extends MyException {
    public UnknowOperatorException(String message) {
        super(message);
    }
    public UnknowOperatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
