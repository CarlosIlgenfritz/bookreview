package com.morotech.bookqualifier.book.domain.builder;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexAuthorsDto;

public class GutendexAuthorsDtoBuilder {

    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private Faker faker;

    public GutendexAuthorsDtoBuilder() {
        faker = new Faker();
        name = faker.book().author();
        birthYear = faker.number().numberBetween(1500, 2023);
        deathYear = faker.number().numberBetween(1500, 2023);
    }

    public GutendexAuthorsDto create() {
        GutendexAuthorsDto gutendexAuthorsDto = new GutendexAuthorsDto();

        gutendexAuthorsDto.name = name;
        gutendexAuthorsDto.birthYear = birthYear;
        gutendexAuthorsDto.deathYear = deathYear;

        return gutendexAuthorsDto;

    }
}
