package com.morotech.bookqualifier.book.portadapter.dto;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class DetailedBookDataDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3632607305369565834L;
    public Integer id;

    public String title;

    public List<GutendexAuthorsDto> authors;

    public List<String> languages;

    public Integer download_count;
    public Double rating;
    public List<String> review;

    public String responseMessage;
    public HttpStatus httpStatus;
    public DetailedBookDataDto(Integer id, String title, List<GutendexAuthorsDto> authors, List<String> languages,
                               Integer download_count, Double rating, List<String> review) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.languages = languages;
        this.download_count = download_count;
        this.rating = rating;
        this.review = review;
    }

    public DetailedBookDataDto(String responseMessage, HttpStatus  httpStatus) {
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

    public DetailedBookDataDto() {}
}
