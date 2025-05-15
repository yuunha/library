package hello.library.book.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId; //null 허용

	@Column(nullable = false)
	private String bookName;

	@Column(nullable = false)
	private String author;

	private String publisher;

	private LocalDate publicationDate;

	@Column(nullable = false, unique = true)
	private String isbn;

	private String category;

	private Integer pages;
}