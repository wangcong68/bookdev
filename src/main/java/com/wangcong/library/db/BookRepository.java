package com.wangcong.library.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findAllByAuthor(String author);
    List<Book> findAllByIsbn(String isbn);
    List<Book> findByNameIsLike(String name);
    Book findByIsbn(String isbn);
    Book findByBookId(Long bookId);
}
