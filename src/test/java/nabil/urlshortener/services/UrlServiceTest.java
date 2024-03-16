package nabil.urlshortener.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import nabil.urlshortener.domain.URL;
import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.exceptions.URLNotFoundException;
import nabil.urlshortener.repositories.UrlRepository;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Value("${application.host}")
    String HOST;
    @Mock
    UrlRepository urlRepository;
    @Mock
    BijectiveFunction bijectiveFunction;
    @InjectMocks
    UrlService urlService;
    @BeforeEach
    void setUp() {
    }

    @Test
    public void test_shorten_newLongUrl() {
        // Arrange
        String longUrl = "https://www.example.com";
        URL savedUrl = new URL(longUrl);
        savedUrl.setId(1L);
        savedUrl.setShortUrl("abc123");
        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(URL.class))).thenReturn(savedUrl);
        when(bijectiveFunction.encode(savedUrl.getId())).thenReturn(savedUrl.getShortUrl());

        // Act
        URLDTO result = urlService.shorten(longUrl);

        // Assert
        assertEquals(HOST + "/" + savedUrl.getShortUrl(), result.getShortUrl());
        assertEquals(savedUrl.getLongUrl(), result.getLongUrl());
    }

    @Test
    public void test_shorten_existingLongUrl() {
        // Arrange
        String longUrl = "https://www.example.com";
        URL existingUrl = new URL(longUrl);
        existingUrl.setId(1L);
        existingUrl.setShortUrl("abc123");
        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.of(existingUrl));

        // Act
        URLDTO result = urlService.shorten(longUrl);

        // Assert
        assertEquals(existingUrl.getShortUrl(), result.getShortUrl());
        assertEquals(existingUrl.getLongUrl(), result.getLongUrl());
    }

    @Test
    public void test_shorten_emptyLongUrl() {
        // Arrange
        String longUrl = "";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> urlService.shorten(longUrl));
    }

        @Test
    public void test_shorten_nullLongUrl() {
        // Arrange
        String longUrl = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> urlService.shorten(longUrl));
    }

    @Test
    public void test_shorten_invalid_url_throws_400_bad_request() {
        // Arrange
        String longUrl = "notValidURL.com";
        // Act
        assertThrows(IllegalArgumentException.class, () -> urlService.shorten(longUrl));
    }

    @Test
    public void test_expand_validShortUrl() {
        // Arrange
        String shortUrl = "abc123";
        long urlId = 1L;
        String longUrl = "https://www.example.com";
        URL url = new URL(longUrl);
        url.setId(urlId);
        url.setShortUrl(shortUrl);
        when(bijectiveFunction.decode(shortUrl)).thenReturn(urlId);
        when(urlRepository.findById(urlId)).thenReturn(Optional.of(url));

        // Act
        String result = urlService.expand(shortUrl);

        // Assert
        assertEquals(longUrl, result);
    }

    @Test
    public void test_expand_notFoundUrl() {
        // Arrange
        String shortUrl = "abc123";
        long urlId = 1L;
        when(bijectiveFunction.decode(shortUrl)).thenReturn(urlId);
        when(urlRepository.findById(urlId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(URLNotFoundException.class, () -> urlService.expand(shortUrl));
    }

    @Test
    public void test_expand_invalidShortUrl() {
        // Arrange
        String shortUrl = "abcde123456"; // too long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> urlService.expand(shortUrl));
    }

    @Test
    public void test_expand_nonAlphanumericShortUrl() {
        // Arrange
        String shortUrl = "abc!@#";
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> urlService.expand(shortUrl));
    }

    @Test
    public void test_expand_nonURLSafeShortUrl() {
        // Arrange
        String shortUrl = "abc+123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> urlService.expand(shortUrl));
    }

    @Test
    public void test_shorten_decode_then_encode() {
        // Arrange
        String longUrl = "https://www.example.com";
        URL savedUrl = new URL(longUrl);
        savedUrl.setId(1L);
        savedUrl.setShortUrl("abc123");
        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(URL.class))).thenReturn(savedUrl);
        when(bijectiveFunction.encode(savedUrl.getId())).thenReturn(savedUrl.getShortUrl());
        when(bijectiveFunction.decode(any())).thenReturn(savedUrl.getId());

        // Act
        URLDTO result = urlService.shorten(longUrl);
        long decodedId = bijectiveFunction.decode(result.getShortUrl());

        // Assert
        assertEquals(savedUrl.getId(), decodedId);
    }
}