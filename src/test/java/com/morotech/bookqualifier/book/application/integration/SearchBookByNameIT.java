package com.morotech.bookqualifier.book.application.integration;

import com.morotech.bookqualifier.book.portadapter.BookRest;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;
import com.morotech.bookqualifier.book.portadapter.dto.SearchResultDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchBookByNameIT {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRest bookRest;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void it_should_return_200_when_search_book_by_name(){
        String url = "http://localhost:" + port + "/books/search?name=Frankenstein";
        ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(url, SearchResultDto.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(84, response.getBody().books.get(0).id);
    }

    @Test
    public void it_should_return_400_when_name_is_null(){
        String url = "http://localhost:" + port + "/books/search?name=";

        try{
            restTemplate.getForEntity(url, GutendexSearchResultDto.class);
        }catch (HttpClientErrorException e){
            assertEquals(400, e.getRawStatusCode());
        }

    }

    @Test
    public void it_should_return_404_when_book_not_found(){
        String url = "http://localhost:" + port + "/books/search?name=Frankensteinnn";

        try{
            restTemplate.getForEntity(url, GutendexSearchResultDto.class);
        }catch (HttpClientErrorException e){
            assertEquals(404, e.getRawStatusCode());
        }

    }
}
