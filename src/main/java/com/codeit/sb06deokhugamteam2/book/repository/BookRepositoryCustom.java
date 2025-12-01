package com.codeit.sb06deokhugamteam2.book.repository;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface BookRepositoryCustom {
    Slice<Book> findBooks(String keyword, String orderBy, String direction, String cursor, Instant nextAfter, int limit);
}
