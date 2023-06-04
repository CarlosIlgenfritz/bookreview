package com.morotech.bookqualifier.book.application;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.builder.GutendexSearchResultDtoBuilder;
import com.morotech.bookqualifier.book.portadapter.GutendexHttpClient;
import com.morotech.bookqualifier.book.portadapter.dto.SearchResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchBookByNameTest {

    private Faker faker;
    private GutendexHttpClient gutendexHttpClient;

    @BeforeEach
    void init() {
        faker = new Faker();
        gutendexHttpClient = mock(GutendexHttpClient.class);
    }

    @Test
    void it_should_return_a_book_when_search_by_name() {
        String bookName = faker.book().title();
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);
        when(gutendexHttpClient.getBookBasedOnName(bookName))
                .thenReturn(new GutendexSearchResultDtoBuilder().createWithCustomTitle(bookName));

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(bookName,  response.books.get(0).title);
    }

    @Test
    void it_should_return_http_status_200_when_everything_is_ok() {
        String bookName = faker.book().title();
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);
        when(gutendexHttpClient.getBookBasedOnName(bookName))
                .thenReturn(new GutendexSearchResultDtoBuilder().create());

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.OK,  response.httpStatus);
    }

    @Test
    void it_should_return_http_status_400_when_name_is_empty() {
        String bookName = "";
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.BAD_REQUEST,  response.httpStatus);
        assertEquals("Name cannot be empty or blank",  response.responseMessage);
    }

    @Test
    void it_should_return_http_status_400_when_name_is_blank() {
        String bookName = " ";
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.BAD_REQUEST,  response.httpStatus);
        assertEquals("Name cannot be empty or blank",  response.responseMessage);
    }

    @Test
    void it_should_return_404_when_book_is_null() {
        String bookName = faker.book().title();
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);
        when(gutendexHttpClient.getBookBasedOnName(bookName))
                .thenReturn(new GutendexSearchResultDtoBuilder().createWithBooksNull());

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.NOT_FOUND,  response.httpStatus);
        assertEquals("No book found with the name " + bookName
                + ". Please add a valid name",  response.responseMessage);
    }

    @Test
    void it_should_return_404_when_book_is_not_found() {
        String bookName = faker.book().title();
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);
        when(gutendexHttpClient.getBookBasedOnName(bookName))
                .thenReturn(new GutendexSearchResultDtoBuilder().createWithBooksEmpty());

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.NOT_FOUND,  response.httpStatus);
        assertEquals("No book found with the name " + bookName
                + ". Please add a valid name",  response.responseMessage);
    }

    @Test
    void it_should_return_http_status_500_when_something_went_wrong() {
        String bookName = faker.book().title();
        SearchBookByName searchBookByName = new SearchBookByName(gutendexHttpClient);
        when(gutendexHttpClient.getBookBasedOnName(bookName))
                .thenThrow(RestClientException.class);

        SearchResultDto response =
                searchBookByName.searchBookByName(bookName);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,  response.httpStatus);
        assertEquals("An unforeseen error occurred, please try again later. If the error persists," +
                " please contact support.", response.responseMessage);
    }
}
