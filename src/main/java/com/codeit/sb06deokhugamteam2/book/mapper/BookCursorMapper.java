package com.codeit.sb06deokhugamteam2.book.mapper;

import com.codeit.sb06deokhugamteam2.book.dto.data.PopularBookDto;
import com.codeit.sb06deokhugamteam2.book.dto.response.CursorPageResponsePopularBookDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class BookCursorMapper {

    public CursorPageResponsePopularBookDto toCursorBookDto(List<PopularBookDto> popularBookDtoList, boolean hasNext, String nextCursor, Instant nextAfter) {

        if(popularBookDtoList.isEmpty()) {
            return CursorPageResponsePopularBookDto.builder()
                    .content(popularBookDtoList)
                    .nextCursor(null)
                    .nextAfter(null)
                    .size(0)
                    .totalElements(null)
                    .hasNext(false)
                    .build();
        }

        return CursorPageResponsePopularBookDto.builder()
                .content(popularBookDtoList)
                .nextCursor(nextCursor)
                .nextAfter(nextAfter)
                .size(popularBookDtoList.size())
                .totalElements(null)       // 커서 페이지네이션에서는 totalElements 필요 x, 응답에 포함되긴 함.
                .hasNext(hasNext)
                .build();
    }
}
