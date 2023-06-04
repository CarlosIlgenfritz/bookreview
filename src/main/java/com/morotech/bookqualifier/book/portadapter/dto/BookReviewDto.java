package com.morotech.bookqualifier.book.portadapter.dto;

public class BookReviewDto {
    public Integer bookId;
    public Integer rating;
    public String review;

    public BookReviewDto(Integer bookId, Integer rating, String review) {
        this.bookId = bookId;
        this.rating = rating;
        this.review = review;
    }
}
