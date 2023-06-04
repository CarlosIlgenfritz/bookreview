package com.morotech.bookqualifier.book.domain.builder;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.portadapter.dto.DetailedBookDataDto;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexAuthorsDto;

import java.util.List;

public class DetailedBookDataDtoBuilder {
    private Integer id;

    private String title;

    private List<GutendexAuthorsDto> authors;

    private List<String> languages;

    private Integer download_count;
    private Double rating;
    private List<String> review;
    private Faker faker;

    public DetailedBookDataDtoBuilder() {
        faker = new Faker();
        this.id = faker.number().numberBetween(1, 100);
        this.title = faker.book().title();
        this.authors = List.of(new GutendexAuthorsDtoBuilder().create());
        this.languages = List.of(faker.programmingLanguage().name());
        this.download_count = faker.number().numberBetween(1, 100);
        this.rating = faker.number().randomDouble(2, 0, 5);
        this.review = List.of(faker.lorem().sentence());
    }

    public DetailedBookDataDto create() {
        return new DetailedBookDataDto(id, title, authors, languages, download_count, rating, review);
    }
}
