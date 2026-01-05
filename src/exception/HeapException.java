package exception;

public class HeapException extends MyException {
    public HeapException(String message) {
        super(message);
    }
    public HeapException(String message, Throwable cause) {
        super(message, cause);
    }
}
