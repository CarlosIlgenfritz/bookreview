package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.portadapter.GutendexApi;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GutendexHttpClient implements GutendexApi {

    private final RestTemplate restTemplate;
    @Value("${gutendex.baseurl}")
    private String baseurl;

    @Autowired
    public GutendexHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GutendexSearchResultDto getBookBasedOnName(String name) {
        String url = baseurl + "?search=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        return restTemplate.getForObject(url, GutendexSearchResultDto.class);
    }

    @Override
    public GutendexSearchResultDto getBookBasedOnId(Integer id) {
        String url = baseurl + "?ids=" + id;
        return restTemplate.getForObject(url, GutendexSearchResultDto.class);
    }
}
