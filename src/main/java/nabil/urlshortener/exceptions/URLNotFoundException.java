package nabil.urlshortener.exceptions;

public class URLNotFoundException extends RuntimeException {
    public URLNotFoundException() {
        super("Url not found.");
    }
}
