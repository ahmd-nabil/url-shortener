package nabil.urlshortener.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nabil.urlshortener.domain.URL;

@Data
@Builder
@AllArgsConstructor
public class URLDTO {
    public String shortUrl;
    public String longUrl;

    public URLDTO(URL url) {
        this.shortUrl = url.getShortUrl();
        this.longUrl = url.getLongUrl();
    }
}
