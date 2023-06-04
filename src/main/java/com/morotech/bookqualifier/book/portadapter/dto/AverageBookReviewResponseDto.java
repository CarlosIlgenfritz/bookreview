package com.morotech.bookqualifier.book.portadapter.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class AverageBookReviewResponseDto {
    public String responseMessage;
    public HttpStatus responseStatus;

    public List<AverageBookReviewDto> averageBookReviewDtoList;

    public AverageBookReviewResponseDto(String responseMessage, HttpStatus responseStatus) {
        this.responseMessage = responseMessage;
        this.responseStatus = responseStatus;
    }

    public AverageBookReviewResponseDto() {
    }
}
