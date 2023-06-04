package com.morotech.bookqualifier.book.portadapter;

import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;

public interface GutendexApi {
    GutendexSearchResultDto getBookBasedOnName(String name);
    GutendexSearchResultDto getBookBasedOnId(Integer id);
}
