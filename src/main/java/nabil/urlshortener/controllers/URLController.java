package nabil.urlshortener.controllers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.services.UrlService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class URLController {
    private final UrlService urlService;

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<URLDTO> shorten(@RequestBody String longUrl) {
        URLDTO urldto = urlService.shorten(longUrl);
        return ResponseEntity.ok(urldto);
    }


    @GetMapping("/{shortURL}")
    public ResponseEntity<Void> redirect(@PathVariable String shortURL) {
        String longUrl = urlService.expand(shortURL);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
