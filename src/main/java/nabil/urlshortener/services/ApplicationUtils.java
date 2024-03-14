package nabil.urlshortener.services;

import org.springframework.util.StringUtils;

public class ApplicationUtils {

    public static void assertNotNull(String s, String message) {
        if(s == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNotEmpty(String s, String message) {
        s = s.trim();
        if(!StringUtils.hasText(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertLengthLessThanNine(String s, String message) {
        if(s.length() > 8) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertAlphaNumeric(String s, String message) {
        if(!s.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException(message);
        }
    }
}
