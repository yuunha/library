package hello.library.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
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
}
