package com.codeit.sb06deokhugamteam2.book.repository;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.entity.QBook;
import com.codeit.sb06deokhugamteam2.book.entity.QBookStats;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QBook book = QBook.book;
    private final QBookStats bookStats = QBookStats.bookStats;

    @Override
    public Slice<Book> findBooks(String keyword, String orderBy, String direction, String cursor, Instant nextAfter, int limit) {
        OrderSpecifier<?> primarySort = getPrimarySort(orderBy, direction);
        OrderSpecifier<?> secondarySort =
                direction.equalsIgnoreCase("ASC") ? book.createdAt.asc() : book.createdAt.desc();

        List<Book> books = queryFactory.selectFrom(book)
                .innerJoin(book.bookStats, bookStats).fetchJoin()
                .where(keywordContains(keyword),
                        getCursorCondition(cursor, orderBy, direction, nextAfter))
                .orderBy(primarySort, secondarySort)
                .limit(limit + 1)
                .fetch();

        boolean hasNext = books.size() > limit;
        if (hasNext) {
            books.remove(books.size() - 1);
        }
        Slice<Book> bookSlice = new SliceImpl<Book>(books, Pageable.unpaged(), hasNext);

        return bookSlice;
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null) {
            return null;
        }
        return book.title.containsIgnoreCase(keyword)
                .or(book.author.containsIgnoreCase(keyword))
                .or(book.isbn.contains(keyword));
    }

    private OrderSpecifier<?> getPrimarySort(String orderBy, String direction) {
        return switch (orderBy) {
            case "publishedDate" ->
                    direction.equalsIgnoreCase("ASC") ? book.publishedDate.asc() : book.publishedDate.desc();
            case "rating" -> {
                NumberExpression<Double> ratingSum = bookStats.ratingSum.castToNum(Double.class);
                NumberExpression<Double> rating = new CaseBuilder()
                        .when(ratingSum.loe(0.0).or(bookStats.reviewCount.loe(0)))
                        .then(0.0)
                        .otherwise(ratingSum.divide(bookStats.reviewCount));
                yield direction.equalsIgnoreCase("ASC") ? rating.asc() : rating.desc();
            }
            case "reviewCount" ->
                    direction.equalsIgnoreCase("ASC") ? bookStats.reviewCount.asc() : bookStats.reviewCount.desc();
            default -> direction.equalsIgnoreCase("ASC") ? book.title.asc() : book.title.desc();
        };
    }

    private BooleanExpression getCursorCondition(String cursor, String orderBy, String direction, Instant nextAfter) {
        if (cursor == null || nextAfter == null) {
            return null;
        }

        return switch (orderBy) {
            case "rating" -> {
                double mainCursor = Double.parseDouble(cursor);
                NumberExpression<Double> ratingSum = bookStats.ratingSum.castToNum(Double.class);
                NumberExpression<Double> rating = new CaseBuilder()
                        .when(ratingSum.loe(0.0).or(bookStats.reviewCount.loe(0)))
                        .then(0.0)
                        .otherwise(ratingSum.divide(bookStats.reviewCount));
                yield direction.equalsIgnoreCase("ASC") ?
                        rating.gt(mainCursor)
                                .or(rating.eq(mainCursor).and(book.createdAt.after(nextAfter))) :
                        rating.lt(mainCursor)
                                .or(rating.eq(mainCursor).and(book.createdAt.before(nextAfter)));
            }
            case "publishedDate" -> {
                LocalDate mainCursor = LocalDate.parse(cursor);
                yield direction.equalsIgnoreCase("ASC") ?
                        book.publishedDate.gt(mainCursor)
                                .or(book.publishedDate.eq(mainCursor).and(book.createdAt.after(nextAfter))) :
                        book.publishedDate.lt(mainCursor)
                                .or(book.publishedDate.eq(mainCursor).and(book.createdAt.before(nextAfter)));
            }
            case "reviewCount" -> {
                int mainCursor = Integer.parseInt(cursor);

                yield direction.equalsIgnoreCase("ASC") ?
                        bookStats.reviewCount.gt(mainCursor)
                                .or(bookStats.reviewCount.eq(mainCursor).and(book.createdAt.after(nextAfter))) :
                        bookStats.reviewCount.lt(mainCursor)
                                .or(bookStats.reviewCount.eq(mainCursor).and(book.createdAt.before(nextAfter)));
            }
            default -> direction.equalsIgnoreCase("ASC") ?
                    book.title.gt(cursor)
                            .or(book.title.eq(cursor).and(book.createdAt.after(nextAfter))) :
                    book.title.loe(cursor)
                            .or(book.title.eq(cursor).and(book.createdAt.before(nextAfter)));
        };
    }
}
