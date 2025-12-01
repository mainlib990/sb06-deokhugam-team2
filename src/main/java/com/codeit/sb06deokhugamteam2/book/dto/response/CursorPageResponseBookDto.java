package com.codeit.sb06deokhugamteam2.book.dto.response;

import com.codeit.sb06deokhugamteam2.book.dto.data.BookDto;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class CursorPageResponseBookDto {
    private List<BookDto> content;
    private String nextCursor;
    private Instant nextAfter;
    private int size;
    private long totalElements;
    private boolean hasNext;
}
