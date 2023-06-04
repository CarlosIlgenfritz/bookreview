package com.morotech.bookqualifier.book.domain.builder;

import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;

import java.util.Collections;
import java.util.List;

public class GutendexSearchResultDtoBuilder {
    public GutendexSearchResultDto create() {
        GutendexSearchResultDto gutendexSearchResultDto = new GutendexSearchResultDto();
        gutendexSearchResultDto.books = List.of(new GutendexBookDtoBuilder().create());
        return gutendexSearchResultDto;
    }

    public GutendexSearchResultDto createWithCustomTitle(String title) {
        GutendexSearchResultDto gutendexSearchResultDto = new GutendexSearchResultDto();
        gutendexSearchResultDto.books = List.of(new GutendexBookDtoBuilder().setTitle(title).create());
        return gutendexSearchResultDto;
    }

    public GutendexSearchResultDto createWithBooksNull() {
        GutendexSearchResultDto gutendexSearchResultDto = new GutendexSearchResultDto();
        gutendexSearchResultDto.books = null;
        return gutendexSearchResultDto;
    }

    public GutendexSearchResultDto createWithBooksEmpty() {
        GutendexSearchResultDto gutendexSearchResultDto = new GutendexSearchResultDto();
        gutendexSearchResultDto.books = Collections.emptyList();
        return gutendexSearchResultDto;
    }
}
