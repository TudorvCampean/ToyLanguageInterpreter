package exception;

public class RepoException extends MyException {
    public RepoException(String message) {
        super(message);
    }
    public RepoException(String message, Throwable cause) {
        super(message, cause);
    }
}
