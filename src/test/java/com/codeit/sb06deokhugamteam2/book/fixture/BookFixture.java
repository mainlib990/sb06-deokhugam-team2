package com.codeit.sb06deokhugamteam2.book.fixture;

import com.codeit.sb06deokhugamteam2.book.entity.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class BookFixture {
    public static Book createBook(int count) {
        return Book.builder()
                .thumbnailUrl("thumbnail" + count + "url")
                .title("title" + count)
                .description("description" + count)
                .publisher("publisher" + count)
                .isbn(String.format("%013d", count))
                .publishedDate(LocalDate.now())
                .author("author" + count)
                .build();
    }

    public static List<Book> createBooks(int number) {
        return IntStream.rangeClosed(1, number)
                .mapToObj(BookFixture::createBook)
                .toList();
    }
}
