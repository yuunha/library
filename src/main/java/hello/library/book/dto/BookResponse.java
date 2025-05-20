package hello.library.book.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

	private Long bookId;
	private String bookName;
	private String author;
	private String publisher;
	private LocalDate publicationDate;
	private String isbn;
	private String category;
	private Integer pages; // DB에서 null 허용이면 Integer, 아니면 int

}