package nabil.urlshortener.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nabil.urlshortener.domain.URL;

public interface UrlRepository extends JpaRepository<URL, Long> {
    Optional<URL> findByLongUrl(String longUrl);
}
