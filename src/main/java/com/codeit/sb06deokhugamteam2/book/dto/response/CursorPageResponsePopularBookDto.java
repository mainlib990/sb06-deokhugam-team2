package com.codeit.sb06deokhugamteam2.book.dto.response;

import com.codeit.sb06deokhugamteam2.book.dto.data.PopularBookDto;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CursorPageResponsePopularBookDto(
        List<PopularBookDto> content,
        String nextCursor,
        Instant nextAfter,
        Integer size,
        Long totalElements,
        boolean hasNext
) {
}
