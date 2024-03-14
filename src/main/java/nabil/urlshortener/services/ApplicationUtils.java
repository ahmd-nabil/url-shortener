package nabil.urlshortener.services;

import org.springframework.util.StringUtils;

public class ApplicationUtils {
    public static void assertNotEmpty(String s) {
        if(s == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        s = s.trim();
        if(!StringUtils.hasText(s)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
    }

    public static void assertLengthLessThanNine(String s) {
        if(s.length() > 8) {
            throw new IllegalArgumentException("URL is too long.");
        }
    }

    public static void assertAlphaNumeric(String s) {
        if(!s.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("URL contains invalid characters.");
        }
    }
}
