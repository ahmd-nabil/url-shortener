package nabil.urlshortener.services;

import static nabil.urlshortener.services.ApplicationUtils.assertAlphaNumeric;
import static nabil.urlshortener.services.ApplicationUtils.assertLengthLessThanNine;
import static nabil.urlshortener.services.ApplicationUtils.assertNotEmpty;
import static nabil.urlshortener.services.ApplicationUtils.assertNotNull;
import static nabil.urlshortener.services.ApplicationUtils.assertValidUrl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nabil.urlshortener.domain.URL;
import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.exceptions.URLNotFoundException;
import nabil.urlshortener.repositories.UrlRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UrlShortener {

    private final UrlRepository urlRepository;
    private final BijectiveFunction bijectiveFunction;

    public URLDTO shorten(String longUrl) {
        assertNotNull(longUrl, "URL cannot be null");
        assertNotEmpty(longUrl, "URL cannot be empty");
        assertValidUrl(longUrl, "URL is invalid");
        // if longUrl exist in db, return it (no duplicates allowed)
        Optional<URL> foundUrl = this.urlRepository.findByLongUrl(longUrl);
        if(foundUrl.isPresent()) {
            return new URLDTO(foundUrl.get());
        }
        URL savedUrl = this.urlRepository.save(new URL(longUrl));
        // generate shortUrl from id
        String shortUrl = bijectiveFunction.encode(savedUrl.getId());
        savedUrl.setShortUrl(shortUrl);
        // return URL object
        return new URLDTO(savedUrl);
    }

    public String expand(String shortUrl) {
        assertNotNull(shortUrl, "short URL cannot be null.");
        assertNotEmpty(shortUrl, "short URL cannot be empty.");
        assertLengthLessThanNine(shortUrl, "short URL is too long.");
        assertAlphaNumeric(shortUrl, "short URL contains invalid characters.");

        Long urlId = bijectiveFunction.decode(shortUrl);
        URL url = this.urlRepository.findById(urlId).orElseThrow(URLNotFoundException::new);
        return url.getLongUrl();
    }
}
