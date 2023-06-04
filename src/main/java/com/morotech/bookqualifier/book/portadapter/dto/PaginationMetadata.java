package com.morotech.bookqualifier.book.portadapter.dto;

import java.io.Serial;
import java.io.Serializable;

public class PaginationMetadata  implements Serializable {
    @Serial
    private static final long serialVersionUID = -7450796135435196659L;
    public int currentPage;
    public int totalPages;
    public long totalItems;
}
