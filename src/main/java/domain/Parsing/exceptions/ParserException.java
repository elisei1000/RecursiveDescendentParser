package domain.Parsing.exceptions;

/**
 * Created by elisei on 16.12.2017.
 */
public class ParserException extends Exception {
    public ParserException(String message) { super(message);}
    public ParserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
