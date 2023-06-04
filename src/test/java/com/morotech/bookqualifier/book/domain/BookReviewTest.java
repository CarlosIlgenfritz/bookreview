package com.morotech.bookqualifier.book.domain;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.common.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookReviewTest {

    private Integer bookId;
    private Integer rating;
    private String review;
    private Faker faker;

    @BeforeEach
    void init() {
        faker = new Faker();
    }

    @Test
    void it_should_be_possible_to_create_a_book_review() {
        bookId = faker.number().numberBetween(1, 100);
        rating = faker.number().numberBetween(1, 5);
        review = faker.lorem().sentence();

        BookReview bookReview = new BookReview(bookId, rating, review);

        assertEquals(bookId, bookReview.getBookId());
        assertEquals(rating, bookReview.getRating());
        assertEquals(review, bookReview.getReview());
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_with_a_null_book_id() {
        bookId = null;
        rating = faker.number().numberBetween(1, 5);
        review = faker.lorem().sentence();

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });


        assertEquals(domainException.getMessage(), "The identifier of the book you are trying to " +
                "review cannot be empty.");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_with_a_negative_or_zero_book_id() {
        bookId = faker.number().numberBetween(-10, 0);
        rating = faker.number().numberBetween(1, 5);
        review = faker.lorem().sentence();

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });


        assertEquals(domainException.getMessage(), "The identifier of the book you are trying to " +
                "review cannot be less than or equal to zero.");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_without_a_rating() {
        bookId = faker.number().numberBetween(1, 10);
        rating = null;
        review = faker.lorem().sentence();

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });


        assertEquals(domainException.getMessage(), "The rating of the book you are trying to review cannot be empty.");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_whit_a_rating_below_zero() {
        bookId = faker.number().numberBetween(1, 10);
        rating = -1;
        review = faker.lorem().sentence();

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });


        assertEquals(domainException.getMessage(), "The rating of the book you are trying to " +
                "review cannot be less than zero or greater than five.");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_whit_a_rating_above_five() {
        bookId = faker.number().numberBetween(1, 10);
        rating = 6;
        review = faker.lorem().sentence();

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });


        assertEquals(domainException.getMessage(), "The rating of the book you are trying to " +
                "review cannot be less than zero or greater than five.");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_without_a_review() {
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(0, 5);
        review = null;

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });

        assertEquals(domainException.getMessage(), "Please enter a valid value for the review field");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_with_a_empty_review() {
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(0, 5);
        review = "";

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });

        assertEquals(domainException.getMessage(), "Please enter a valid value for the review field");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_with_a_blank_review() {
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(0, 5);
        review = " ";

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });

        assertEquals(domainException.getMessage(), "Please enter a valid value for the review field");
    }

    @Test
    void it_should_not_be_possible_to_create_a_book_review_whit_a_review_length_above_255_characters() {
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(0, 5);
        review = "THIS STRING IS 256 CHARACTERS xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

        DomainException domainException = assertThrows(DomainException.class, () -> {
            new BookReview(bookId, rating, review);
        });

        assertEquals(domainException.getMessage(), "The length of your review cannot be greater than 255 characters");
    }
}
