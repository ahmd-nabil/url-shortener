package nabil.urlshortener.dtos;

import lombok.Builder;
import lombok.Data;
import nabil.urlshortener.domain.URL;

@Data
@Builder
public class URLDTO {
    public String shortUrl;
    public String longUrl;

    public URLDTO(URL url) {
        this.shortUrl = url.getShortUrl();
        this.longUrl = url.getLongUrl();
    }

    public URLDTO(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
