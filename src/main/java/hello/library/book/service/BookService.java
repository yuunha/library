package hello.library.book.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;

	@Transactional
	public Book registerBook(BookRequest request) {
		Book book = Book.builder()
			.bookName(request.getBookName())
			.author(request.getAuthor())
			.publisher(request.getPublisher())
			.publicationDate(request.getPublicationDate())
			.isbn(request.getIsbn())
			.category(request.getCategory())
			.pages(request.getPages())
			.build();

		return bookRepository.save(book);
	}

	@Transactional
	public void deleteByIsbn(String isbn) {
		Book book = bookRepository.findByIsbn(isbn)
			.orElseThrow(() -> new NoSuchElementException("해당 ISBN의 책이 존재하지 않습니다."));
		bookRepository.delete(book);
	}

	public Book getBookByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn)
			.orElseThrow(() -> new NoSuchElementException("해당 ISBN의 책이 존재하지 않습니다."));
	}
	// 모든 책 조회
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	// 책 제목으로 조회 (부분 일치 포함)
	public List<Book> searchBooksByTitle(String keyword) {
		return bookRepository.findByBookNameContainingIgnoreCase(keyword);
	}
}