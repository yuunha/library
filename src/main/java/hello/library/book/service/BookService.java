package hello.library.book.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.common.exception.BusinessException;
import hello.library.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;

	@Transactional
	public Book registerBook(BookRequest request) {

		//이미 존재하는 책인지 확인
		if (bookRepository.existsByIsbn(request.getIsbn())) {
			throw new BusinessException(ErrorCode.DUPLICATE_ISBN, "이미 존재하는 책입니다.(ISBN중복)");
		}

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

	// @Transactional
	// public void deleteByIsbn(String isbn) {
	// 	Book book = bookRepository.findByIsbn(isbn)
	// 		.orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND,"해당 isbn을 가진 책이 존재하지 않습니다."));
	// 	bookRepository.delete(book);
	// }
	@Transactional
	public void deleteBookById(long id) {
		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND,"해당 Id를 가진 책이 존재하지 않습니다."));
		bookRepository.delete(book);
	}

	// public Book getBookByIsbn(String isbn) {
	// 	return bookRepository.findByIsbn(isbn)
	// 		.orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND,"해당 ISBN을 가진 책이 존재하지 않습니다."));
	// }
	public Book getBookById(long id) {
		return bookRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND,"해당 Id를 가진 책이 존재하지 않습니다."));
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