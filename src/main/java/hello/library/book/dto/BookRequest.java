package hello.library.book.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
	@NotBlank(message = "책 이름은 필수입니다.")
	@Size(max = 100, message = "책 이름은 최대 100자까지 가능합니다.")
	private String bookName;

	@NotBlank(message = "저자는 필수입니다.")
	@Size(max = 50, message = "저자명은 최대 50자까지 가능합니다.")
	private String author;

	private String publisher;

	private LocalDate publicationDate;

	@NotBlank(message = "ISBN은 필수입니다.")
	//isbn 유효성 테스트
	private String isbn;

	private String category;

	@Min(value = 1, message = "페이지 수는 1 이상이어야 합니다.")
	private int pages;
}