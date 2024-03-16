package nabil.urlshortener.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nabil.urlshortener.configs.RedisConfig;
import nabil.urlshortener.domain.URL;
import nabil.urlshortener.repositories.UrlRepository;

@Import({ RedisConfig.class, UrlService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
  CacheAutoConfiguration.class,
  RedisAutoConfiguration.class
})
public class UrlServiceTestIT {
    @MockBean
    UrlRepository urlRepository;
    @MockBean
    BijectiveFunction bijectiveFunction;

    @Autowired
    UrlService urlService;

    @Autowired
    private CacheManager cacheManager;
    @Test
    void test_when_expand_first_time_hit_db_then_cache() {
        URL url = new URL("https://www.example.com");
        url.setId(1L);
        url.setShortUrl("abc123");
        given(urlRepository.findById(1L)).willReturn(Optional.of(url));
        given(bijectiveFunction.decode("abc123")).willReturn(1L);

        String longUrlCacheMiss = urlService.expand("abc123");
        String longUrlCacheHit = urlService.expand("abc123");

        assertEquals(longUrlCacheMiss, url.getLongUrl());
        assertEquals(longUrlCacheHit, url.getLongUrl());

        // db call happens 1 time, second time goes to cache
        verify(urlRepository, times(1)).findById(any());
    }
}
