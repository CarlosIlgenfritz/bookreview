package com.morotech.bookqualifier.book.portadapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class GutendexAuthorsDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1252293257504079346L;

    @JsonProperty("name")
    public String name;
    @JsonProperty("bith_year")
    public Integer birthYear;
    @JsonProperty("death_year")
    public Integer deathYear;
}
