package nabil.urlshortener.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import nabil.urlshortener.dtos.URLDTO;
import nabil.urlshortener.exceptions.URLNotFoundException;
import nabil.urlshortener.services.UrlShortener;

@WebMvcTest(URLController.class)
@ExtendWith(MockitoExtension.class)
class URLControllerTest {
    @Value("${application.host}")
    public String HOST;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UrlShortener urlShortener;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void test_shorten_success() throws Exception {
        String longUrl = "longUrl";
        String shortUrl = "shortUrl";
        BDDMockito.when(urlShortener.shorten(longUrl)).thenReturn(new URLDTO(longUrl, shortUrl));

        mockMvc.perform(post("/").contentType("text/plain").content(longUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(HOST + shortUrl));
    }

    @Test
    public void test_redirect_success() throws Exception {
        String shortUrl = "shortUrl";
        String longUrl = "longUrl";
        BDDMockito.when(urlShortener.expand(shortUrl)).thenReturn(longUrl);

        mockMvc.perform(get("/{shortURL}", shortUrl))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", longUrl));
    }

    @Test
    public void test_redirect_returns_404_when_shortUrl_not_found() throws Exception {
        String shortUrl = "brandNew";
        BDDMockito.when(urlShortener.expand(shortUrl)).thenThrow(URLNotFoundException.class);

        mockMvc.perform(get("/{shortURL}", shortUrl))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shorten_empty_url_returns_400_bad_request() throws Exception {
        String longUrl = "";

        mockMvc.perform(post("/").contentType("text/plain").content(longUrl))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_shorten_invalid_url() throws Exception {
        String longUrl = "invalidUrl";
        BDDMockito.when(urlShortener.shorten(longUrl)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/").contentType("text/plain").content(longUrl))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_expand_invalid_short_url() throws Exception {
        String shortUrl = "tooLongShortURL";
        BDDMockito.when(urlShortener.expand(shortUrl)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/{shortUrl}", shortUrl))
                .andExpect(status().isBadRequest());
    }

    // Should return a 404 error when given a valid shortened URL that does not exist in the database
    @Test
    public void test_expand_valid_short_url_not_found() throws Exception {
        String shortUrl = "validShortUrl";
        BDDMockito.when(urlShortener.expand(shortUrl)).thenThrow(URLNotFoundException.class);

        mockMvc.perform(get("/{shortURL}", shortUrl))
                .andExpect(status().isNotFound());
    }

//    // Should handle concurrent requests to shorten URLs without generating duplicate shortened URLs
      // does not work yet
//    @Test
//    public void test_shorten_concurrent_requests() throws Exception {
//        String longUrl = "longUrl";
//        String shortUrl = "shortUrl";
//        BDDMockito.when(urlShortener.shorten(longUrl)).thenReturn(new URLDTO(longUrl, shortUrl));
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<Callable<ResultActions>> tasks = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            tasks.add(() -> mockMvc.perform(post("/").contentType("text/plain").content(longUrl)));
//        }
//        List<Future<ResultActions>> results = executorService.invokeAll(tasks);
//
//        for (Future<ResultActions> result : results) {
//            result.get().andExpect(status().isOk()).andExpect(jsonPath("$.shortUrl").value(HOST + shortUrl));
//        }
//    }

    @Test
    public void test_expand_concurrent_requests() throws Exception {
        String shortUrl = "shortUrl";
        String longUrl = "longUrl";
        BDDMockito.when(urlShortener.expand(shortUrl)).thenReturn(longUrl);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<ResultActions>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(() -> mockMvc.perform(get("/{shortURL}", shortUrl)));
        }
        List<Future<ResultActions>> results = executorService.invokeAll(tasks);

        for (Future<ResultActions> result : results) {
            result.get().andExpect(status().isFound()).andExpect(header().string("Location", longUrl));
        }
    }

    // Should handle URLs with query parameters correctly
    @Test
    public void test_shorten_with_query_parameters() throws Exception {
        String longUrl = "longUrl?param1=value1&param2=value2";
        String shortUrl = "shortUrl";
        BDDMockito.when(urlShortener.shorten(longUrl)).thenReturn(new URLDTO(longUrl, shortUrl));

        mockMvc.perform(post("/").contentType("text/plain").content(longUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(HOST + shortUrl));
    }
}