package nabil.urlshortener.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        assertNotEmpty(longUrl);
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
        assertNotEmpty(shortUrl);
        assertLengthLessThanNine(shortUrl);
        Long urlId = bijectiveFunction.decode(shortUrl);
        URL url = this.urlRepository.findById(urlId).orElseThrow(URLNotFoundException::new);
        return url.getLongUrl();
    }

    private void assertNotEmpty(String s) {
        if(s == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        s = s.trim();
        if(!StringUtils.hasText(s)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
    }

    private void assertLengthLessThanNine(String s) {
        if(s.length() > 8) {
            throw new IllegalArgumentException("URL is too long.");
        }
    }
}
