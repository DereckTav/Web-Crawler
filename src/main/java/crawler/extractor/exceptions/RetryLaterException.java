package crawler.extractor.exceptions;

public class RetryLaterException extends Exception {
    public RetryLaterException(String m) {
        super(m);
    }
}
