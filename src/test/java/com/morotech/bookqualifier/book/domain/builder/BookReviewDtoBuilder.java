package com.morotech.bookqualifier.book.domain.builder;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;

public class BookReviewDtoBuilder {

    private Integer bookId;
    private Integer rating;
    private String review;
    private Faker faker;

    public BookReviewDtoBuilder() {
        faker = new Faker();
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(1, 5);
        review = faker.lorem().sentence();
    }

    public BookReviewDto create() {
        return new BookReviewDto(bookId, rating, review);
    }

    public BookReviewDtoBuilder setBookId(Integer bookId) {
        this.bookId = bookId;
        return this;
    }

    public BookReviewDtoBuilder setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

}
