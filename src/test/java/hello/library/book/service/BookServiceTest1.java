package hello.library.book.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.common.exception.BusinessException;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BookServiceTest1 {

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
	@DisplayName("책 등록 성공 - 모든 필드 들어옴")
	public void BookRegisterSuccess1() {
		BookRequest request = BookRequest.builder()
			.bookName("나미야 잡화점의 기적")
			.author("히가시노 게이고")
			.publisher("현대문학")
			.publicationDate(LocalDate.of(2020, 5, 1))
			.isbn("9781234567890")
			.category("소설")
			.pages(400)
			.build();

		// when
		Book savedBook = bookService.registerBook(request);

		// then
		assertThat(savedBook.getBookId()).isNotNull();
		assertThat(savedBook.getBookName()).isEqualTo("나미야 잡화점의 기적");
		assertThat(bookRepository.findById(savedBook.getBookId())).isPresent();
	}
	@Test
	@DisplayName("책 등록 성공 - 필수 필드만 들어옴")
	public void BookRegisterSuccess2() {
		BookRequest request = BookRequest.builder()
			.bookName("산책")
			.author("김영하")
			.isbn("9788901234567")
			.build();

		// when
		Book savedBook = bookService.registerBook(request);

		// then
		assertThat(savedBook.getBookId()).isNotNull();
		assertThat(savedBook.getBookName()).isEqualTo("산책");
		assertThat(savedBook.getIsbn()).isEqualTo("9788901234567");
		assertThat(bookRepository.findById(savedBook.getBookId())).isPresent();
	}
	@Test
	@DisplayName("책 등록 실패 - 이미 존재하는 책 등록(isbn 동일)")
	public void BookRegisterFail1() {
		//given
		registerBook();

		BookRequest duplicateBookRequest = BookRequest.builder()
			.bookName("모모")
			.author("미하엘 엔데")
			.isbn("1111111111111")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.category("소설")
			.pages(300)
			.build();


		assertThatThrownBy(() -> bookService.registerBook(duplicateBookRequest))
			.isInstanceOf(BusinessException.class)
			.hasMessage("이미 존재하는 책입니다.(ISBN중복)");
	}

	//책 삭제
	@Test
	@DisplayName("책 삭제 성공 by Id")
	void deleteBookSuccess() {
		// given
		Book book = Book.builder()
			.bookName("테스트책")
			.author("저자")
			.publisher("출판사")
			.publicationDate(LocalDate.now())
			.isbn("9781234567890")
			.category("소설")
			.pages(200)
			.build();
		Book savedBook= bookRepository.save(book);

		// when
		bookService.deleteBookById(savedBook.getBookId());

		// then
		assertThat(bookRepository.findById(savedBook.getBookId())).isEmpty();
	}

	@Test
	@DisplayName("책 삭제 실패 - 존재하지 않는 Id")
	void deleteBookFail() {
		assertThatThrownBy(() -> bookService.deleteBookById(1L))
			.isInstanceOf(BusinessException.class)
			.hasMessage("해당 isbn을 가진 책이 존재하지 않습니다.");

	}

}
