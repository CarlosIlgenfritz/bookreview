package com.morotech.bookqualifier.book.domain.builder;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexAuthorsDto;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexBookDto;

import java.util.List;

public class GutendexBookDtoBuilder {
    private Integer id;
    private String title;
    private List<GutendexAuthorsDto> authors;
    private List<String> languages;
    private Integer download_count;
    private Faker faker;

    public GutendexBookDtoBuilder() {
        faker = new Faker();
        id = faker.number().numberBetween(1, 1000);
        title = faker.book().title();
        authors = List.of(new GutendexAuthorsDtoBuilder().create());
        languages = List.of(faker.country().name());
        download_count = faker.number().numberBetween(1, 1000);
    }

    public GutendexBookDto create(){
        GutendexBookDto gutendexBookDto = new GutendexBookDto();

        gutendexBookDto.id = id;
        gutendexBookDto.title = title;
        gutendexBookDto.authors = authors;
        gutendexBookDto.languages = languages;
        gutendexBookDto.download_count = download_count;

        return gutendexBookDto;
    }

    public GutendexBookDtoBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
}
