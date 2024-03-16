package nabil.urlshortener.services;

import java.net.URL;

import org.springframework.util.StringUtils;

public class ApplicationUtils {

    public static void assertNotNull(String s, String message) {
        if(s == null) {
            throw new NullPointerException(message);
        }
    }

    public static void assertNotEmpty(String s, String message) {
        s = s.trim();
        if(!StringUtils.hasText(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Used to make sure that shortUrl length is shorter than 11 (62^11 is gonna be bigger than what Long datatype holds)
     * **/
    public static void assertLengthLessThanEleven(String s, String message) {
        if(s.length() > 10) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertAlphaNumeric(String s, String message) {
        if(!s.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertValidUrl(String s, String message) {
        try {
            new URL(s).toURI();
        } catch (Exception e) {
            throw new IllegalArgumentException(message);
        }
    }
}
