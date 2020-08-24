package net.nuttle.notes.es;

public class SearchException extends RuntimeException {
    public SearchException() {
        super();
    }
    public SearchException(String msg) {
        super(msg);
    }
    public SearchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
