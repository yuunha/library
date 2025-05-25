package hello.library.book.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.mapper.BookMapper;
import hello.library.book.repository.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest1 {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void clean() {
		bookRepository.deleteAll();
	}

	@Test
	@DisplayName("책 등록 성공")
	void registerBook_success() throws Exception {
		// given
		BookRequest request = BookRequest.builder()
			.bookName("모모")
			.author("미하엘 엔데")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.isbn("9781234567890")
			.category("소설")
			.pages(300)
			.build();

		// when & then
		mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());

		// DB에 저장됐는지 검증
		List<Book> books = bookRepository.findAll();
		assertThat(books).hasSize(1);
		assertThat(books.get(0).getIsbn()).isEqualTo("9781234567890");
		assertThat(books.get(0).getBookName()).isEqualTo("모모");
		assertThat(books.get(0).getAuthor()).isEqualTo("미하엘 엔데");
		assertThat(books.get(0).getPublisher()).isEqualTo("비룡소");
		assertThat(books.get(0).getPublicationDate()).isEqualTo(LocalDate.of(2000, 1, 1));

	}

	@Test
	@DisplayName("책 등록 실패 - 동일한 isbn")
	void registerBook_fail() throws Exception {
		// given
		BookRequest request = BookRequest.builder()
			.bookName("모모")
			.author("미하엘 엔데")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.isbn("9781234567890")
			.category("소설")
			.pages(300)
			.build();
		//Book 객체 저장
		Book savedBook = bookRepository.save(BookMapper.toEntity(request));

		// when & then
		mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("BOOK-002"));

	}

	@Test
	@DisplayName("책 등록 실패 - 필수 필드 누락")
	void registerBook_fail2() throws Exception {
		BookRequest request1 = BookRequest.builder()
			.bookName("모모")
			.author("미하엘 엔데")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.category("소설")
			.pages(300)
			.build();
		// when & then
		mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request1)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isbn").value("ISBN은 필수입니다."));

		BookRequest request2 = BookRequest.builder()
			.author("미하엘 엔데")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.category("소설")
			.isbn("9781234567890")
			.pages(300)
			.build();
		// when & then
		mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request2)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.bookName").value("책 이름은 필수입니다."));
	}


	@Test
	@DisplayName("책 삭제 by BookId 성공")
	void deleteBook_success() throws Exception {
		// given
		BookRequest request = BookRequest.builder()
			.bookName("모모")
			.author("미하엘 엔데")
			.publisher("비룡소")
			.publicationDate(LocalDate.of(2000, 1, 1))
			.isbn("9781234567890")
			.category("소설")
			.pages(300)
			.build();
		//Book 객체 저장
		Book savedBook = bookRepository.save(BookMapper.toEntity(request));

		Long bookId = savedBook.getBookId();

		// when & then
			mockMvc.perform(delete("/book/" + bookId)
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("책 삭제 실패 - 존재하지 않는 bookId")
	void deleteBook_fail() throws Exception {
		// when & then
		mockMvc.perform(delete("/book/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errorCode").value("BOOK-001"));
	}

}