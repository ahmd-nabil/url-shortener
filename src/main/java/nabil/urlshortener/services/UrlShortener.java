package nabil.urlshortener.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nabil.urlshortener.domain.URL;
import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.repositories.UrlRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UrlShortener {

    private final UrlRepository urlRepository;
    private final BijectiveFunction bijectiveFunction;

    public URLDTO shorten(String longUrl) {
        // check if longUrl does not exist in db
        Optional<URL> foundUrl = this.urlRepository.findByLongUrl(longUrl);
        if(foundUrl.isPresent()) {
            return new URLDTO(foundUrl.get());
        }
        // save shortUrl in db
        URL savedUrl = this.urlRepository.save(new URL(longUrl));
        // generate shortUrl from id
        String shortUrl = bijectiveFunction.encode(savedUrl.getId());
        savedUrl.setShortUrl(shortUrl);
        // return URL object
        return new URLDTO(savedUrl);
    }

    public String expand(String shortUrl) {
        return "longUrl";
    }
}
