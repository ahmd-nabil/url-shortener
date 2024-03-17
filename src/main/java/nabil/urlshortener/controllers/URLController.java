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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.services.UrlService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class URLController {
    private final UrlService urlService;

    @Operation(summary = "shortens a long url")
    @Parameter(name = "longUrl", description = "original url to be shortened", required = true, example="https://www.example.com")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "",
                content = { @Content(mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "Invalid url supplied"),
        @ApiResponse(responseCode = "500", description = "no url provided")
    })
    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<URLDTO> shorten(@RequestBody String longUrl) {
        URLDTO urldto = urlService.shorten(longUrl);
        return ResponseEntity.ok(urldto);
    }


    @Operation(summary = "redirects to the original url, given the short url")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "redirects to original url"),
        @ApiResponse(responseCode = "400", description = "invalid url supplied"),
        @ApiResponse(responseCode = "500", description = "no short url provided")
    })
    @GetMapping("/{shortURL}")
    public ResponseEntity<Void> redirect(@PathVariable String shortURL) {
        String longUrl = urlService.expand(shortURL);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
