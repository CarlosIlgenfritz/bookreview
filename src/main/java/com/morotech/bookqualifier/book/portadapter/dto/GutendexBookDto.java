package com.morotech.bookqualifier.book.portadapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GutendexBookDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8590886987983311940L;

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("authors")
    public List<GutendexAuthorsDto> authors;
    @JsonProperty("languages")
    public List<String> languages;
    @JsonProperty("download_count")
    public Integer download_count;
}
