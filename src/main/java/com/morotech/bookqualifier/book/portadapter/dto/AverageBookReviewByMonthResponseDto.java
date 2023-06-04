package com.morotech.bookqualifier.book.portadapter.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class AverageBookReviewByMonthResponseDto {
    public String responseMessage;
    public HttpStatus responseStatus;

    public List<AverageBookReviewByMonthDto> averageBookReviewByMonthDtoList;

    public AverageBookReviewByMonthResponseDto(String responseMessage, HttpStatus responseStatus){
        this.responseMessage  = responseMessage;
        this.responseStatus = responseStatus;
    }

    public AverageBookReviewByMonthResponseDto() {
    }
}
