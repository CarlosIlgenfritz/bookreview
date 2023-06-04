package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.portadapter.GutendexHttpClient;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;
import com.morotech.bookqualifier.book.portadapter.dto.SearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SearchBookByName {

    private GutendexHttpClient gutendexHttpClient;

    @Autowired
    public SearchBookByName(GutendexHttpClient gutendexHttpClient) {
        this.gutendexHttpClient = gutendexHttpClient;
    }

    @Cacheable(value = "books", unless = "!#result.responseMessage.equals('Sucess finding the book!')")
    public SearchResultDto searchBookByName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            return new SearchResultDto("Name cannot be empty or blank", HttpStatus.BAD_REQUEST);
        }

        try {
            GutendexSearchResultDto bookBasedOnName = gutendexHttpClient.getBookBasedOnName(name);

            if (bookBasedOnName.books == null || bookBasedOnName.books.isEmpty()) {
                return new SearchResultDto("No book found with the name " + name
                        + ". Please add a valid name", HttpStatus.NOT_FOUND);
            }

            SearchResultDto searchResultDto = new SearchResultDto("Sucess finding the book!", HttpStatus.OK);
            searchResultDto.books = bookBasedOnName.books;

            return searchResultDto;
        } catch (Exception e) {
            e.printStackTrace();
            return new SearchResultDto("An unforeseen error occurred, please try again later. If the error persists," +
                            " please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
