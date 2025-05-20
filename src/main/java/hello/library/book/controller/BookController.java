package hello.library.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.library.book.dto.BookRequest;
import hello.library.book.dto.BookResponse;
import hello.library.book.entity.Book;
import hello.library.book.mapper.BookMapper;
import hello.library.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@PostMapping
	public ResponseEntity<Book> registerBook(@Valid @RequestBody BookRequest request) {
		Book savedBook = bookService.registerBook(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteBook(@RequestParam("bookId") long id) {
		bookService.deleteBookById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<BookResponse> getBook(@RequestParam("bookId") long id) {
		Book book = bookService.getBookById(id);
		BookResponse response = BookMapper.toResponse(book);
		return ResponseEntity.ok(response);
	}

}
