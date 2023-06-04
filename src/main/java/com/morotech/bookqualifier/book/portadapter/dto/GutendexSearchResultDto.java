package com.morotech.bookqualifier.book.portadapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class GutendexSearchResultDto implements Serializable {
    @JsonProperty("results")
    public List<GutendexBookDto> books;

    public GutendexSearchResultDto() {
    }
}
