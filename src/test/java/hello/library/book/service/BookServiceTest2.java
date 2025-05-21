package hello.library.book.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.common.exception.BusinessException;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BookServiceTest2 {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    void registerBook() {
        bookRepository.save(Book.builder()
            .bookName("모모")
            .author("미하엘 엔데")
            .isbn("1111111111111")
            .publisher("비룡소")
            .publicationDate(LocalDate.of(2000, 1, 1))
            .category("소설")
            .pages(300)
            .build());
    }
    @Test
    @DisplayName("책 조회 성공 - Id로 조회")
    void getBookById_success() {
        // given
        Book book = Book.builder()
            .bookName("모모")
            .author("미하엘 엔데")
            .publisher("비룡소")
            .publicationDate(LocalDate.of(2000, 5, 1))
            .isbn("9781234567890")
            .category("소설")
            .pages(300)
            .build();
        Book savedBook = bookRepository.save(book);

        // when
        Book foundBook = bookService.getBookById(savedBook.getBookId());

        // then
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getBookName()).isEqualTo("모모");
        assertThat(foundBook.getIsbn()).isEqualTo("9781234567890");
    }


    @Test
    @DisplayName("책 조회 실패 - 존재하지 않는 Id")
    void getBookByIsbn_fail() {

        assertThatThrownBy(() -> bookService.getBookById(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessage("해당 Id를 가진 책이 존재하지 않습니다.");
    }
    @Test
    @DisplayName("모든 책 조회")
    void getAllBooks() {
        registerBook();
        List<Book> books = bookService.getAllBooks();
        assertThat(books).hasSize(1);
    }

    @Test
    @DisplayName("책 제목으로 검색 - 키워드 포함")
    void searchBooksByTitle_keyword() {
        registerBook();

        List<Book> result = bookService.searchBooksByTitle("모");

        assertThat(result).hasSize(1);
        assertThat(result).extracting(Book::getBookName)
            .contains("모모");
    }

    @Test
    @DisplayName("책 제목으로 검색 - 키워드 불일치")
    void searchBooksByTitle_noMatch() {
        registerBook();

        List<Book> result = bookService.searchBooksByTitle("없는책");

        assertThat(result).isEmpty();
    }
}
