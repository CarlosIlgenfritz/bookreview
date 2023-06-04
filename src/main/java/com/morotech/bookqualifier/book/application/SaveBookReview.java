package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.domain.BookReview;
import com.morotech.bookqualifier.book.domain.common.DomainException;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SaveBookReview {

    private BookReviewRepository bookReviewRepository;

    @Autowired
    public SaveBookReview(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public ResponseEntity<String> save(BookReviewDto bookReviewDto) {
        try {
            BookReview bookReview = new BookReview(bookReviewDto.bookId, bookReviewDto.rating, bookReviewDto.review);

            bookReviewRepository.save(bookReview);

            return new ResponseEntity<>("Book Review saved!", HttpStatus.OK);
        }catch (DomainException domainException){
            return new ResponseEntity<>(domainException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unforeseen error occurred, please try again later. If the error persists," +
                    "please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
