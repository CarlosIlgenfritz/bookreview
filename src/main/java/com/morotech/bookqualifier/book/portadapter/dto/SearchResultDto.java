package com.morotech.bookqualifier.book.portadapter.dto;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SearchResultDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7074782959297470046L;

    public String responseMessage;
    public HttpStatus httpStatus;
    public List<GutendexBookDto> books;

    public SearchResultDto(String responseMessage, HttpStatus httpStatus) {
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

    public SearchResultDto() {
    }
}
