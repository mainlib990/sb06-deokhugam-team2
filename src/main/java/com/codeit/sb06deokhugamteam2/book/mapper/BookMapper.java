package com.codeit.sb06deokhugamteam2.book.mapper;

import com.codeit.sb06deokhugamteam2.book.dto.data.BookDto;
import com.codeit.sb06deokhugamteam2.book.dto.data.PopularBookDto;
import com.codeit.sb06deokhugamteam2.book.dto.response.CursorPageResponsePopularBookDto;
import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.DashBoard;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class BookMapper {
    public BookDto toDto(Book book) {
        return BookDto.builder()
                .isbn(book.getIsbn())
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .description(book.getDescription())
                .publishedDate(book.getPublishedDate())
                .reviewCount(book.getReviewCount())
                .rating(ratingOperation(book.getReviewCount(), book.getRatingSum()))
                .thumbnailUrl(book.getThumbnailUrl())
                .publisher(book.getPublisher())
                .build();
    }

    private double ratingOperation(int reviewCount, double ratingSum) {
        if (reviewCount > 0 && ratingSum > 0) {
            return ratingSum / reviewCount;
        }
        return 0.0;
    }

    public PopularBookDto toDto(DashBoard dashBoard, Book book, PeriodType period) {
        return PopularBookDto.builder()
                .id(dashBoard.getId())
                .bookId(dashBoard.getEntityId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .thumbnailUrl(book.getThumbnailUrl())
                .period(period)
                .rank(dashBoard.getRank())
                .score(dashBoard.getScore())
                .reviewCount(book.getReviewCount().longValue())
                .rating(book.getRatingSum() / book.getReviewCount())
                .createdAt(dashBoard.getCreatedAt())
                .build();
    }

    public CursorPageResponsePopularBookDto toCursorBookDto(List<PopularBookDto> popularBookDtoList, Integer limit) {

        boolean hasNext = false;
        if (popularBookDtoList.size() > limit) {
            popularBookDtoList.remove(limit);
            hasNext = true;
        }

        String nextCursor = popularBookDtoList.isEmpty() ? null : popularBookDtoList.get(popularBookDtoList.size() - 1).rank().toString();

        Instant nextAfter = popularBookDtoList.isEmpty() ? null : popularBookDtoList.get(popularBookDtoList.size() - 1).createdAt();

        return new CursorPageResponsePopularBookDto(
                popularBookDtoList,
                nextCursor,
                nextAfter,
                popularBookDtoList.size(),
                null,       // 커서 페이지네이션에서는 totalElements 필요 x, 응답에 포함되긴 함.
                hasNext
        );
    }
}
