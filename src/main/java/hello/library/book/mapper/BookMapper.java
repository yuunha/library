package hello.library.book.mapper;

import hello.library.book.dto.BookRequest;
import hello.library.book.dto.BookResponse;
import hello.library.book.entity.Book;

public class BookMapper {

	// BookRequest → Book
	public static Book toEntity(BookRequest request) {
		return Book.builder()
			.bookName(request.getBookName())
			.author(request.getAuthor())
			.publisher(request.getPublisher())
			.publicationDate(request.getPublicationDate())
			.isbn(request.getIsbn())
			.category(request.getCategory())
			.pages(request.getPages())
			.build();
	}

	//Book -> BookResponse
	public static BookResponse toResponse(Book book) {
		return BookResponse.builder()
			.bookId(book.getBookId())
			.bookName(book.getBookName())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.isbn(book.getIsbn())
			.category(book.getCategory())
			.pages(book.getPages())
			.build();
	}
}